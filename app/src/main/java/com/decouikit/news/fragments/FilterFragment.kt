package com.decouikit.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.decouikit.news.R
import com.decouikit.news.adapters.FeaturedNewsAdapter
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.fragment_filter.view.*

class FilterFragment: Fragment() {
    private lateinit var itemView: View
    private lateinit var filter: String

    private lateinit var featuredItems: ArrayList<String>
    private lateinit var featuredAdapter: FeaturedNewsAdapter

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
    }

    private fun initFeaturedNews() {
        featuredItems = ArrayList()
        featuredItems.add("Prvi")
        featuredItems.add("Drugi")
        featuredItems.add("Treci")
        featuredItems.add("Cetvrti")
        featuredAdapter = FeaturedNewsAdapter(featuredItems, itemView.context)

        itemView.viewPager.adapter = featuredAdapter
        itemView.viewPager.offscreenPageLimit = 3
        itemView.viewPager.setCurrentItem(1, true)//TODO selektovan prvi, promeniti nakon dobijanja pravih podataka
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