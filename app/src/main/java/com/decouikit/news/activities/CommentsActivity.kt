package com.decouikit.news.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.decouikit.news.R
import com.decouikit.news.activities.common.BaseActivity
import com.decouikit.news.adapters.CommentsAdapter
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.extensions.openComments
import com.decouikit.news.network.CommentsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.CommentItem
import com.decouikit.news.utils.ActivityUtil
import com.decouikit.news.utils.EndlessRecyclerOnScrollListener
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.activity_all_comments.*
import org.jetbrains.anko.doAsync

class CommentsActivity : BaseActivity(), View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter: CommentsAdapter
    private var postId: Int = -1
    private val commentsService =
        RetrofitClientInstance.retrofitInstance?.create(CommentsService::class.java)
    private var page = 0
    private val perPage = 10
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_comments)
        ActivityUtil.setLayoutDirection(this, getLayoutDirection(), R.id.commentsParent)

        postId = intent.getIntExtra(NewsConstants.POST_ITEM_ID, -1)
        initLayout()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        rvItems.addOnScrollListener(object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore() {
                loadData()
            }
        })
        refreshContent()
    }

    private fun loadData() {
        swipeRefresh.isRefreshing = true
        getComments()
    }

    private fun getComments() {
        doAsync {
            commentsService?.getCommentListPostId(postId, ++page, perPage)?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (it.response.body().isNullOrEmpty() && adapter.itemCount == 0) {
                            hideContent(true)
                        } else {
                            val comments = it.response.body() as ArrayList<CommentItem>
                            hideContent(false)
                            adapter.setData(comments)
                        }
                    }
                    is Result.Failure -> {
                        hideContent(true)
                    }
                }
                swipeRefresh.isRefreshing = false
            })
        }
    }

    private fun initLayout() {
        adapter = CommentsAdapter(arrayListOf())
        linearLayoutManager = LinearLayoutManager(this)
        rvItems.layoutManager = linearLayoutManager
        rvItems.adapter = adapter
    }

    private fun initListeners() {
        ivBack.setOnClickListener(this)
        btnWriteComment.setOnClickListener(this)
        swipeRefresh.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        refreshContent()
    }

    private fun hideContent(isListEmpty: Boolean) {
        if (isListEmpty) {
            rvItems.visibility = View.GONE
            tvTitle.visibility = View.GONE
            emptyCommentContainer.visibility = View.VISIBLE
        } else {
            rvItems.visibility = View.VISIBLE
            tvTitle.visibility = View.VISIBLE
            emptyCommentContainer.visibility = View.GONE
        }
    }

    private fun refreshContent() {
        adapter.removeAllItems()
        page = 0
        loadData()
    }

    override fun onClick(v: View) {
        when (v) {
            ivBack -> {
                onBackPressed()
            }
            btnWriteComment -> {
                v.openComments(this, PostCommentActivity::class.java, postId)
            }
        }
    }
}