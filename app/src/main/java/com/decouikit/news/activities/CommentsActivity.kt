package com.decouikit.news.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
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

class CommentsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var adapter: CommentsAdapter
    private var postId: Int = -1
    private val commentsService = RetrofitClientInstance.retrofitInstance?.create(CommentsService::class.java)
    private var page = 1
    private val perPage = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_comments)

        postId = intent.getIntExtra(NewsConstants.POST_ITEM_ID, -1)
        initLayout()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        getComments()
    }

    private fun initLayout() {
        adapter = CommentsAdapter(arrayListOf())
        rvItems.layoutManager = LinearLayoutManager(this)
        rvItems.adapter = adapter
    }

    private fun getComments() {
        doAsync {
            commentsService?.getCommentListPostId(postId, page, perPage)?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (it.response.body() != null) {
                            val comments = it.response.body() as List<CommentItem>
                            adapter.setData(comments)
                        }
                    }
                    is Result.Failure -> {

                    }
                }
            })
        }
    }

    private fun initListeners() {
        ivBack.setOnClickListener(this)
        btnWriteComment.setOnClickListener(this)
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