package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.interfaces.PostListener
import com.decouikit.news.network.PostsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.PostItem
import org.jetbrains.anko.doAsync

object SyncPost {
    fun getPostById(context: Context, postId: String, listener: PostListener?) {
        if (postId.isEmpty()) {
            listener?.onError(Throwable("Post id is empty"))
            return
        }
        val service =
            RetrofitClientInstance.getRetrofitInstance(context)?.create(PostsService::class.java)
        doAsync {
            service?.getPostsById(postId)?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        try {
                            val postItem: PostItem = it.response.body() as PostItem
                            for (category in InMemory.getCategoryList()) {
                                if (postItem.categories.contains(category.id)) {
                                    postItem.categoryName = category.name
                                }
                            }
                            for (mediaItem in InMemory.getMediaList()) {
                                if (mediaItem.id == postItem.featured_media) {
                                    postItem.source_url = mediaItem.source_url
                                }
                            }
                            listener?.onSuccess(postItem)
                        } catch (e: Exception) {
                            listener?.onError(e)
                        }
                    }
                    is Result.Failure -> {
                        listener?.onError(it.error)
                    }
                }
            })
        }
    }
}