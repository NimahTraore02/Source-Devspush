package com.decouikit.news.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.decouikit.news.R
import com.decouikit.news.adapters.ViewPagerAdapter
import com.decouikit.news.database.Config
import com.decouikit.news.database.InMemory
import com.decouikit.news.interfaces.HomeFragmentListener
import com.decouikit.news.network.dto.Category
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
        callback.homeFragmentBehavior()
    }

    private fun initTabs() {
        val adapter = fragmentManager?.let { ViewPagerAdapter(it) }

        val mTabLayout = itemView.findViewById<TabLayout>(R.id.homeTab)
        if (Config.isAllScreenEnabled()) {
            mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.all)))
            adapter?.addFragment(FilterFragment.newInstance(0, ""))
        }
        InMemory.getCategoryList(requireContext()).forEach {
            mTabLayout.addTab(mTabLayout.newTab().setText(it.name))
            adapter?.addFragment(getFilterFragment(it))
        }
        itemView.viewPager.adapter = adapter
        itemView.viewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                mTabLayout
            )
        )
        mTabLayout.addOnTabSelectedListener(this)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        itemView.viewPager.currentItem = tab.position
    }

    private fun getFilterFragment(category: Category): Fragment {
        return if (Config.isFeaturesPostsGetFromSticky()) {
            FilterStickyFragment.newInstance(category.id, category.name)
        } else {
            FilterFragment.newInstance(category.id, category.name)
        }
    }

    private fun getFilterFragment(): Fragment {
        return if (Config.isFeaturesPostsGetFromSticky()) {
            FilterStickyFragment.newInstance(0, getString(R.string.menu_home))
        } else {
            FilterFragment.newInstance(0, getString(R.string.menu_home))
        }
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}