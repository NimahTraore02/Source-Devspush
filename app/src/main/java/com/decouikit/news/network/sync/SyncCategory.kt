package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.interfaces.Sync
import com.decouikit.news.interfaces.SyncListener
import com.decouikit.news.network.CategoryService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.Category
import org.jetbrains.anko.doAsync

object SyncCategory : Sync {
    override fun sync(context: Context, listener: SyncListener?) {
        val categoryService =
            RetrofitClientInstance.getRetrofitInstance(context)?.create(CategoryService::class.java)
        doAsync {
            categoryService?.getCategoryList()?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (!it.response.body().isNullOrEmpty()) {
                            InMemory.setCategoryList(it.response.body() as List<Category>)
                        }
                        listener?.finish(true)
                    }
                    is Result.Failure -> {
                        listener?.finish(false)
                    }
                }
            })
        }
    }
}
