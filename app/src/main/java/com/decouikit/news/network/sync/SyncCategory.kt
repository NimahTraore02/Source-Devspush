package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.interfaces.ResultListener
import com.decouikit.news.network.CategoryService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.Category
import org.jetbrains.anko.doAsync

object SyncCategory {
    private val categories = mutableListOf<Category>()
    var pageNumber = 1
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
                        categories.clear()
                        listener?.onResult(true)
                    }
                    is Result.Failure -> {
                        pageNumber = 1
                        InMemory.setCategoryList(categories)
                        categories.clear()
                        listener?.onResult(false)
                    }
                }
            })
        }
    }
}
