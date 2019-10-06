package com.decouikit.news.network.sync

import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.interfaces.Sync
import com.decouikit.news.interfaces.SyncListener
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.TagService
import com.decouikit.news.network.dto.Tag
import org.jetbrains.anko.doAsync

object SyncTags : Sync {
    override fun sync(listener: SyncListener?) {
        val tagService = RetrofitClientInstance.retrofitInstance?.create(TagService::class.java)
        doAsync {
            tagService?.getTagList()?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (it.response.body() != null) {
                            InMemory.setTagsList(it.response.body() as List<Tag>)
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
