package com.decouikit.news.activities

import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.decouikit.news.R
import com.decouikit.news.activities.common.BaseActivity
import com.decouikit.news.adapters.CommentsAdapter
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.openComments
import com.decouikit.news.network.CommentsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.CommentItem
import com.decouikit.news.utils.ActivityUtil
import com.decouikit.news.utils.EndlessRecyclerOnScrollListener
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.activity_all_comments.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class CommentsActivity : BaseActivity(), View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener, NestedScrollView.OnScrollChangeListener {

    private lateinit var adapter: CommentsAdapter
    private var postId: Int = -1
    private val commentsService by lazy {
        RetrofitClientInstance.getRetrofitInstance(this)?.create(CommentsService::class.java)
    }
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
        refreshContent()
    }

    private fun getComments() {
        GlobalScope.launch(Dispatchers.Main) {
            val response =
                commentsService?.getCommentListPostId(postId, ++page, perPage)?.awaitResponse()
            if (response?.isSuccessful == true) {
                if (!response.body().isNullOrEmpty()) {
                    val comments = response.body() as ArrayList<CommentItem>
                    hideContent(false)
                    adapter.setData(comments)
                } else {
                    hideContent(adapter.itemCount == 0)
                }
            }else {
                hideContent(adapter.itemCount == 0)
            }
            mShimmerViewContainer.stopShimmer()
            mShimmerViewContainer.visibility = View.GONE
            swipeRefresh.isRefreshing = false
        }
    }

    private fun initLayout() {
        if (Preference(this).isRtlEnabled) {
            ivBack.rotation = 180f
        }
        adapter = CommentsAdapter(arrayListOf())
        linearLayoutManager = LinearLayoutManager(this)
        rvItems.layoutManager = linearLayoutManager
        rvItems.adapter = adapter
    }

    private fun initListeners() {
        ivBack.setOnClickListener(this)
        btnWriteComment.setOnClickListener(this)
        swipeRefresh.setOnRefreshListener(this)
        nestedParent.setOnScrollChangeListener(this)
    }

    override fun onRefresh() {
        swipeRefresh.isRefreshing = true
        refreshContent()
    }

    private fun hideContent(isListEmpty: Boolean) {
        if (isListEmpty) {
            nestedParent.visibility = View.GONE
            tvTitle.visibility = View.GONE
            emptyCommentContainer.visibility = View.VISIBLE
        } else {
            nestedParent.visibility = View.VISIBLE
            tvTitle.visibility = View.VISIBLE
            emptyCommentContainer.visibility = View.GONE
        }
    }

    private fun refreshContent() {
        hideAllOnRefresh()
        mShimmerViewContainer.visibility = View.VISIBLE
        mShimmerViewContainer.startShimmer()
        adapter.removeAllItems()
        page = 0
        getComments()
    }

    private fun hideAllOnRefresh() {
        nestedParent.visibility = View.GONE
        tvTitle.visibility = View.GONE
        emptyCommentContainer.visibility = View.GONE
    }

    override fun onClick(v: View) {
        when (v) {
            ivBack -> onBackPressed()
            btnWriteComment -> v.openComments(this, PostCommentActivity::class.java, postId)
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
                val visibleItemCount = linearLayoutManager.childCount
                val totalItemCount = linearLayoutManager.itemCount
                val pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    swipeRefresh.isRefreshing = true
                    getComments()
                }
            }
        }
    }
}