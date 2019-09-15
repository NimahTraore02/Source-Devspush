package com.decouikit.news.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.decouikit.news.R
import com.decouikit.news.activities.common.BaseActivity
import com.decouikit.news.adapters.ViewAllAdapter
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.extensions.openPostActivity
import com.decouikit.news.interfaces.OpenPostListener
import com.decouikit.news.network.PostsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.utils.EndlessRecyclerOnScrollListener
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.activity_view_all.*
import org.jetbrains.anko.doAsync

class ViewAllActivity : BaseActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, OpenPostListener {

    private var categoryId: Int? = 0
    private var categoryName: String? = ""
    private val allMediaList = InMemory.getMediaList()
    private lateinit var adapter: ViewAllAdapter
    private val items = arrayListOf<PostItem>()
    private val postService = RetrofitClientInstance.retrofitInstance?.create(PostsService::class.java)
    private var page = 0
    private val perPage = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all)

        categoryId = intent.getIntExtra(NewsConstants.CATEGORY_ID, -1)
        categoryName = intent.getStringExtra(NewsConstants.CATEGORY_NAME)
        initLayout()
        initListeners()
    }


    private fun initLayout() {
        adapter = ViewAllAdapter(arrayListOf(), this)
        rvItems.layoutManager = LinearLayoutManager(this)
        rvItems.adapter = adapter
    }

    private fun initListeners() {
        ivBack.setOnClickListener(this)
        swipeRefresh.setOnRefreshListener(this)
    }

    override fun onResume() {
        super.onResume()
        rvItems.addOnScrollListener(object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore() {
                swipeRefresh.isRefreshing = true
                loadData()
            }
        })
        refreshContent()
    }

    override fun onClick(v: View) {
        when (v) {
            ivBack -> {
                onBackPressed()
            }
        }
    }

    override fun openPost(view: View, item: PostItem) {
        view.openPostActivity(view.context, item)
    }

    override fun onRefresh() {
        swipeRefresh.isRefreshing = true
        refreshContent()
    }

    private fun refreshContent() {
        mShimmerViewContainer.visibility = View.VISIBLE
        mShimmerViewContainer.startShimmerAnimation()
        items.removeAll { true }
        adapter.removeAllItems()
        page = 0
        loadData()
    }


    private fun loadData() {
        getPosts()
    }

    private fun getPosts() {
        doAsync {
            postService?.getPostsByCategory(categoryId.toString(), ++page, perPage)?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (!it.response.body().isNullOrEmpty()) {
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
                            adapter.setData(items)
                        }
                        mShimmerViewContainer.stopShimmerAnimation()
                        mShimmerViewContainer.visibility = View.GONE
                        swipeRefresh.isRefreshing = false
                    }
                }
            })
        }
    }
}