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
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.replaceFragmentWithBackStack
import com.decouikit.news.network.dto.MediaItem
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.fragment_filter.view.*

class FilterFragment : Fragment(), View.OnClickListener {

    private lateinit var itemView: View
    private var categoryId: Int? = null
    private lateinit var categoryName: String
    private lateinit var allMediaList: List<MediaItem>

    private var featuredItems = arrayListOf<MediaItem>()
    private lateinit var featuredAdapter: FeaturedNewsAdapter

    private lateinit var recentItems: ArrayList<String>
    private lateinit var recentAdapter: RecentNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = arguments?.getInt(NewsConstants.CATEGORY_ID)
        categoryName = arguments?.getString(NewsConstants.CATEGORY_NAME, "").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        itemView = inflater.inflate(R.layout.fragment_filter, container, false)
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allMediaList = InMemory.getMediaList()

        initFeaturedNews()
        initRecentNews()
        initListeners()
    }

    private fun initFeaturedNews() {

        featuredAdapter = FeaturedNewsAdapter(featuredItems, itemView.context)

        itemView.viewPager.adapter = featuredAdapter
        itemView.viewPager.offscreenPageLimit = 3
        itemView.viewPager.setCurrentItem(
            1,
            true
        )//TODO selektovan prvi, promeniti nakon dobijanja pravih podataka
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

    private fun initListeners() {
        itemView.tvFeaturedNewsViewAll.setOnClickListener(this)
        itemView.tvRecentNewsViewAll.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v) {
            itemView.tvFeaturedNewsViewAll -> {
                replaceFragmentWithBackStack(
                    ViewAllFragment.newInstance(getString(R.string.featured_news)),
                    R.id.navigation_container
                )
            }
            itemView.tvRecentNewsViewAll -> {
                replaceFragmentWithBackStack(
                    ViewAllFragment.newInstance(getString(R.string.recent_news)),
                    R.id.navigation_container
                )
            }
        }
    }

    companion object {
        fun newInstance(categoryId: Int, categoryName: String): FilterFragment {
            val fragment = FilterFragment()
            val args = Bundle()
            args.putInt(NewsConstants.CATEGORY_ID, categoryId)
            args.putString(NewsConstants.CATEGORY_NAME, categoryName)
            fragment.arguments = args
            return fragment
        }
    }
}