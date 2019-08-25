package com.decouikit.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.decouikit.news.R
import com.decouikit.news.adapters.FeaturedNewsAdapter
import com.decouikit.news.extensions.pxToDp
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.fragment_home_filter.*
import kotlinx.android.synthetic.main.fragment_home_filter.view.*

class HomeFilterFragment: Fragment() {
    private lateinit var itemView: View
    private lateinit var filter: String

    private lateinit var featuredItems: ArrayList<String>
    private lateinit var featuredAdapter: FeaturedNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filter = arguments?.getString(NewsConstants.HOME_FILTER, "").toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        itemView = inflater.inflate(R.layout.fragment_home_filter, container, false)
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        featuredItems = ArrayList()
        featuredItems.add("Prvi")
        featuredItems.add("Drugi")
        featuredItems.add("Treci")
        featuredItems.add("Cetvrti")

        featuredAdapter = FeaturedNewsAdapter(featuredItems, view.context)

        itemView.viewPager.adapter = featuredAdapter
        itemView.viewPager.offscreenPageLimit = 3
    }

    companion object {
        fun newInstance(filter: String): HomeFilterFragment {
            val fragment = HomeFilterFragment()
            val args = Bundle()
            args.putString(NewsConstants.HOME_FILTER, filter)
            fragment.arguments = args
            return fragment
        }
    }
}