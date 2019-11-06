package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.Config
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.interfaces.ResultListener
import com.decouikit.news.network.PostsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.PostItem
import org.jetbrains.anko.doAsync

object SyncPost {
    fun getPostById(context: Context, postId: String, listener: ResultListener<PostItem>?) {
        doAsync {
            val service =
                RetrofitClientInstance.getRetrofitInstance(context)
                    ?.create(PostsService::class.java)
            service?.getPostsById(postId)?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        try {
                            val postItem: PostItem = it.response.body() as PostItem
                            for (category in InMemory.getCategoryList(context)) {
                                if (postItem.categories.contains(category.id)) {
                                    postItem.categoryName = category.name
                                }
                            }
                            listener?.onResult(postItem)
                        } catch (e: Exception) {
                            listener?.onResult(null)
                        }
                    }
                    is Result.Failure -> {
                        listener?.onResult(null)
                    }
                }
            })
        }
    }

    fun getPostsList(
        context: Context,
        categoryId: String,
        sticky: Boolean?,
        page: Int = 1,
        perPage: Int = Config.getNumberOfItemPerPage(),
        listener: ResultListener<List<PostItem>>?
    ) {
        doAsync {
            val service =
                RetrofitClientInstance.getRetrofitInstance(context)
                    ?.create(PostsService::class.java)
            service?.getPostsList(categoryId, sticky, page, perPage)?.enqueue(result = {
                val items: ArrayList<PostItem> = ArrayList()

                when (it) {
                    is Result.Success -> {
                        try {
                            if (!it.response.body().isNullOrEmpty()) {
                                val items = it.response.body() as ArrayList<PostItem>
                                items.sortedBy { it.modified_gmt }
                            }
                            items.forEach { postItem ->
                                if (postItem.categories.isNotEmpty()) {
                                    postItem.categoryName = InMemory.getCategoryById(postItem.categories[0])?.name?:""
                                }
                            }
                            listener?.onResult(items)
                        } catch (e: Exception) {
                            listener?.onResult(arrayListOf())
                        }
                    }
                    is Result.Failure -> {
                        listener?.onResult(arrayListOf())
                    }
                }
            })
        }
    }


    fun getPostsSearch(
        context: Context,
        search: String,
        tags: Int?,
        page: Int = 1,
        perPage: Int = Config.getNumberOfItemPerPage(),
        listener: ResultListener<List<PostItem>>?
    ) {
        doAsync {
            val service =
                RetrofitClientInstance.getRetrofitInstance(context)
                    ?.create(PostsService::class.java)
            service?.getPostsSearch(search, tags, page, perPage)?.enqueue(result = {
                val items: ArrayList<PostItem> = ArrayList()
                when (it) {
                    is Result.Success -> {
                        try {
                            if (!it.response.body().isNullOrEmpty()) {
                                val items = it.response.body() as ArrayList<PostItem>
                                items.sortedBy { it.modified_gmt }
                            }
                            items.forEach { postItem ->
                                if (postItem.categories.isNotEmpty()) {
                                    postItem.categoryName = InMemory.getCategoryById(postItem.categories[0])?.name?:""
                                }
                            }
                            listener?.onResult(items)
                        } catch (e: Exception) {
                            listener?.onResult(arrayListOf())
                        }
                    }
                    is Result.Failure -> {
                        listener?.onResult(arrayListOf())
                    }
                }
            })
        }
    }
}