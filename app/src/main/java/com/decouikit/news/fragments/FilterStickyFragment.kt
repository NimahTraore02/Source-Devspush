package com.decouikit.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.decouikit.news.R
import com.decouikit.news.adapters.FeaturedNewsAdapter
import com.decouikit.news.adapters.RecentNewsAdapter
import com.decouikit.news.database.Config
import com.decouikit.news.extensions.viewAll
import com.decouikit.news.interfaces.ResultListener
import com.decouikit.news.network.dto.CategoryType
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.network.sync.SyncPost
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.fragment_filter.view.*
import org.jetbrains.anko.doAsync

class FilterStickyFragment : Fragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
    NestedScrollView.OnScrollChangeListener {

    private lateinit var itemView: View
    private var categoryId: Int? = null
    private lateinit var categoryName: String

    private lateinit var featuredAdapter: FeaturedNewsAdapter

    private lateinit var recentAdapter: RecentNewsAdapter
    private var recentPostItems = arrayListOf<PostItem>()
    private lateinit var recentManager: GridLayoutManager

    private var page = 0
    private var featuresSync = false
    private var recentSync = false

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
        itemView = inflater.inflate(R.layout.fragment_filter_sticky, container, false)
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        initListeners()
        itemView.nestedParent.setOnScrollChangeListener(this)
        refreshContent()
    }

    private fun initLayout() {
        featuredAdapter = FeaturedNewsAdapter(arrayListOf(), itemView.context)
        recentAdapter = RecentNewsAdapter(arrayListOf())
        recentManager = GridLayoutManager(itemView.context, 2)
        itemView.rvRecentNews.layoutManager = recentManager
        itemView.rvRecentNews.adapter = recentAdapter
        itemView.rvRecentNews.setHasFixedSize(true)
        setShimmerAnimationVisibility(true)
    }

    private fun initListeners() {
        itemView.tvFeaturedNewsViewAll.setOnClickListener(this)
        itemView.tvRecentNewsViewAll.setOnClickListener(this)
        itemView.swipeRefresh.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        itemView.swipeRefresh.isRefreshing = true
        refreshContent()
    }

    private fun refreshContent() {
        itemView.viewPager.removeAllViews()
        setShimmerAnimationVisibility(true)
        page = 0
        featuresSync = false
        recentSync = false
        featuredAdapter.removeAllItems()
        recentAdapter.removeAllItems()
        recentPostItems.removeAll { true }
        getFeaturedNews()
        getRecentNews()
    }

    private fun setShimmerAnimationVisibility(isVisible: Boolean) {
        activity?.runOnUiThread {
            if (isVisible) {
                mShimmerFeaturedNewsContainer.visibility = View.VISIBLE
                mShimmerFeaturedNewsContainer.startShimmerAnimation()
                mShimmerRecentNewsContainer.visibility = View.VISIBLE
                mShimmerRecentNewsContainer.startShimmerAnimation()
            } else {
                mShimmerFeaturedNewsContainer.stopShimmerAnimation()
                mShimmerFeaturedNewsContainer.visibility = View.GONE
                mShimmerRecentNewsContainer.stopShimmerAnimation()
                mShimmerRecentNewsContainer.visibility = View.GONE
            }
        }
    }

    private fun getFeaturedNews() {
        doAsync {
            SyncPost.getPostsList(
                requireContext(),
                categoryId.toString(),
                true,
                1,
                Config.getNumberOfItemPerPage(),
                object : ResultListener<List<PostItem>> {
                    override fun onResult(value: List<PostItem>?) {
                        featuresSync = true
                        if (value != null) {
                            if (value.isEmpty()) {
                                hideFeaturedNews(true)
                                checkEmptyState()
                            } else {
                                val featuredItems = value as ArrayList<PostItem>
                                featuredAdapter.setData(featuredItems)
                                itemView.viewPager.adapter = featuredAdapter
                                itemView.viewPager.offscreenPageLimit =
                                    Config.getNumberOfItemForSlider()
                                checkEmptyState()
                                hideFeaturedNews(featuredAdapter.count == 0)
                                if (featuresSync && recentSync) {
                                    setEmptyState(featuredAdapter.count == 0 && recentAdapter.itemCount == 0)
                                }
                            }
                        } else {
                            hideFeaturedNews(true)
                            checkEmptyState()
                        }
                    }
                }
            )
        }
    }

    private fun getRecentNews() {
        SyncPost.getPostsList(
            requireContext(),
            categoryId.toString(),
            false,
            ++page,
            Config.getNumberOfItemPerPage(),
            object : ResultListener<List<PostItem>> {
                override fun onResult(value: List<PostItem>?) {
                    recentSync = true
                    if (value != null) {
                        if (value.isNotEmpty()) {
                            val allPostList = value as ArrayList<PostItem>
                            for (postItem in allPostList) {
                                if (!recentPostItems.contains(postItem)) {
                                    recentPostItems.add(postItem)
                                }
                            }
                            recentAdapter.setData(recentPostItems)
                            hideRecentNews(recentAdapter.itemCount == 0)
                            checkEmptyState()
                        } else {
                            hideRecentNews(recentAdapter.itemCount == 0)
                            checkEmptyState()
                        }
                    } else {
                        hideRecentNews(recentAdapter.itemCount == 0)
                        checkEmptyState()
                    }
                }
            })
        swipeRefresh.isRefreshing = false
        setShimmerAnimationVisibility(false)
        checkEmptyState()
    }

    private fun checkEmptyState() {
        if (featuresSync && recentSync) {
            setEmptyState(featuredAdapter.count == 0 && recentAdapter.itemCount == 0)
        }
    }


    private fun setEmptyState(isVisible: Boolean) {
        recentSync = false
        featuresSync = false
        if (isVisible) {
            itemView.nestedParent.visibility = View.GONE
            itemView.emptyStateContainer.visibility = View.VISIBLE
        } else {
            itemView.nestedParent.visibility = View.VISIBLE
            itemView.emptyStateContainer.visibility = View.GONE
        }
    }

    private fun hideFeaturedNews(isEmpty: Boolean) {
        if (isEmpty) {
            itemView.tvFeaturedNewsTitle.visibility = View.GONE
            itemView.tvFeaturedNewsViewAll.visibility = View.GONE
            itemView.viewPager.visibility = View.GONE
        } else {
            itemView.tvFeaturedNewsTitle.visibility = View.VISIBLE
            itemView.tvFeaturedNewsViewAll.visibility = View.VISIBLE
            itemView.viewPager.visibility = View.VISIBLE
        }
    }

    private fun hideRecentNews(isEmpty: Boolean) {
        if (isEmpty) {
            itemView.tvRecentNewsTitle.visibility = View.GONE
            itemView.tvRecentNewsViewAll.visibility = View.GONE
            itemView.rlRecentNews.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        when (v) {
            itemView.tvFeaturedNewsViewAll -> {
                v.viewAll(v.context, categoryId, categoryName, CategoryType.FEATURED.ordinal)
            }
            itemView.tvRecentNewsViewAll -> {
                v.viewAll(v.context, categoryId, categoryName, CategoryType.RECENT.ordinal)
            }
        }
    }

    override fun onScrollChange(
        v: NestedScrollView?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        if (v?.getChildAt(v.childCount - 1) != null) {
            if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) &&
                scrollY > oldScrollY
            ) {

                val visibleItemCount = recentManager.childCount
                val totalItemCount = recentManager.itemCount
                val pastVisibleItems = recentManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    swipeRefresh.isRefreshing = true
                    getRecentNews()
                }
            }
        }
    }

    companion object {
        fun newInstance(categoryId: Int, categoryName: String): FilterStickyFragment {
            val fragment = FilterStickyFragment()
            val args = Bundle()
            args.putInt(NewsConstants.CATEGORY_ID, categoryId)
            args.putString(NewsConstants.CATEGORY_NAME, categoryName)
            fragment.arguments = args
            return fragment
        }
    }
}