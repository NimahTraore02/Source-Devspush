package com.decouikit.news.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.decouikit.news.R
import com.decouikit.news.activities.common.BaseActivity
import com.decouikit.news.adapters.BaseListAdapter
import com.decouikit.news.adapters.HashTagAdapter
import com.decouikit.news.database.Config
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.*
import com.decouikit.news.interfaces.OnHashTagClickListener
import com.decouikit.news.interfaces.OpenPostListener
import com.decouikit.news.network.CommentsService
import com.decouikit.news.network.PostsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.CommentItem
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.network.dto.Tag
import com.decouikit.news.network.sync.SyncTags
import com.decouikit.news.utils.*
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.util.*
import kotlin.math.abs

class PostActivity : BaseActivity(), View.OnClickListener, OpenPostListener,
    UriChromeClient.FullscreenInterface, OnHashTagClickListener {

    private lateinit var adapter: BaseListAdapter
    private lateinit var hashTagAdapter: HashTagAdapter

    private var isRunning = false

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
        initLayout()
        initValues()
        initListeners()
        incrementAdsCounterAndShowAds()
        showBannerAds()
    }

    override fun getAdsContainer(): ViewGroup? {
        return viewContainerForAds
    }

    private fun initLayout() {
        //Creating list type for recent news
        val adapterType =
            AdapterListTypeUtil.getAdapterTypeFromValue(Preference(this).recentFromPostAdapterStyle)
        adapter = BaseListAdapter(arrayListOf(), adapterType)
        rvRecentNews.layoutManager = GridLayoutManager(this, adapterType.columns)

        adapter.setItemClickListener(this)
        rvRecentNews.adapter = adapter

        setWritingCommentsVisibility(true)

        //Setting tags
        hashTagAdapter = HashTagAdapter(arrayListOf(), this)
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.justifyContent = JustifyContent.FLEX_START
        rvTags.layoutManager = layoutManager
        rvTags.adapter = hashTagAdapter
    }

    private fun incrementAdsCounterAndShowAds() {
        prefs.interstitialAdCounter = prefs.interstitialAdCounter + 1
        if (prefs.interstitialAdCounter == Config.promptForInterstitialCounter()) {
            prefs.interstitialAdCounter = 0
            showInterstitialAds()
        }
    }

    fun setWritingCommentsVisibility(isVisible: Boolean) {
        if (Config.isReadingCommentEnabled()) {
            btnOpenComments.visibility = if (isVisible) View.VISIBLE else View.GONE
        } else {
            btnOpenComments.visibility = View.GONE
        }
    }

    private fun loadTag(post: PostItem) {
        runOnUiThread {
            post.tags.forEach { tagId ->
                GlobalScope.launch(Dispatchers.Main) {
                    val tag = tagsService.getTagById(tagId, context = applicationContext)
                    tag?.let { tagList.add(it) }
                    hashTagAdapter.setData(tagList)
                }
            }
        }
    }

    private fun loadPostItem(): PostItem {
        return gson.fromJson(intent.getStringExtra(NewsConstants.POST_ITEM), PostItem::class.java)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initValues() {
        val isRTL = Preference(this).isRtlEnabled
        if (isRTL) {
            ivBack.rotation = 180f
        }
        ImageLoadingUtil.load(this, postItem, ivPostBg)
        ivBookmark.setBookmarkIcon(postItem)
        tvTag.loadCategoryName(postItem)
        tvItemTitle.setHtml(postItem.title.rendered)
        tvDate.text = Date().getDateFromString(postItem.date)?.getCalendarDate()
        tvComments.visibility = postItem.getCommentVisible()
        setWritingCommentsVisibility(postItem.getCommentVisible() == View.VISIBLE)

        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = UriChromeClient(this, this)

        val style = if (Preference(this).isThemeLight()) {
            NewsConstants.HTML_STYLE
        } else {
            NewsConstants.HTML_STYLE_DARK
        }

        val rtlText = if (Preference(this).isRtlEnabled) "dir=\"rtl\" lang=\"\"" else ""

        webView.loadDataWithBaseURL(
            null,
            "<HTML $rtlText>" + String.format("%s%s", style, postItem.content.rendered) + "</HTML>",
            NewsConstants.TEXT_HTML,
            NewsConstants.UTF_8,
            null
        )
        tvComments.text = resources.getQuantityString(
            R.plurals.numberOfComments, 0, 0
        )
        btnOpenComments.text =
            getString(R.string.view_all_comments, 0)

        getRelatedNews()
        loadTag(postItem)
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

    override fun onStart() {
        super.onStart()
        isRunning = true
    }

    override fun onStop() {
        super.onStop()
        isRunning = false
    }

    override fun isRunning(): Boolean {
        return isRunning
    }

    private fun getRelatedNews() {
        GlobalScope.launch(Dispatchers.Main) {
            val response = postsService?.getPostsList(
                postItem.categories[0].categoryToString(), null,
                page, Config.getNumberOfItemPerPage()
            )?.awaitResponse()
            if (response?.isSuccessful == true) {
                if (!response.body().isNullOrEmpty()) {
                    val items = response.body() as ArrayList<PostItem>
                    for (item in items) {
                        item.categoryName = postItem.categoryName
                        postItems.add(item)
                    }
                    postItems.sortBy { it.modified_gmt }
                    adapter.setData(postItems)
                    adapter.removeItem(postItem)
                }
            }

        }
    }

    private fun getNumberOfComments() {
        GlobalScope.launch(Dispatchers.Main) {
            val response = commentsService?.getCommentListPostId(postItem.id)?.awaitResponse()
            var commentCounter = 0
            if (response?.isSuccessful == true) {
                if (!response.body().isNullOrEmpty()) {
                    val result = response.body() as List<CommentItem>
                    commentCounter = result.size
                }
            }
            tvComments.text = resources.getQuantityString(
                R.plurals.numberOfComments,
                commentCounter,
                commentCounter
            )
            btnOpenComments.text = getString(R.string.view_all_comments, commentCounter)
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
                if (Config.isReadingCommentEnabled()) {
                    v.openComments(this, CommentsActivity::class.java, postItem.id)
                }
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
        setWritingCommentsVisibility(false)
        recentTitle.visibility = View.GONE
        cardParent.visibility = View.GONE
    }

    override fun hideFullscreen() {
        rlRecentNews.visibility = View.VISIBLE
        appbar.visibility = View.VISIBLE
        setWritingCommentsVisibility(true)
        recentTitle.visibility = View.VISIBLE
        cardParent.visibility = View.VISIBLE
    }

    override fun openPost(view: View, item: PostItem) {
        view.openPostActivity(view.context, item)
        finish()
    }
}