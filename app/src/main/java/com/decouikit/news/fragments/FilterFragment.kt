package com.decouikit.news.fragments

import android.os.Bundle
import android.util.Log
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
import com.decouikit.news.extensions.categoryToString
import com.decouikit.news.extensions.viewAll
import com.decouikit.news.interfaces.ResultListener
import com.decouikit.news.network.dto.CategoryType
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.network.sync.SyncPost
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.fragment_filter.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync

class FilterFragment : Fragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
    NestedScrollView.OnScrollChangeListener {

    private lateinit var itemView: View
    private var categoryId: Int? = null
    private lateinit var categoryName: String

    private lateinit var featuredAdapter: FeaturedNewsAdapter

    private lateinit var recentAdapter: RecentNewsAdapter
    private var recentPostItems = arrayListOf<PostItem>()
    private lateinit var recentManager: GridLayoutManager

    private var page = 0

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
        initLayout()
        initListeners()

        refreshContent()
    }

    private fun initLayout() {
        featuredAdapter = FeaturedNewsAdapter(arrayListOf(), itemView.context)
        recentAdapter = RecentNewsAdapter(arrayListOf())
        recentManager = GridLayoutManager(itemView.context, 2)
        itemView.rvRecentNews.layoutManager = recentManager
        itemView.rvRecentNews.adapter = recentAdapter
        itemView.rvRecentNews.setHasFixedSize(true)
    }

    private fun initListeners() {
        itemView.tvFeaturedNewsViewAll.setOnClickListener(this)
        itemView.tvRecentNewsViewAll.setOnClickListener(this)
        itemView.swipeRefresh.setOnRefreshListener(this)
        itemView.nestedParent.setOnScrollChangeListener(this)
    }

    private fun initData() {
        GlobalScope.launch(context = Dispatchers.Main) {
            delay(500)
            SyncPost.getPostsList(requireContext(), categoryId?.categoryToString(), null, ++page,
                Config.getNumberOfItemPerPage(), object : ResultListener<List<PostItem>> {
                    override fun onResult(value: List<PostItem>?) {
                        if (value?.size ?: 0 > 0) {
                            initFeaturedNews(value)
                            initRecentNews(value)
                            setShimmerAnimationVisibility(false)
                        } else {
                            setEmptyState(featuredAdapter.count == 0)
                        }
                        itemView.swipeRefresh.isRefreshing = false
                    }
                })
        }
    }

    private fun initFeaturedNews(value: List<PostItem>?) {
        val featuredPostItems = arrayListOf<PostItem>()
        var postCounter = 0
        if (value != null) {
            for (postItem in value) {
                postItem.categoryName = categoryName
                featuredPostItems.add(postItem)
                postCounter++
                if (postCounter == Config.getNumberOfItemForSlider()) {
                    break
                }
            }
        }
        featuredAdapter.setData(featuredPostItems)
        itemView.viewPager.adapter = featuredAdapter
        itemView.viewPager.offscreenPageLimit = Config.getNumberOfItemForSlider()
        setEmptyState(featuredAdapter.count == 0)
    }

    private fun initRecentNews(value: List<PostItem>?) {
        var start = Config.getNumberOfItemForSlider()
        val end = value?.size ?: 0
        if (start > end) {
            start = end
        }
        if (value != null) {
            for (postItem in value.subList(start, end)) {
                postItem.categoryName = categoryName
                if (!recentPostItems.contains(postItem)) {
                    recentPostItems.add(postItem)
                }
            }
        }
        recentAdapter.setData(recentPostItems)
        hideRecentNews(recentAdapter.itemCount == 0)
    }

    private fun loadMoreRecentData() {
        doAsync {

            SyncPost.getPostsList(
                requireContext(),
                getCategoryId(),
                null,
                ++page,
                Config.getNumberOfItemPerPage(),
                object : ResultListener<List<PostItem>> {
                    override fun onResult(value: List<PostItem>?) {
                        value?.forEach { postItem ->
                            if (!recentPostItems.contains(postItem)) {
                                recentPostItems.add(postItem)
                            }
                        }
                        recentAdapter.setData(recentPostItems)
                        itemView.swipeRefresh.isRefreshing = false
                    }
                })
        }
    }

    private fun getCategoryId(): String? {
        return if (categoryId != 0) {
            categoryId.toString()
        } else {
            null
        }
    }

    override fun onRefresh() {
        itemView.swipeRefresh.isRefreshing = true
        refreshContent()
    }

    private fun refreshContent() {
        itemView.viewPager.removeAllViews()
        setShimmerAnimationVisibility(true)
        page = 0
        featuredAdapter.removeAllItems()
        recentAdapter.removeAllItems()
        recentPostItems.removeAll { true }
        initData()
    }

    private fun setShimmerAnimationVisibility(isVisible: Boolean) {
        activity?.runOnUiThread {
            if (isVisible) {
                mShimmerFeaturedNewsContainer.visibility = View.VISIBLE
                mShimmerFeaturedNewsContainer.startShimmer()
                mShimmerRecentNewsContainer.visibility = View.VISIBLE
                mShimmerRecentNewsContainer.startShimmer()
            } else {
                mShimmerFeaturedNewsContainer.stopShimmer()
                mShimmerFeaturedNewsContainer.visibility = View.GONE
                mShimmerRecentNewsContainer.stopShimmer()
                mShimmerRecentNewsContainer.visibility = View.GONE
            }
        }
    }

    override fun onClick(v: View) {
        when (v) {
            itemView.tvFeaturedNewsViewAll -> {
                if (Config.isFeaturesPostsGetFromSticky()) {
                    v.viewAll(v.context, categoryId, categoryName, CategoryType.FEATURED.ordinal)
                } else {
                    v.viewAll(v.context, categoryId, categoryName, CategoryType.RECENT.ordinal)
                }
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
                    loadMoreRecentData()
                }
            }
        }
    }

    private fun setEmptyState(isVisible: Boolean) {
        Log.e("OkHttp", "setEmptyState $isVisible")
        if (isVisible) {
            itemView.nestedParent.visibility = View.GONE
            itemView.emptyStateContainer.visibility = View.VISIBLE
        } else {
            itemView.nestedParent.visibility = View.VISIBLE
            itemView.emptyStateContainer.visibility = View.GONE
        }
    }

    private fun hideRecentNews(isEmpty: Boolean) {
        if (isEmpty) {
            itemView.tvRecentNewsTitle.visibility = View.GONE
            itemView.tvRecentNewsViewAll.visibility = View.GONE
            itemView.rlRecentNews.visibility = View.GONE
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