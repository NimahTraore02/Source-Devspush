package com.decouikit.news.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.decouikit.news.R
import com.decouikit.news.activities.common.BaseActivity
import com.decouikit.news.adapters.ViewAllAdapter
import com.decouikit.news.database.InMemory
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.*
import com.decouikit.news.network.CommentsService
import com.decouikit.news.network.PostsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.CommentItem
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.utils.NewsConstants
import com.decouikit.news.utils.UriChromeClient
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_post.*
import org.jetbrains.anko.doAsync
import java.util.*
import kotlin.collections.ArrayList


class PostActivity: BaseActivity(), View.OnClickListener {

    private lateinit var postItem: PostItem
    val gson by lazy {
        Gson()
    }
    private lateinit var adapter: ViewAllAdapter
    private var postItems = arrayListOf<PostItem>()
    private val allMediaList = InMemory.getMediaList()

    private val commentsService = RetrofitClientInstance.retrofitInstance?.create(CommentsService::class.java)
    private var page = 1
    private var perPage = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        postItem = gson.fromJson(intent.getStringExtra(NewsConstants.POST_ITEM), PostItem::class.java)
        initLayout()
        initListeners()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initLayout() {
        ivPostBg.load(postItem.source_url)
        if (Preference(this).getBookmarkedNews().contains(postItem)){
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bookmark_red))
        } else {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bookmark))
        }
        tvTag.text = postItem.categoryName
        tvItemTitle.text = postItem.title.rendered
        tvDate.text = Date().getDateFromString(postItem.date)?.getCalendarDate()
        tvComments.visibility = if (postItem.comment_status == NewsConstants.OPEN_COMMENT_STATUS) {
            View.VISIBLE
        } else {
            View.GONE
        }

        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = UriChromeClient(this)
        webView.loadData("<style>img{display: inline;height: auto;max-width: 100%;} " +
                "iframe{display: block;max-width:100%;margin-top:10px;margin-bottom:10px;}</style>"
                + postItem.content.rendered,
            "text/html", "UTF-8")

        adapter = ViewAllAdapter(arrayListOf())
        rvRecentNews.layoutManager = LinearLayoutManager(this)
        rvRecentNews.adapter = adapter

        getRelatedNews()
    }

    private fun initListeners() {
        ivBack.setOnClickListener(this)
        ivBookmark.setOnClickListener(this)
        btnOpenComments.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        getNumberOfComments()
    }

    private fun getRelatedNews() {
        doAsync {
            val postsService = RetrofitClientInstance.retrofitInstance?.create(PostsService::class.java)
            val categoryId = postItem.categories[0]
            postsService?.getPostsByCategory(categoryId.toString(), page, perPage)?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (it.response.body() != null) {
                            val items = it.response.body() as ArrayList<PostItem>
                            for (item in items) {
                                for (mediaItem in allMediaList) {
                                    if (mediaItem.id == item.featured_media) {
                                        item.source_url = mediaItem.source_url
                                        item.categoryName = postItem.categoryName
                                        postItems.add(item)
                                    }
                                }
                            }
                            adapter.setData(postItems)
                            adapter.removeItem(postItem)
                        }
                    }
                }
            })
        }
    }

    private fun getNumberOfComments() {
        doAsync {
            commentsService?.getCommentListPostId(postItem.id)?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (it.response.body() != null) {
                            val result = it.response.body() as List<CommentItem>
                            tvComments.text = if (result.size == 1) {
                                getString(R.string.comment)
                            } else {
                                getString(R.string.comments, result.size)
                            }
                            btnOpenComments.text = getString(R.string.view_all_comments, result.size)
                        }
                    }
                    is Result.Failure -> {
                        Log.e("TEST", "Failure")
                    }
                }
            })
        }
    }

    override fun onClick(v: View) {
        when (v) {
            ivBack -> {
                onBackPressed()
            }
            ivBookmark -> {
                v.bookmark(this, postItem, ivBookmark)
            }
            btnOpenComments -> {
                v.openComments(this, CommentsActivity::class.java ,postItem.id)
            }
        }
    }
}