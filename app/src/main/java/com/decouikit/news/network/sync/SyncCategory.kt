package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.InMemory
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.interfaces.ResultListener
import com.decouikit.news.network.CategoryService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.Category
import org.jetbrains.anko.doAsync

object SyncCategory {
    private val categories = mutableListOf<Category>()
    private var pageNumber = 1
    fun sync(context: Context, listener: ResultListener<Boolean>?) {
        val categoryService =
            RetrofitClientInstance.getRetrofitInstance(context)?.create(CategoryService::class.java)
        doAsync {
            categoryService?.getCategoryList(page = pageNumber)?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (!it.response.body().isNullOrEmpty()) {
                            categories.addAll(it.response.body() as List<Category>)
                            pageNumber++
                            sync(context, listener)
                            return@enqueue
                        }
                        pageNumber = 1
                        InMemory.setCategoryList(categories)
                        Preference(context).persisCategories(categories as ArrayList<Category>)
                        categories.clear()
                        listener?.onResult(true)
                    }
                    is Result.Failure -> {
                        pageNumber = 1
                        Preference(context).loadCategories()
                        categories.clear()
                        listener?.onResult(false)
                    }
                }
            })
        }
    }

    fun getCategoryById(id: Int, context: Context, listener: ResultListener<Category>?) {
        doAsync {
            val categoryService =
                RetrofitClientInstance.getRetrofitInstance(context)
                    ?.create(CategoryService::class.java)
            val category = InMemory.getCategoryById(id)
            if (category != null) {
                listener?.onResult(category)
            } else {
                categoryService?.getCategoryById(id.toString())?.enqueue {
                    when (it) {
                        is Result.Success -> {
                            try {
                                val categoryItem = it.response.body() as Category
                                InMemory.addCategory(context, categoryItem)
                                listener?.onResult(categoryItem)
                            } catch (e: Exception) {
                                listener?.onResult(null)
                            }
                        }
                        is Result.Failure -> {
                            listener?.onResult(null)
                        }
                    }
                }
            }
        }
    }
}
