package com.decouikit.news.activities

import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.decouikit.news.R
import com.decouikit.news.activities.common.BaseActivity
import com.decouikit.news.adapters.BaseListAdapter
import com.decouikit.news.adapters.common.CommonListAdapterType
import com.decouikit.news.database.Config
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.categoryToString
import com.decouikit.news.extensions.openPostActivity
import com.decouikit.news.interfaces.OpenPostListener
import com.decouikit.news.network.PostsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.CategoryType
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.utils.ActivityUtil
import com.decouikit.news.utils.AdapterListTypeUtil
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.activity_view_all.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class ViewAllActivity : BaseActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
    OpenPostListener, NestedScrollView.OnScrollChangeListener {

    private var categoryId: Int? = 0
    private var categoryName: String? = ""
    private var categoryType: CategoryType = CategoryType.ALL
    private var isDataLoading = false
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: BaseListAdapter
    private val items = arrayListOf<PostItem>()
    private val postService by lazy {
        RetrofitClientInstance.getRetrofitInstance(this)?.create(PostsService::class.java)
    }
    private var page = 0
    private val perPage = Config.getNumberOfItemPerPage()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all)

        ActivityUtil.setLayoutDirection(this, getLayoutDirection(), R.id.viewAllParent)
        categoryId = intent.getIntExtra(NewsConstants.CATEGORY_ID, -1)
        categoryName = intent.getStringExtra(NewsConstants.CATEGORY_NAME)
        categoryType = CategoryType.getType(
            intent.getIntExtra(NewsConstants.CATEGORY_FEATURES, CategoryType.ALL.ordinal)
        )
        initLayout()
        initListeners()
        initValues()
    }

    private fun initValues() {
        when (categoryType) {
            CategoryType.ALL -> {
                tvTitle.setText(R.string.recent_news)
            }
            CategoryType.FEATURED -> {
                tvTitle.setText(R.string.featured_news)
            }
            CategoryType.RECENT -> {
                tvTitle.setText(R.string.recent_news)
            }
        }

        refreshContent()
    }

    private fun loadData() {
        swipeRefresh.isRefreshing = true
        when (categoryType) {
            CategoryType.ALL -> {
                getAllPosts()
            }
            CategoryType.FEATURED -> {
                getPostsWithSticky(true)
            }
            CategoryType.RECENT -> {
                getPostsWithSticky(false)
            }
        }
    }

    private fun initLayout() {
        if (Preference(this).isRtlEnabled) {
            ivBack.rotation = 180f
        }

        //Creating list type
        val adapterType =
            AdapterListTypeUtil.getAdapterTypeFromValue(Preference(this).viewAllAdapterStyle)
        adapter = BaseListAdapter(arrayListOf(), adapterType)
        layoutManager = GridLayoutManager(this, adapterType.columns)

        adapter.setItemClickListener(this)
        rvItems.layoutManager = layoutManager
        rvItems.adapter = adapter

        setShimmerType(adapterType)
    }

    private fun setShimmerType(adapterType: CommonListAdapterType) {
        when(adapterType) {
            CommonListAdapterType.ADAPTER_VERSION_1 -> {
                recentShimmer1.visibility = View.VISIBLE
                recentShimmer2.visibility = View.GONE
                recentShimmer3.visibility = View.GONE
            }
            CommonListAdapterType.ADAPTER_VERSION_2 -> {
                recentShimmer1.visibility = View.GONE
                recentShimmer2.visibility = View.VISIBLE
                recentShimmer3.visibility = View.GONE

            }
            CommonListAdapterType.ADAPTER_VERSION_3 -> {
                recentShimmer1.visibility = View.GONE
                recentShimmer2.visibility = View.GONE
                recentShimmer3.visibility = View.VISIBLE

            }
        }
    }
    private fun initListeners() {
        ivBack.setOnClickListener(this)
        swipeRefresh.setOnRefreshListener(this)
        nestedParent.setOnScrollChangeListener(this)
    }

    override fun onClick(v: View) {
        when (v) {
            ivBack -> onBackPressed()
        }
    }

    override fun openPost(view: View, item: PostItem) {
        view.openPostActivity(view.context, item)
    }

    override fun onRefresh() {
        swipeRefresh.isRefreshing = true
        refreshContent()
    }

    private fun refreshContent() {
        hideAllOnRefresh()
        mShimmerViewContainer.visibility = View.VISIBLE
        mShimmerViewContainer.startShimmer()
        items.removeAll { true }
        adapter.removeAllItems()
        page = 0
        loadData()
    }

    private fun getPostsWithSticky(sticky: Boolean) {

        isDataLoading = true

        GlobalScope.launch(Dispatchers.Main) {
            val response = postService?.getPostsList(
                categoryId?.categoryToString(),
                sticky,
                ++page,
                perPage
            )?.awaitResponse()

            isDataLoading = false

            if (response?.isSuccessful == true) {
                if (response.body().isNullOrEmpty()) {
                    hideContent(adapter.itemCount == 0)
                } else {
                    val posts = response.body() as ArrayList<PostItem>
                    for (postItem in posts) {
                        //loop for getting image urls, post name is fixed from intent
                        postItem.categoryName = categoryName.toString()
                        items.add(postItem)
                    }
                    if (items.isEmpty()) {
                        hideContent(true)
                    } else {
                        hideContent(false)
                        adapter.setData(items)
                    }
                }
            } else {
                hideContent(adapter.itemCount == 0)
            }
            mShimmerViewContainer.startShimmer()
            mShimmerViewContainer.visibility = View.GONE
            swipeRefresh.isRefreshing = false
        }
    }

    private fun getAllPosts() {
        GlobalScope.launch(Dispatchers.Main) {
            isDataLoading = true
            val response = postService?.getPostsList(
                categoryId?.categoryToString(),
                null,
                ++page,
                perPage
            )?.awaitResponse()

            if (response?.isSuccessful == true) {
                if (response.body().isNullOrEmpty()) {
                    hideContent(adapter.itemCount == 0)
                } else {
                    val posts = response.body() as ArrayList<PostItem>
                    for (postItem in posts) {
                        //loop for getting image urls, post name is fixed from intent
                        postItem.categoryName = categoryName.toString()
                        items.add(postItem)
                    }
                    if (items.isEmpty()) {
                        hideContent(true)
                    } else {
                        hideContent(false)
                        adapter.setData(items)
                    }
                }
            } else {
                hideContent(adapter.itemCount == 0)
            }
            mShimmerViewContainer.startShimmer()
            mShimmerViewContainer.visibility = View.GONE
            swipeRefresh.isRefreshing = false
        }
    }

    private fun hideContent(isListEmpty: Boolean) {
        if (isListEmpty) {
            tvTitle.visibility = View.GONE
            rvItems.visibility = View.GONE
            emptyPostsContainer.visibility = View.VISIBLE
        } else {
            tvTitle.visibility = View.VISIBLE
            rvItems.visibility = View.VISIBLE
            emptyPostsContainer.visibility = View.GONE
        }
    }

    private fun hideAllOnRefresh() {
        tvTitle.visibility = View.GONE
        rvItems.visibility = View.GONE
        emptyPostsContainer.visibility = View.GONE
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
                    swipeRefresh.isRefreshing = true
                    loadData()
                }
            }
        }
    }
}
