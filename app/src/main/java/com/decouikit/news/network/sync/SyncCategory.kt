package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.InMemory
import com.decouikit.news.database.Preference
import com.decouikit.news.network.CategoryService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.Category
import retrofit2.awaitResponse

object SyncCategory {

    suspend fun sync(context: Context): Boolean {
        val service = getService(context)
        val response = service?.getCategoryList(page = 1)?.awaitResponse()
        val categories = mutableListOf<Category>()
        return if (response?.isSuccessful == true) {
            if (!response.body().isNullOrEmpty()) {
                categories.addAll(response.body() as List<Category>)
            }
            InMemory.setCategoryList(categories)
            Preference(context).persisCategories(categories as ArrayList<Category>)
            categories.clear()
            true
        } else {
            Preference(context).loadCategories()
            false
        }
    }

    suspend fun getCategoryById(id: Int, context: Context): Category? {
        val service = getService(context)
        val category = InMemory.getCategoryById(id)
        if (category != null) {
            return category
        } else {
            val response = service?.getCategoryById(id.toString())?.awaitResponse()
            if (response?.isSuccessful == true) {
                try {
                    val categoryItem = response.body() as Category
                    InMemory.addCategory(context, categoryItem)
                    return categoryItem
                } catch (e: Exception) { }
            }
            return null
        }
    }


    private fun getService(context: Context): CategoryService? {
        return RetrofitClientInstance
            .getRetrofitInstance(context)
            ?.create(CategoryService::class.java)
    }
}
