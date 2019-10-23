package com.decouikit.news.activities

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.decouikit.news.R
import com.decouikit.news.activities.common.BaseActivity
import com.decouikit.news.adapters.ViewAllAdapter
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.extensions.openPostActivity
import com.decouikit.news.interfaces.OpenPostListener
import com.decouikit.news.network.PostsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.utils.ActivityUtil
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.doAsync
import com.decouikit.news.extensions.hideSoftKeyboard


class SearchActivity : BaseActivity(), View.OnClickListener, OpenPostListener, SwipeRefreshLayout.OnRefreshListener, View.OnKeyListener {

    private var adapter = ViewAllAdapter(arrayListOf(), this)

    private var searchText = ""

    private val postService by lazy {
        RetrofitClientInstance.getRetrofitInstance(this)?.create(PostsService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        ActivityUtil.setLayoutDirection(this, getLayoutDirection(), R.id.searchParent)

        initLayout()
        initListeners()
    }

    private fun initLayout() {
        rvSearch.layoutManager = LinearLayoutManager(this)
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
    }

    override fun onClick(v: View) {
        when (v) {
            ivBack -> {
                onBackPressed()
            }
            ivSearch -> {
                searchText = etSearch.text.toString()
                startSearching()
            }
        }
    }
    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        // If the event is a key-down event on the "enter" button
        if ((event?.action == KeyEvent.ACTION_DOWN) &&
            (keyCode == KeyEvent.KEYCODE_ENTER)) {
            startSearching()
            return true
        }
        return false
    }

    private fun startSearching() {
        swipeRefresh.isRefreshing = true
        setEmptyState(false)
        hideSoftKeyboard()
        adapter.removeAllItems()
        search(searchText)
    }
    private fun search(text: String) {
        doAsync {
            postService?.getPostsSearch(text)?.enqueue {
                when (it) {
                    is Result.Success -> {
                        if (it.response.body().isNullOrEmpty() && adapter.itemCount == 0) {
                            setEmptyState(true)
                        } else {
                            val posts = it.response.body() as ArrayList<PostItem>
                            adapter.setData(posts)
                        }
                    }
                    is Result.Failure -> {
                        setEmptyState(true)
                    }
                }
                swipeRefresh.isRefreshing = false
            }
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
            empty_container.visibility = View.VISIBLE
            swipeRefresh.visibility = View.GONE
        } else {
            empty_container.visibility = View.GONE
            swipeRefresh.visibility = View.VISIBLE
        }
    }
}