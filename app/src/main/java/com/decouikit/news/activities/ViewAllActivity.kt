package com.decouikit.news.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.decouikit.news.R
import com.decouikit.news.activities.common.BaseActivity
import com.decouikit.news.adapters.ViewAllAdapter
import com.decouikit.news.database.Config
import com.decouikit.news.database.InMemory
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.extensions.openPostActivity
import com.decouikit.news.interfaces.OpenPostListener
import com.decouikit.news.network.PostsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.CategoryType
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.utils.ActivityUtil
import com.decouikit.news.utils.EndlessRecyclerOnScrollListener
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.activity_view_all.*
import org.jetbrains.anko.doAsync

class ViewAllActivity : BaseActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
    OpenPostListener {

    private var categoryId: Int? = 0
    private var categoryName: String? = ""
    private var categoryType: CategoryType = CategoryType.ALL
    private var isDataLoading = false
    private val allMediaList = InMemory.getMediaList()
    private lateinit var adapter: ViewAllAdapter
    private val items = arrayListOf<PostItem>()
    private val postService by lazy {
        RetrofitClientInstance.getRetrofitInstance(this)?.create(PostsService::class.java)
    }
    private var page = 0
    private val perPage = Config.getNumberOfItemPerPage()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all)

        ActivityUtil.setLayoutDirection(this, getLayoutDirection(), R.id.viewAllParent)
        categoryId = intent.getIntExtra(NewsConstants.CATEGORY_ID, -1)
        categoryName = intent.getStringExtra(NewsConstants.CATEGORY_NAME)
        categoryType = CategoryType.getType(
            intent.getIntExtra(NewsConstants.CATEGORY_FEATURES, CategoryType.ALL.ordinal)
        )
        initLayout()
        initListeners()
        initValues()
    }

    private fun initValues() {
        when (categoryType) {
            CategoryType.ALL -> {
                tvTitle.setText(R.string.recent_news)
            }
            CategoryType.FEATURED -> {
                tvTitle.setText(R.string.featured_news)
            }
            CategoryType.RECENT -> {
                tvTitle.setText(R.string.recent_news)
            }
        }

        rvItems.addOnScrollListener(object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore() {
                onRefresh()
            }
        })
        refreshContent()
    }

    private fun loadData() {
        when (categoryType) {
            CategoryType.ALL -> {
                getAllPosts()
            }
            CategoryType.FEATURED -> {
                getPostsWithSticky(true)
            }
            CategoryType.RECENT -> {
                getPostsWithSticky(false)
            }
        }
    }

    private fun initLayout() {
        if (Preference(this).isRtlEnabled) {
            ivBack.rotation = 180f
        }
        adapter = ViewAllAdapter(arrayListOf(), this)
        rvItems.layoutManager = LinearLayoutManager(this)
        rvItems.adapter = adapter
    }

    private fun initListeners() {
        ivBack.setOnClickListener(this)
        swipeRefresh.setOnRefreshListener(this)
    }

    override fun onClick(v: View) {
        when (v) {
            ivBack -> onBackPressed()
        }
    }

    override fun openPost(view: View, item: PostItem) {
        view.openPostActivity(view.context, item)
    }

    override fun onRefresh() {
        if (!swipeRefresh.isRefreshing) {
            swipeRefresh.isRefreshing = true
            refreshContent()
        }
    }

    private fun refreshContent() {
        hideAllOnRefresh()
        mShimmerViewContainer.visibility = View.VISIBLE
        mShimmerViewContainer.startShimmerAnimation()
        items.removeAll { true }
        adapter.removeAllItems()
        page = 0
        loadData()
    }

    private fun getPostsWithSticky(sticky: Boolean) {
        doAsync {
            isDataLoading = true
            postService?.getPostsByCategoryWithSticky(
                categoryId.toString(),
                ++page,
                perPage,
                sticky
            )?.enqueue(result = {
                isDataLoading = false
                when (it) {
                    is Result.Success -> {
                        if (it.response.body().isNullOrEmpty() && adapter.itemCount == 0) {
                            hideContent(true)
                        } else {
                            val posts = it.response.body() as ArrayList<PostItem>
                            for (postItem in posts) {
                                for (mediaItem in allMediaList) {
                                    if (mediaItem.id == postItem.featured_media) {
                                        //loop for getting image urls, post name is fixed from intent
                                        postItem.categoryName = categoryName.toString()
                                        postItem.source_url = mediaItem.source_url
                                        items.add(postItem)
                                    }
                                }
                            }
                            if (items.isEmpty()) {
                                hideContent(true)
                            } else {
                                hideContent(false)
                                adapter.setData(items)
                            }
                        }
                    }
                    is Result.Failure -> {
                        hideContent(true)
                    }
                }
                mShimmerViewContainer.stopShimmerAnimation()
                mShimmerViewContainer.visibility = View.GONE
                swipeRefresh.isRefreshing = false
            })
        }
    }

    private fun getAllPosts() {
        doAsync {
            isDataLoading = true
            postService?.getPostsByCategoryWithSticky(
                categoryId.toString(),
                ++page,
                perPage
            )
                ?.enqueue(result = {
                    isDataLoading = false
                    when (it) {
                        is Result.Success -> {
                            if (it.response.body().isNullOrEmpty() && adapter.itemCount == 0) {
                                hideContent(true)
                            } else {
                                val posts = it.response.body() as ArrayList<PostItem>
                                for (postItem in posts) {
                                    for (mediaItem in allMediaList) {
                                        if (mediaItem.id == postItem.featured_media) {
                                            //loop for getting image urls, post name is fixed from intent
                                            postItem.categoryName = categoryName.toString()
                                            postItem.source_url = mediaItem.source_url
                                            items.add(postItem)
                                        }
                                    }
                                }
                                if (items.isEmpty()) {
                                    hideContent(true)
                                } else {
                                    hideContent(false)
                                    adapter.setData(items)
                                }
                            }
                        }
                        is Result.Failure -> {
                            hideContent(true)
                        }
                    }
                    mShimmerViewContainer.stopShimmerAnimation()
                    mShimmerViewContainer.visibility = View.GONE
                    swipeRefresh.isRefreshing = false
                })
        }
    }

    private fun hideContent(isListEmpty: Boolean) {
        if (isListEmpty) {
            tvTitle.visibility = View.GONE
            rvItems.visibility = View.GONE
            emptyPostsContainer.visibility = View.VISIBLE
        } else {
            tvTitle.visibility = View.VISIBLE
            rvItems.visibility = View.VISIBLE
            emptyPostsContainer.visibility = View.GONE
        }
    }

    private fun hideAllOnRefresh() {
        tvTitle.visibility = View.GONE
        rvItems.visibility = View.GONE
        emptyPostsContainer.visibility = View.GONE
    }
}
