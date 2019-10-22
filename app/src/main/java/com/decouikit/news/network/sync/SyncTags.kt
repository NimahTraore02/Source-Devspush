package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.interfaces.Sync
import com.decouikit.news.interfaces.SyncListener
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.TagService
import com.decouikit.news.network.dto.Category
import com.decouikit.news.network.dto.Tag
import org.jetbrains.anko.doAsync

object SyncTags : Sync {
    private val tags = mutableListOf<Tag>()
    var pageNumber = 1
    override fun sync(context: Context, listener: SyncListener?) {
        val tagService = RetrofitClientInstance.getRetrofitInstance(context)?.create(TagService::class.java)
        doAsync {
            tagService?.getTagList(page = pageNumber)?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (!it.response.body().isNullOrEmpty()) {
                            tags.addAll(it.response.body() as List<Tag>)
                            pageNumber++
                            sync(context, listener)
                            return@enqueue
                        }
                        pageNumber = 1
                        InMemory.setTagsList(tags)
                        listener?.finish(true)
                    }
                    is Result.Failure -> {
                        pageNumber = 1
                        InMemory.setTagsList(tags)
                        listener?.finish(false)
                    }
                }
            })
        }
    }
}
