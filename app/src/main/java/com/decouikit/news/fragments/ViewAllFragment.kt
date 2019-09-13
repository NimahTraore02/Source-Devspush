package com.decouikit.news.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.decouikit.news.R
import com.decouikit.news.adapters.ViewAllAdapter
import com.decouikit.news.database.Config
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.interfaces.ViewAllFragmentListener
import com.decouikit.news.network.PostsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.utils.EndlessRecyclerOnScrollListener
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.fragment_view_all.view.*
import org.jetbrains.anko.doAsync

class ViewAllFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var itemView: View
    private var categoryId: Int? = 0
    private lateinit var categoryName: String
    private val allMediaList = InMemory.getMediaList()
    private lateinit var adapter: ViewAllAdapter
    private val items = arrayListOf<PostItem>()
    private lateinit var callback: ViewAllFragmentListener
    val postsService = RetrofitClientInstance.retrofitInstance?.create(PostsService::class.java)
    private var page = 0
    private val perPage = 10

    override fun onAttach(context: Context) {
        callback = context as ViewAllFragmentListener
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = arguments?.getInt(NewsConstants.CATEGORY_ID)
        categoryName = arguments?.getString(NewsConstants.CATEGORY_NAME, "").toString()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        itemView = inflater.inflate(R.layout.fragment_view_all, container, false)
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        initListeners()
        getPosts()
    }

    private fun initLayout() {
        adapter = ViewAllAdapter(arrayListOf())
        itemView.rvItems.layoutManager = LinearLayoutManager(itemView.context)
        itemView.rvItems.adapter = adapter
    }

    private fun initListeners() {
        itemView.swipeRefresh.setOnRefreshListener(this)
    }

    override fun onResume() {
        super.onResume()
        callback.viewAllFragmentBehavior()
        loadData()
    }

    override fun onRefresh() {
        adapter.removeAllItems()
        page = 0
        loadData()
    }

    private fun loadData() {
        itemView.swipeRefresh.isRefreshing = true
        getPosts()
    }

    private fun getPosts() {
        doAsync {
            postsService?.getPostsByCategory(categoryId.toString(), ++page, perPage)?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (it.response.body() != null) {
                            val posts = it.response.body() as ArrayList<PostItem>
                            for (postItem in posts) {
                                for (mediaItem in allMediaList) {
                                    if (mediaItem.id == postItem.featured_media) {
                                        postItem.categoryName = categoryName
                                        postItem.source_url = mediaItem.source_url
                                        items.add(postItem)
                                        adapter.setData(items)
                                    }
                                }
                            }
                        }
                        itemView.swipeRefresh.isRefreshing = false
                    }
                }
            })
        }
    }

    companion object {
        fun newInstance(categoryId: Int, categoryName: String): ViewAllFragment {
            val fragment = ViewAllFragment()
            val args = Bundle()
            args.putInt(NewsConstants.CATEGORY_ID, categoryId)
            args.putString(NewsConstants.CATEGORY_NAME, categoryName)
            fragment.arguments = args
            return fragment
        }
    }
}