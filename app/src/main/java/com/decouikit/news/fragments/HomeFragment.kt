package com.decouikit.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.decouikit.news.R
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.replaceFragment
import com.google.android.material.tabs.TabLayout

class HomeFragment : Fragment(), TabLayout.OnTabSelectedListener {

    private lateinit var itemView: View
    private lateinit var fragment: FilterFragment

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
        InMemory.getCategoryList().forEach {
            mTabLayout.addTab(mTabLayout.newTab().setText(it.name))
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        fragment = FilterFragment.newInstance(InMemory.getCategoryList()[tab.position].id, InMemory.getCategoryList()[tab.position].name)
        replaceFragment(fragment, R.id.frmHomePlaceholder)
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}