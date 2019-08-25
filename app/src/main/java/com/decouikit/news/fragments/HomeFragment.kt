package com.decouikit.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.decouikit.news.R
import com.decouikit.news.extensions.replaceFragment
import com.google.android.material.tabs.TabLayout

class HomeFragment : Fragment(), TabLayout.OnTabSelectedListener {

    private lateinit var itemView: View
    private lateinit var fragment: HomeFilterFragment

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

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        when (tab?.position) {
            0 -> {
                fragment = HomeFilterFragment.newInstance(getString(R.string.tab_architecture))
            }
            1 -> {
                fragment = HomeFilterFragment.newInstance(getString(R.string.tab_art))
            }
            2 -> {
                fragment = HomeFilterFragment.newInstance(getString(R.string.tab_work))
            }
            3 -> {
                fragment = HomeFilterFragment.newInstance(getString(R.string.tab_current))
            }
            4 -> {
                fragment = HomeFilterFragment.newInstance(getString(R.string.tab_food))
            }
            5 -> {
                fragment = HomeFilterFragment.newInstance(getString(R.string.tab_travel))
            }
        }
        replaceFragment(fragment, R.id.frmHomePlaceholder)
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}