package com.decouikit.news.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.decouikit.news.R
import com.decouikit.news.adapters.ViewAllAdapter
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.interfaces.ViewAllFragmentListener
import com.decouikit.news.network.PostsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.fragment_view_all.view.*
import org.jetbrains.anko.doAsync

class ViewAllFragment : Fragment() {

    private lateinit var itemView: View

    private var categoryId: Int? = 0
    private lateinit var categoryName: String

    private val allMediaList = InMemory.getMediaList()

    private lateinit var adapter: ViewAllAdapter
    private val items = arrayListOf<PostItem>()

    private lateinit var callback: ViewAllFragmentListener

    override fun onAttach(context: Context?) {
        callback = context as ViewAllFragmentListener
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = arguments?.getInt(NewsConstants.CATEGORY_ID)
        categoryName = arguments?.getString(NewsConstants.CATEGORY_NAME, "").toString()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        itemView = inflater.inflate(R.layout.fragment_view_all, container, false)
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLayout()
        val postsService = RetrofitClientInstance.retrofitInstance?.create(PostsService::class.java)
        doAsync {
            postsService?.getPostsByCategory(categoryId.toString(), 1, 10)?.enqueue(result = {
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
                    }
                }
            })
        }
    }

    private fun initLayout() {
        adapter = ViewAllAdapter(arrayListOf())
        itemView.rvItems.layoutManager = LinearLayoutManager(itemView.context)
        itemView.rvItems.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        callback.viewAllFragmentBehavior()
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