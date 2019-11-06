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
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.categoryToString
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.extensions.viewAll
import com.decouikit.news.network.PostsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.CategoryType
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.fragment_filter.view.*
import org.jetbrains.anko.doAsync

class FilterFragment : Fragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
    NestedScrollView.OnScrollChangeListener {

    private lateinit var itemView: View
    private var categoryId: Int? = null
    private lateinit var categoryName: String

    private val postsService by lazy {
        RetrofitClientInstance.getRetrofitInstance(requireContext())
            ?.create(PostsService::class.java)
    }

    private lateinit var allPostList: List<PostItem>

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
    }

    private fun initListeners() {
        itemView.tvFeaturedNewsViewAll.setOnClickListener(this)
        itemView.tvRecentNewsViewAll.setOnClickListener(this)
        itemView.swipeRefresh.setOnRefreshListener(this)
    }

    private fun initData() {
        doAsync {
            postsService?.getPostsList(
                categoryId?.categoryToString(),
                null,
                ++page,
                Config.getNumberOfItemPerPage()
            )?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (!it.response.body().isNullOrEmpty()) {
                            allPostList = it.response.body() as ArrayList<PostItem>
                            allPostList.sortedBy { it.modified_gmt }
                            initFeaturedNews()
                            initRecentNews()
                        }
                        setShimmerAnimationVisibility(false)
                        itemView.swipeRefresh.isRefreshing = false
                    }
                }
            })
        }
    }

    private fun initFeaturedNews() {
        val featuredPostItems = arrayListOf<PostItem>()
        var postCounter = 0
        for (postItem in allPostList) {
            postItem.categoryName = categoryName
            featuredPostItems.add(postItem)
            postCounter++
            if (postCounter == Config.getNumberOfItemForSlider()) {
                break
            }
        }
        featuredAdapter.setData(featuredPostItems)
        itemView.viewPager.adapter = featuredAdapter
        itemView.viewPager.offscreenPageLimit = Config.getNumberOfItemForSlider()
        itemView.viewPager.setCurrentItem(
            1,
            true
        )
        setEmptyState(featuredAdapter.count == 0)
    }

    private fun initRecentNews() {
        var start = Config.getNumberOfItemForSlider()
        val end = allPostList.size
        if (start > end) {
            start = end
        }
        for (postItem in allPostList.subList(start, end)) {
            postItem.categoryName = categoryName
            if (!recentPostItems.contains(postItem)) {
                recentPostItems.add(postItem)
            }
        }
        recentAdapter.setData(recentPostItems)

        hideRecentNews(recentAdapter.itemCount == 0)
    }

    private fun loadMoreRecentData() {
        doAsync {
            postsService?.getPostsList(
                categoryId.toString(), null,
                ++page,
                Config.getNumberOfItemPerPage()
            )?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (!it.response.body().isNullOrEmpty()) {
                            val allPostList = it.response.body() as ArrayList<PostItem>
                            for (postItem in allPostList) {
                                postItem.categoryName = categoryName
                                if (!recentPostItems.contains(postItem)) {
                                    recentPostItems.add(postItem)
                                }
                            }
                            allPostList.sortedBy { it.modified_gmt }
                            recentAdapter.setData(recentPostItems)
                        }
                    }
                }
                itemView.swipeRefresh.isRefreshing = false
            })
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