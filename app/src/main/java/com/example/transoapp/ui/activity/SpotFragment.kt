package com.example.transoapp.ui.activity

import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.transoapp.R
import com.example.transoapp.databinding.FragmentSpotBinding
import com.example.transoapp.listener.EventListener
import com.example.transoapp.pojo.ApiRequestData
import com.example.transoapp.pojo.ExampleData
import com.example.transoapp.pojo.SymbolPriceTickerData
import com.example.transoapp.ui.activity.MainActivity.Companion.dialog
import com.example.transoapp.ui.adapter.SpotAdapter
import com.example.transoapp.viewmodels.MainViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import javax.net.ssl.SSLSocketFactory

class SpotFragment : Fragment() {

    val TAG = "SpotFragment"
    private var _binding: FragmentSpotBinding? = null
    private val binding get() = _binding!!
    lateinit var mainViewModel: MainViewModel
    lateinit var exampleAdapter: SpotAdapter
    private var exampleList = ArrayList<ExampleData.Doc>()
    private var webSocketClient: WebSocketClient? = null
    private val symbolsMap: ArrayMap<String, Int> = ArrayMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpotBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel =
            ViewModelProvider(requireActivity())[MainViewModel::class.java]
        setObserver()
        setAdapter()
    }

    private fun setObserver() {
        mainViewModel.exampleLiveData.observe(viewLifecycleOwner) {
            dialog?.dismiss()
            if (it != null) { //set response data
                Log.e(TAG, "SpotFragment")
                exampleList.clear()
                it.data?.docs?.let { it1 -> exampleList.addAll(it1.take(10)) }
                exampleAdapter.notifyDataSetChanged()
                symbolsMap.clear()
                exampleList.forEachIndexed { index, doc ->
                    symbolsMap.set(doc.pairName, index)
                }
                webSocketClient?.close()
                initWebSocket()
            } else {
                showToast(getString(R.string.something_wrong_validation))
            }
        }
    }

    private fun setAdapter() = with(binding) {
        exampleAdapter = SpotAdapter(exampleList, object : EventListener<ExampleData.Doc> {
            override fun onItemClick(pos: Int, item: ExampleData.Doc, view: View) {

            }
        })
        recyclerview.adapter = exampleAdapter
    }

    private fun initWebSocket() {
        val binanceUri = URI(MainActivity.binanceUrl)
        createWebSocketClient(binanceUri)
        val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        webSocketClient?.setSocketFactory(socketFactory)
        webSocketClient?.connect()
    }

    private fun createWebSocketClient(binanceUri: URI?) {
        webSocketClient = object : WebSocketClient(binanceUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d(TAG, "onOpen")
                subscribe()
            }

            override fun onMessage(message: String?) {
//                Log.d(TAG, "onMessage msg : $message")
                setUpPriceText(message)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(TAG, "onClose")
            }

            override fun onError(ex: Exception?) {
                Log.e(TAG, "onError: ${ex?.message}")
            }
        }
    }

    private fun subscribe() {
        val paramsList =
            exampleList.take(10).map { it.pairName?.lowercase().toString() + "@ticker" }
        val apiRequestData = ApiRequestData().apply {
            method = "SUBSCRIBE"
            params = paramsList
            id = 1
        }
        val request = Gson().toJson(apiRequestData)
        webSocketClient?.send(request)
    }

    private fun getParamsData() {
        val list: ArrayList<ExampleData.Doc> = exampleList.clone() as ArrayList<ExampleData.Doc>
        while (list.isNullOrEmpty().not() && list.size >= 10) {
            val paramsList =
                list.take(10).map { it.pairName?.lowercase().toString() + "@ticker" }
            val apiRequestData = ApiRequestData().apply {
                method = "SUBSCRIBE"
                params = paramsList
                id = 1
            }
            val request = Gson().toJson(apiRequestData)
            webSocketClient?.send(request)
            list.subList(0, 10).clear()
        }
    }

    private fun setUpPriceText(message: String?) {
        message?.let {
            val symbolPriceTickerData = Gson().fromJson<SymbolPriceTickerData>(
                message,
                object : TypeToken<SymbolPriceTickerData>() {}.type
            )
            if (symbolsMap.contains(symbolPriceTickerData?.s.toString())) {
                val position = symbolsMap[symbolPriceTickerData?.s.toString()]
                if (position != null) {
                    val item = exampleList.get(position)
                    item.priceChange = symbolPriceTickerData?.p?.toFloat()
                    item.priceChangePercent = symbolPriceTickerData?.pricePercentage?.toFloat()

                    requireActivity().runOnUiThread {
                        exampleAdapter.notifyItemChanged(position, "CHANGE_PRICE")
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        Log.e("webs", webSocketClient.toString())
        if (webSocketClient != null)
            initWebSocket()
    }

    override fun onPause() {
        super.onPause()
        webSocketClient?.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        webSocketClient?.close()
    }

}