package com.decouikit.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.decouikit.news.R
import com.google.android.material.tabs.TabLayout

class HomeFragment : Fragment(), TabLayout.OnTabSelectedListener {

    private lateinit var itemView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        itemView = inflater.inflate(R.layout.fragment_home, container, false)
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTabs()
    }

    private fun initTabs() {

        val mTabLayout = itemView.findViewById<TabLayout>(R.id.homeTab)
        mTabLayout.addOnTabSelectedListener(this)

        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_architecture))
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_art))
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_work))
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_current))
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_food))
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_travel))
    }

    override fun onTabReselected(p0: TabLayout.Tab?) {

    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {

    }

    override fun onTabSelected(p0: TabLayout.Tab?) {

    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}