package com.decouikit.news.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.decouikit.news.R
import com.decouikit.news.activities.common.BaseActivity
import com.decouikit.news.adapters.HashTagAdapter
import com.decouikit.news.adapters.ViewAllAdapter
import com.decouikit.news.database.Config
import com.decouikit.news.database.InMemory
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.*
import com.decouikit.news.interfaces.OnHashTagClickListener
import com.decouikit.news.interfaces.OpenPostListener
import com.decouikit.news.interfaces.ResultListener
import com.decouikit.news.network.CommentsService
import com.decouikit.news.network.PostsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.CommentItem
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.network.dto.Tag
import com.decouikit.news.network.sync.SyncTags
import com.decouikit.news.utils.ActivityUtil
import com.decouikit.news.utils.NewsConstants
import com.decouikit.news.utils.UriChromeClient
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_post.*
import org.jetbrains.anko.doAsync
import java.util.*
import kotlin.math.abs

open class PostActivity : BaseActivity(), View.OnClickListener, OpenPostListener,
    UriChromeClient.FullscreenInterface, OnHashTagClickListener {

    private lateinit var adapter: ViewAllAdapter

    private val postsService by lazy {
        RetrofitClientInstance.getRetrofitInstance(context = applicationContext)
            ?.create(PostsService::class.java)
    }

    private val commentsService by lazy {
        RetrofitClientInstance.getRetrofitInstance(this)?.create(CommentsService::class.java)
    }

    private val tagsService by lazy {
        SyncTags
    }

    private lateinit var postItem: PostItem
    private var page = 1
    private var postItems = arrayListOf<PostItem>()
    private var tagList = arrayListOf<Tag>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        ActivityUtil.setLayoutDirection(this, getLayoutDirection(), R.id.coordinatorParent)
        postItem = loadPostItem()
        loadTag(postItem)
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

    private fun loadTag(post: PostItem) {
        post.tags.forEach { tagId ->
            tagsService.getTagById(tagId, this, object : ResultListener<Tag> {
                override fun onResult(value: Tag?) {
                    value?.let { tagList.add(it) }
                }
            })
        }
    }

    private fun loadPostItem(): PostItem {
        return gson.fromJson(intent.getStringExtra(NewsConstants.POST_ITEM), PostItem::class.java)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initLayout() {
        if (Preference(this).isRtlEnabled) {
            ivBack.rotation = 180f
        }
        ivPostBg.load(postItem)
        ivBookmark.setBookmarkIcon(postItem)
        tvTag.text = postItem.categoryName
        tvItemTitle.setHtml(postItem.title.rendered)
        tvDate.text = Date().getDateFromString(postItem.date)?.getCalendarDate()
        tvComments.visibility = postItem.getCommentVisible()
        btnOpenComments.visibility = postItem.getCommentVisible()

        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = UriChromeClient(this, this)

        val style = if (Preference(this).isThemeLight()) {
            NewsConstants.HTML_STYLE
        } else {
            NewsConstants.HTML_STYLE_DARK
        }

        webView.loadDataWithBaseURL(
            null,
            "<HTML>" + String.format("%s%s", style, postItem.content.rendered) + "</HTML>",
            NewsConstants.TEXT_HTML,
            NewsConstants.UTF_8,
            null
        )
        tvComments.text = resources.getQuantityString(
            R.plurals.numberOfComments, 0, 0
        )
        btnOpenComments.text =
            getString(R.string.view_all_comments, 0)

        adapter = ViewAllAdapter(arrayListOf(), this)
        rvRecentNews.layoutManager = LinearLayoutManager(this)
        rvRecentNews.adapter = adapter

        getRelatedNews()

        //Setting tags
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.justifyContent = JustifyContent.FLEX_START
        rvTags.layoutManager = layoutManager
        rvTags.adapter = HashTagAdapter(tagList, this)
    }

    private fun initListeners() {
        ivBack.setOnClickListener(this)
        ivBookmark.setOnClickListener(this)
        tvComments.setOnClickListener(this)
        ivShare.setOnClickListener(this)
        btnOpenComments.setOnClickListener(this)

        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            tvToolbarTitle.visibility =
                if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        })
    }

    override fun onResume() {
        super.onResume()
        getNumberOfComments()
    }

    private fun getRelatedNews() {
        doAsync {
            postsService?.getPostsList(
                postItem.categories[0].categoryToString(),
                null,
                page,
                Config.getNumberOfItemPerPage()
            )?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (!it.response.body().isNullOrEmpty()) {
                            val items = it.response.body() as ArrayList<PostItem>
                            for (item in items) {
                                item.categoryName = postItem.categoryName
                                postItems.add(item)
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
                        if (!it.response.body().isNullOrEmpty()) {
                            val result = it.response.body() as List<CommentItem>
                            tvComments.text = resources.getQuantityString(
                                R.plurals.numberOfComments, result.size, result.size
                            )
                            btnOpenComments.text =
                                getString(R.string.view_all_comments, result.size)
                        } else {
                            tvComments.text = resources.getQuantityString(
                                R.plurals.numberOfComments, 0, 0
                            )
                            btnOpenComments.text =
                                getString(R.string.view_all_comments, 0)
                        }
                    }
                    is Result.Failure -> {
                        tvComments.text = resources.getQuantityString(
                            R.plurals.numberOfComments, 0, 0
                        )
                        btnOpenComments.text =
                            getString(R.string.view_all_comments, 0)
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
                v.share(this, postItem)
            }
        }
    }

    override fun onHashtagClick(tag: Tag) {
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra(NewsConstants.SEARCH_SLUG, tag.id)
        startActivity(intent)
    }

    override fun showFullscreen() {
        rlRecentNews.visibility = View.GONE
        appbar.visibility = View.GONE
        btnOpenComments.visibility = View.GONE
        recentTitle.visibility = View.GONE
        cardParent.visibility = View.GONE
    }

    override fun hideFullscreen() {
        rlRecentNews.visibility = View.VISIBLE
        appbar.visibility = View.VISIBLE
        btnOpenComments.visibility = View.VISIBLE
        recentTitle.visibility = View.VISIBLE
        cardParent.visibility = View.VISIBLE
    }

    override fun openPost(view: View, item: PostItem) {
        view.openPostActivity(view.context, item)
        finish()
    }
}