package com.decouikit.news.network.sync

import android.content.Context
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
}