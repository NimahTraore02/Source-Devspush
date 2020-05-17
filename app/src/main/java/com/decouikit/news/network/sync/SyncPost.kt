package com.decouikit.news.network.sync

import android.content.Context
import android.util.Log
import com.decouikit.news.database.Config
import com.decouikit.news.database.InMemory
import com.decouikit.news.network.PostsService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.PostItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.awaitResponse

object SyncPost {
    suspend fun getPostById(context: Context, postId: String): PostItem? {
        val service = getService(context)
        val response = service?.getPostsById(postId)?.awaitResponse()
        if (response?.isSuccessful == true) {
            try {
                val postItem: PostItem = response.body() as PostItem
                for (category in InMemory.getCategoryList(context)) {
                    if (postItem.categories.contains(category.id)) {
                        postItem.categoryName = category.name
                    }
                }
                return postItem
            } catch (e: Exception) { }
        }
        return null
    }

    suspend fun getPostsList(
        context: Context, categoryId: String?,
        sticky: Boolean?, page: Int = 1,
        perPage: Int = Config.getNumberOfItemPerPage()
    ): List<PostItem> {
        var items: ArrayList<PostItem> = ArrayList()
        val service = getService(context)
        val response = service?.getPostsList(categoryId, sticky, page, perPage)?.awaitResponse()
        if (response?.isSuccessful == true) {
            try {
                if (!response.body().isNullOrEmpty()) {
                    items = response.body() as ArrayList<PostItem>
                }
                items.forEach { postItem ->
                    if (postItem.categories.isNotEmpty()) {
                        postItem.categoryName =
                            InMemory.getCategoryById(postItem.categories[0])?.name ?: ""
                    }
                }
                return items
            } catch (e: Exception) { }
        }
        return items
    }

    suspend fun getPostsSearch(
        context: Context,
        search: String,
        tags: Int?,
        page: Int = 1,
        perPage: Int = Config.getNumberOfSearchedItemsPerPage()
    ): List<PostItem> {
        var items = ArrayList<PostItem>()
        val service = getService(context)
        var response: Response<List<PostItem>>? = null
        try {
            response = service?.getPostsSearch(search, tags, page, perPage)?.awaitResponse()
        } catch (e: Exception) {
            Log.e("SEARCH_TEST", " "+e.localizedMessage)
        }
        if (response?.isSuccessful == true) {
            try {
                if (!response.body().isNullOrEmpty()) {
                    items = response.body() as ArrayList<PostItem>
                    items.sortedBy { it.id }
                }
                items.forEach { postItem ->
                    if (postItem.categories.isNotEmpty()) {
                        postItem.categoryName =
                            InMemory.getCategoryById(postItem.categories[0])?.name ?: ""
                    }
                }
                return items
            } catch (e: Exception) { }
        }
        return items
    }

    private fun getService(context: Context): PostsService? {
        return RetrofitClientInstance
            .getRetrofitInstance(context)
            ?.create(PostsService::class.java)
    }
}