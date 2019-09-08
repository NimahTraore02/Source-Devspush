package com.decouikit.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.decouikit.news.R
import com.decouikit.news.adapters.FeaturedNewsAdapter
import com.decouikit.news.adapters.RecentNewsAdapter
import com.decouikit.news.database.InMemory
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.extensions.replaceFragmentWithBackStack
import com.decouikit.news.interfaces.AddBookmarkListener
import com.decouikit.news.network.PostsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.MediaItem
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.fragment_filter.view.*
import org.jetbrains.anko.doAsync

class FilterFragment : Fragment(), View.OnClickListener, AddBookmarkListener {

    private lateinit var itemView: View

    private var categoryId: Int? = null
    private lateinit var categoryName: String

    private lateinit var allMediaList: List<MediaItem>
    private lateinit var allPostList: List<PostItem>

    private var featuredPostItems = arrayListOf<PostItem>()
    private lateinit var featuredAdapter: FeaturedNewsAdapter

    private var recentPostItems = arrayListOf<PostItem>()
    private lateinit var recentAdapter: RecentNewsAdapter

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
        itemView = inflater.inflate(R.layout.fragment_filter, container, false)
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postsService = RetrofitClientInstance.retrofitInstance?.create(PostsService::class.java)
        doAsync {
            postsService?.getPostsByCategory(categoryId.toString(), 1, 10)?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (it.response.body() != null) {
                            allMediaList = InMemory.getMediaList()
                            allPostList = it.response.body() as ArrayList<PostItem>
                            initFeaturedNews()
                            initRecentNews()
                        }
                    }
                }
            })
        }
        initListeners()
    }

    private fun initFeaturedNews() {
        var postCounter = 0
        for (postItem in allPostList) {
            postItem.categoryName = categoryName
            for (mediaItem in allMediaList) {
                if (mediaItem.id == postItem.featured_media) {
                    postItem.source_url = mediaItem.source_url
                }
            }
            featuredPostItems.add(postItem)
            postCounter++
            if (postCounter == 3) {
                break
            }
        }
        featuredAdapter = FeaturedNewsAdapter(featuredPostItems, itemView.context, this)

        itemView.viewPager.adapter = featuredAdapter
        itemView.viewPager.offscreenPageLimit = 3
        itemView.viewPager.setCurrentItem(
            1,
            true
        )
    }

    private fun initRecentNews() {
        for (postItem in allPostList.subList(3, allPostList.size)) {
            postItem.categoryName = categoryName
            for (mediaItem in allMediaList) {
                if (mediaItem.id == postItem.featured_media) {
                    postItem.source_url = mediaItem.source_url
                }
            }
            recentPostItems.add(postItem)
        }

        recentAdapter = RecentNewsAdapter(recentPostItems)

        itemView.rvRecentNews.layoutManager = GridLayoutManager(itemView.context, 2)
        itemView.rvRecentNews.adapter = recentAdapter
        itemView.rvRecentNews.setHasFixedSize(true)
    }

    private fun initListeners() {
        itemView.tvFeaturedNewsViewAll.setOnClickListener(this)
        itemView.tvRecentNewsViewAll.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v) {
            itemView.tvFeaturedNewsViewAll -> {
                categoryId?.let { ViewAllFragment.newInstance(it, categoryName) }?.let {
                    replaceFragmentWithBackStack(
                        it,
                        R.id.navigation_container
                    )
                }
            }
            itemView.tvRecentNewsViewAll -> {
                categoryId?.let { ViewAllFragment.newInstance(it, categoryName) }?.let {
                    replaceFragmentWithBackStack(
                        it,
                        R.id.navigation_container
                    )
                }
            }
        }
    }

    override fun addBookmark(items: List<PostItem>) {
        val postItem = Preference(itemView.context).getBookmarkByPostId(items[itemView.viewPager.currentItem].id)
                if(postItem == null) {
                    Preference(itemView.context).addBookmark(items[itemView.viewPager.currentItem])
                } else {
                    Preference(itemView.context).removeBookmark(items[itemView.viewPager.currentItem])
                }
        //TODO dodati promenu bookmark ikonice
    }

    companion object {
        fun newInstance(categoryId: Int, categoryName: String): FilterFragment {
            val fragment = FilterFragment()
            val args = Bundle()
            args.putInt(NewsConstants.CATEGORY_ID, categoryId)
            args.putString(NewsConstants.CATEGORY_NAME, categoryName)
            fragment.arguments = args
            return fragment
        }
    }
}