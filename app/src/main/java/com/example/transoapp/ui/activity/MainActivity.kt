package com.example.transoapp.ui.activity

import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.transoapp.MyApp
import com.example.transoapp.R
import com.example.transoapp.databinding.ActivityMainBinding
import com.example.transoapp.listener.EventListener
import com.example.transoapp.pojo.ApiRequestData
import com.example.transoapp.pojo.ExampleData
import com.example.transoapp.pojo.SymbolPriceTickerData
import com.example.transoapp.ui.adapter.ExampleAdapter
import com.example.transoapp.ui.adapter.ViewPagerTravelPagerAdapter
import com.example.transoapp.utils.Utils.isNetworkAvailable
import com.example.transoapp.utils.Utils.setProgressDialog
import com.example.transoapp.viewmodels.MainViewModel
import com.example.transoapp.viewmodels.MainViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import javax.inject.Inject
import javax.net.ssl.SSLSocketFactory

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var exampleAdapter: ExampleAdapter
    private var exampleList = ArrayList<ExampleData.Doc>()
    private var dialog: AlertDialog? = null
    private var webSocketClient: WebSocketClient? = null
    private val symbolsMap: ArrayMap<String, Int> = ArrayMap()

    companion object {
        const val binanceUrl = "wss://stream.binance.com:9443/ws"
    }

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        (application as MyApp).applicationComponent.inject(this)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
        dialog = setProgressDialog(this, "Loading..")

        setObserver()
        initView()
    }


    private fun setTabs() = with(binding) {
        val tabs = getTabs()
        for (i in tabs.indices) {
            tabLayout.addTab(tabLayout.newTab().setText(tabs[i]))
        }
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = ViewPagerTravelPagerAdapter(
            supportFragmentManager,
            tabLayout.tabCount,
        )
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                tabLayout
            )
        )

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }


    private fun getTabs(): ArrayList<String> {
        val arrayList = arrayListOf<String>()
        arrayList.add("Spot")
        arrayList.add("Zone")
        return arrayList
    }

    private fun setObserver() {
        mainViewModel.exampleLiveData.observe(this) {
            dialog?.dismiss()
            if (it != null) { //set response data
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


    private fun initView() = with(binding) {
        setTabs()
        setAdapter()
//        dialog?.show()
//        callApi()
    }

    private fun setAdapter() = with(binding) {
//        exampleAdapter = ExampleAdapter(exampleList, object : EventListener<ExampleData.Doc> {
//            override fun onItemClick(pos: Int, item: ExampleData.Doc, view: View) {
//
//            }
//        })
//        recyclerview.adapter = exampleAdapter
    }


    private fun callApi() = with(binding) {
        if (isNetworkAvailable(this@MainActivity)) {
            mainViewModel.callApi()
        } else {
            showToast(getString(R.string.internet_validation))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketClient?.close()
    }

    private fun initWebSocket() {
        val binanceUri = URI(binanceUrl)
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

                    runOnUiThread {
                        exampleAdapter.notifyItemChanged(position, "CHANGE_PRICE")
                    }
                }
            }
        }
    }
}