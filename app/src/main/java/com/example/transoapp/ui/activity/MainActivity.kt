package com.example.transoapp.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.transoapp.MyApp
import com.example.transoapp.R
import com.example.transoapp.databinding.ActivityMainBinding
import com.example.transoapp.ui.adapter.ViewPagerTravelPagerAdapter
import com.example.transoapp.utils.Utils.isNetworkAvailable
import com.example.transoapp.utils.Utils.setProgressDialog
import com.example.transoapp.viewmodels.MainViewModel
import com.example.transoapp.viewmodels.MainViewModelFactory
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ActivityMainBinding

    companion object {
        const val binanceUrl = "wss://stream.binance.com:9443/ws"
        var dialog: AlertDialog? = null
    }

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        (application as MyApp).applicationComponent.inject(this)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
        dialog = setProgressDialog(this, "Loading..")

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


    private fun initView() = with(binding) {
        setTabs()
        dialog?.show()
        callApi()
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


}