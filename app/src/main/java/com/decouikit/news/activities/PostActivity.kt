package com.decouikit.news.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.decouikit.news.R
import com.decouikit.news.activities.common.BaseActivity
import com.decouikit.news.adapters.ViewAllAdapter
import com.decouikit.news.database.Config
import com.decouikit.news.database.InMemory
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.*
import com.decouikit.news.interfaces.OpenPostListener
import com.decouikit.news.network.CommentsService
import com.decouikit.news.network.PostsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.CommentItem
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.utils.ActivityUtil
import com.decouikit.news.utils.NewsConstants
import com.decouikit.news.utils.UriChromeClient
import kotlinx.android.synthetic.main.activity_post.*
import org.jetbrains.anko.doAsync
import java.util.*
import com.google.android.material.appbar.AppBarLayout



open class PostActivity : BaseActivity(), View.OnClickListener, OpenPostListener {

    private lateinit var postItem: PostItem

    private lateinit var adapter: ViewAllAdapter
    private var postItems = arrayListOf<PostItem>()
    private val allMediaList = InMemory.getMediaList()

    private val commentsService =
        RetrofitClientInstance.retrofitInstance?.create(CommentsService::class.java)
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        ActivityUtil.setLayoutDirection(this, getLayoutDirection(), R.id.coordinatorParent)
        postItem = loadPostItem()
        initLayout()
        initListeners()
        incrementAdsCounterAndShowAds()
    }

    private fun incrementAdsCounterAndShowAds() {
        prefs.interstitialAdCounter = prefs.interstitialAdCounter + 1
        if (prefs.interstitialAdCounter == Config.promptForInterstitialCounter()) {
            prefs.interstitialAdCounter = 0
            showInterstitialAds()
        }
    }

    private fun loadPostItem(): PostItem {
        return gson.fromJson(intent.getStringExtra(NewsConstants.POST_ITEM), PostItem::class.java)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initLayout() {
        if(Preference(this).isRtlEnabled) {
            ivBack.rotation = 180f
        }
        ivPostBg.load(postItem.source_url)
        if (Preference(this).getBookmarkedNews().contains(postItem)) {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bookmark_red))
        } else {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bookmark))
        }
        tvTag.text = postItem.categoryName
        tvItemTitle.text = postItem.title.rendered
        tvDate.text = Date().getDateFromString(postItem.date)?.getCalendarDate()
        tvComments.visibility = postItem.getCommentVisible()

        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = UriChromeClient(this)


        webView.loadData(
            String.format("%s%s", NewsConstants.HTML_STYLE, postItem.content.rendered),
            NewsConstants.TEXT_HTML,
            NewsConstants.UTF_8
        )
        adapter = ViewAllAdapter(arrayListOf(), this)
        rvRecentNews.layoutManager = LinearLayoutManager(this)
        rvRecentNews.adapter = adapter

        getRelatedNews()
    }

    private fun initListeners() {
        ivBack.setOnClickListener(this)
        ivBookmark.setOnClickListener(this)
        tvComments.setOnClickListener(this)
        ivShare.setOnClickListener(this)
        btnOpenComments.setOnClickListener(this)

        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (Math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                // Collapsed
                tvToolbarTitle.visibility = View.VISIBLE
            } else {
                tvToolbarTitle.visibility = View.GONE
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getNumberOfComments()
    }

    private fun getRelatedNews() {
        doAsync {
            val postsService =
                RetrofitClientInstance.retrofitInstance?.create(PostsService::class.java)
            val categoryId = postItem.categories[0]
            postsService?.getPostsByCategory(
                categoryId.toString(),
                page,
                Config.getNumberOfItemPerPage()
            )?.enqueue(result = {
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
                            btnOpenComments.text =
                                getString(R.string.view_all_comments, result.size)
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
            tvComments -> {
                v.openComments(this, CommentsActivity::class.java, postItem.id)
            }
            btnOpenComments -> {
                v.openComments(this, CommentsActivity::class.java, postItem.id)
            }
            ivShare -> {
                v.share(this, postItem.link)
            }
        }
    }

    override fun openPost(view: View, item: PostItem) {
        view.openPostActivity(view.context, item)
        finish()
    }
}