package com.decouikit.news.activities

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.decouikit.news.R
import com.decouikit.news.activities.common.BaseActivity
import com.decouikit.news.adapters.ViewAllAdapter
import com.decouikit.news.database.InMemory
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.hideSoftKeyboard
import com.decouikit.news.extensions.openPostActivity
import com.decouikit.news.interfaces.OpenPostListener
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.network.sync.SyncPost
import com.decouikit.news.utils.ActivityUtil
import com.decouikit.news.utils.EndlessRecyclerOnScrollListener
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class SearchActivity : BaseActivity(), View.OnClickListener, OpenPostListener,
    SwipeRefreshLayout.OnRefreshListener, View.OnKeyListener, NestedScrollView.OnScrollChangeListener  {

    private lateinit var layoutManager: LinearLayoutManager
    private var adapter = ViewAllAdapter(arrayListOf(), this)

    private var searchText = ""
    private var page = 0
    private var tagId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        ActivityUtil.setLayoutDirection(this, getLayoutDirection(), R.id.searchParent)

        tagId = getTagId()
        setTagName()

        initLayout()
        initListeners()
    }

    private fun getTagId(): Int? {
        var id: Int? = null
        if (intent.hasExtra(NewsConstants.SEARCH_SLUG)) {
            id = intent.getIntExtra(NewsConstants.SEARCH_SLUG, -1)
            if (id == -1) {
                id = null
            }
        }
        return id
    }

    private fun setTagName() {
        val tag = InMemory.getTagById(tagId ?: -1)
        if (tag != null) {
            tvTagName.visibility = View.VISIBLE
            tvTagName.text = getString(R.string.hash_tag_search, tag.name)
        }
    }

    private fun initLayout() {
        if (Preference(this).isRtlEnabled) {
            ivBack.rotation = 180f
        }
        layoutManager = LinearLayoutManager(this)
        rvSearch.layoutManager = layoutManager
        rvSearch.adapter = adapter
        setEmptyState(false)
        swipeRefresh.isRefreshing = true
        search(searchText)
    }

    private fun initListeners() {
        ivBack.setOnClickListener(this)
        ivSearch.setOnClickListener(this)
        swipeRefresh.setOnRefreshListener(this)
        etSearch.setOnKeyListener(this)
        nestedParent.setOnScrollChangeListener(this)
    }

    override fun onClick(v: View) {
        when (v) {
            ivBack -> {
                onBackPressed()
            }
            ivSearch -> {
                page = 0
                startSearching()
            }
        }
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        // If the event is a key-down event on the "enter" button
        if ((event?.action == KeyEvent.ACTION_DOWN) &&
            (keyCode == KeyEvent.KEYCODE_ENTER)
        ) {
            page = 0
            startSearching()
            return true
        }
        return false
    }

    private fun startSearching() {
        swipeRefresh.isRefreshing = true
        setEmptyState(false)
        hideSoftKeyboard()
        if (page == 0) {
            adapter.removeAllItems()
        }
        search(etSearch.text.toString())
    }

    private fun search(text: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val posts = SyncPost.getPostsSearch(applicationContext, text, tagId, ++page)
            if (posts.isNotEmpty()) {
                adapter.setSearchedData(posts as ArrayList<PostItem>)
            }
            setEmptyState(adapter.itemCount == 0)
            swipeRefresh.isRefreshing = false
        }
    }

    override fun onRefresh() {
        startSearching()
    }

    override fun openPost(view: View, item: PostItem) {
        view.openPostActivity(view.context, item)
    }

    private fun setEmptyState(isHidden: Boolean) {
        if (isHidden) {
            tvTagName.visibility = View.GONE
            empty_container.visibility = View.VISIBLE
            swipeRefresh.visibility = View.GONE
        } else {
            tvTagName.visibility = if (tagId != null) {
                View.VISIBLE
            } else {
                View.GONE
            }
            empty_container.visibility = View.GONE
            swipeRefresh.visibility = View.VISIBLE
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
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    startSearching()
                }
            }
        }
    }
}