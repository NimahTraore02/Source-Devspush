package com.decouikit.news.activities

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AbsListView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.activity_all_comments.*
import org.jetbrains.anko.doAsync

class CommentsActivity : BaseActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter: CommentsAdapter
    private var postId: Int = -1
    private val commentsService = RetrofitClientInstance.retrofitInstance?.create(CommentsService::class.java)
    private var page = 0
    private val perPage = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_comments)

        postId = intent.getIntExtra(NewsConstants.POST_ITEM_ID, -1)
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        loadData()
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
                        if (it.response.body() != null) {
                            val comments = it.response.body() as ArrayList<CommentItem>
                            initLayout()
                            if (comments.isNullOrEmpty()) {
                                hideContent(true)
                            } else {
                                hideContent(false)
                                adapter.setData(comments)
                            }
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
        rvItems.layoutManager = LinearLayoutManager(this)
        rvItems.adapter = adapter
    }

    private fun initListeners() {
        ivBack.setOnClickListener(this)
        btnWriteComment.setOnClickListener(this)
        swipeRefresh.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        adapter.removeAllItems()
        page = 0
        loadData()
    }

    private fun hideContent(isListEmpty: Boolean) {
        if(isListEmpty) {
            scrollView.visibility = View.GONE
            emptyCommentContainer.visibility = View.VISIBLE
        } else {
            scrollView.visibility = View.VISIBLE
            emptyCommentContainer.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        when(v) {
            ivBack -> {
                onBackPressed()
            }
            btnWriteComment -> {
                v.openComments(this, PostCommentActivity::class.java, postId)
            }
        }
    }
}