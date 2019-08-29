package com.decouikit.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.decouikit.news.R
import com.decouikit.news.adapters.FeaturedNewsAdapter
import com.decouikit.news.adapters.RecentNewsAdapter
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.fragment_filter.view.*

class FilterFragment: Fragment() {
    private lateinit var itemView: View
    private lateinit var filter: String

    private lateinit var featuredItems: ArrayList<String>
    private lateinit var featuredAdapter: FeaturedNewsAdapter

    private lateinit var recentItems: ArrayList<String>
    private lateinit var recentAdapter: RecentNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filter = arguments?.getString(NewsConstants.FILTER, "").toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        itemView = inflater.inflate(R.layout.fragment_filter, container, false)
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFeaturedNews()
        initRecentNews()
    }

    private fun initFeaturedNews() {
        featuredItems = arrayListOf()
        featuredItems.add("Prvi")
        featuredItems.add("Drugi")
        featuredItems.add("Treci")
        featuredItems.add("Cetvrti")
        featuredAdapter = FeaturedNewsAdapter(featuredItems, itemView.context)

        itemView.viewPager.adapter = featuredAdapter
        itemView.viewPager.offscreenPageLimit = 3
        itemView.viewPager.setCurrentItem(1, true)//TODO selektovan prvi, promeniti nakon dobijanja pravih podataka
    }

    private fun initRecentNews() {
        recentItems = arrayListOf()
        recentItems.add("Sport")
        recentItems.add("Architecture")
        recentItems.add("Design")
        recentItems.add("Food")
        recentItems.add("Drink")
        recentItems.add("Design")
        recentItems.add("Architecture")

        recentAdapter = RecentNewsAdapter(recentItems)

        itemView.rvRecentNews.layoutManager = GridLayoutManager(itemView.context, 2)
        itemView.rvRecentNews.adapter = recentAdapter
        itemView.rvRecentNews.setHasFixedSize(true)
    }

    companion object {
        fun newInstance(filter: String): FilterFragment {
            val fragment = FilterFragment()
            val args = Bundle()
            args.putString(NewsConstants.FILTER, filter)
            fragment.arguments = args
            return fragment
        }
    }
}