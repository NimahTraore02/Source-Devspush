package com.decouikit.news.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.decouikit.news.R
import com.decouikit.news.adapters.ViewPagerAdapter
import com.decouikit.news.database.InMemory
import com.decouikit.news.interfaces.HomeFragmentListener
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment(), TabLayout.OnTabSelectedListener {

    private lateinit var itemView: View
    private lateinit var callback: HomeFragmentListener

    override fun onAttach(context: Context) {
        callback = context as HomeFragmentListener
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        itemView = inflater.inflate(R.layout.fragment_home, container, false)
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTabs()
    }

    private fun initTabs() {
        val adapter = fragmentManager?.let { ViewPagerAdapter(it) }

        val mTabLayout = itemView.findViewById<TabLayout>(R.id.homeTab)
        InMemory.getCategoryList().forEach {
            mTabLayout.addTab(mTabLayout.newTab().setText(it.name))
            adapter?.addFragment(FilterFragment.newInstance(it.id, it.name))
        }

        itemView.viewPager.adapter = adapter

        itemView.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mTabLayout))
        mTabLayout.addOnTabSelectedListener(this)

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        itemView.viewPager.currentItem = tab.position
    }

    override fun onResume() {
        super.onResume()
        callback.homeFragmentBehavior()
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}