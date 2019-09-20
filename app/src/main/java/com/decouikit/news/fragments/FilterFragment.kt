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
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.extensions.viewAll
import com.decouikit.news.network.PostsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.utils.EndlessRecyclerOnScrollListener
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.fragment_filter.view.*
import org.jetbrains.anko.doAsync

class FilterFragment : Fragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var itemView: View
    private var categoryId: Int? = null
    private lateinit var categoryName: String

    private val postsService = RetrofitClientInstance.retrofitInstance?.create(PostsService::class.java)
    private var allMediaList = InMemory.getMediaList()

    private lateinit var allPostList: List<PostItem>

    private lateinit var featuredAdapter: FeaturedNewsAdapter

    private lateinit var recentAdapter: RecentNewsAdapter
    private var recentPostItems = arrayListOf<PostItem>()

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
    }
    override fun onResume() {
        super.onResume()
        itemView.nestedParent.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener
        { _, _, _, _, _ ->

            swipeRefresh.isRefreshing = true
            loadMoreRecentData()
        })
        refreshContent()
    }

    private fun initLayout() {
        featuredAdapter = FeaturedNewsAdapter(arrayListOf(), itemView.context)

        recentAdapter = RecentNewsAdapter(arrayListOf())
        itemView.rvRecentNews.layoutManager = GridLayoutManager(itemView.context, 2)
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
            postsService?.getPostsByCategory(categoryId.toString(), ++page, Config.getNumberOfItemPerPage())?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (it.response.body() != null) {
                            allPostList = it.response.body() as ArrayList<PostItem>
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
            for (mediaItem in allMediaList) {
                if (mediaItem.id == postItem.featured_media) {
                    postItem.source_url = mediaItem.source_url
                }
            }
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
    }

    private fun initRecentNews() {
        for (postItem in allPostList.subList(Config.getNumberOfItemForSlider(), allPostList.size)) {
            postItem.categoryName = categoryName
            for (mediaItem in allMediaList) {
                if (mediaItem.id == postItem.featured_media) {
                    postItem.source_url = mediaItem.source_url
                }
            }
            recentPostItems.add(postItem)
        }
        recentAdapter.setData(recentPostItems)
    }

    private fun loadMoreRecentData() {
        doAsync {
            postsService?.getPostsByCategory(categoryId.toString(), ++page, Config.getNumberOfItemPerPage())?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (it.response.body() != null) {
                            val allPostList = it.response.body() as ArrayList<PostItem>
                            for (postItem in allPostList) {
                                postItem.categoryName = categoryName
                                for (mediaItem in allMediaList) {
                                    if (mediaItem.id == postItem.featured_media) {
                                        postItem.source_url = mediaItem.source_url
                                    }
                                }
                                recentPostItems.add(postItem)
                            }
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
                v.viewAll(v.context, categoryId, categoryName)
            }
            itemView.tvRecentNewsViewAll -> {
                v.viewAll(v.context, categoryId, categoryName)
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