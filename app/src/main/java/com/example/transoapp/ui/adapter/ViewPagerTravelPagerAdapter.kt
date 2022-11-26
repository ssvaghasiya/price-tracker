package com.example.transoapp.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.transoapp.ui.activity.SpotFragment
import com.example.transoapp.ui.activity.ZoneFragment


class ViewPagerTravelPagerAdapter(
    fragmentManager: FragmentManager,
    private val totalItems: Int,
) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getCount(): Int {
        return totalItems
    }

    override fun getItem(position: Int): Fragment {
        var fragment = Fragment()
        val bundle = Bundle()

        when (position) {
            0 ->
                fragment = SpotFragment()

            1 -> {
                fragment = ZoneFragment()
            }


        }

        fragment.arguments = bundle
        return fragment
    }

}