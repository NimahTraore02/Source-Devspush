package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.interfaces.TagListener
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.TagService
import com.decouikit.news.network.dto.Tag

object SyncTags {

    fun getTagById(id: Int, context: Context, listener: TagListener?) {
        val tagService =
            RetrofitClientInstance.getRetrofitInstance(context)?.create(TagService::class.java)
        val tag = InMemory.getTagById(id)
        if (tag != null) {
            listener?.onResult(true, tag)
        } else {
            tagService?.getTagById(id.toString())?.enqueue {
                when (it) {
                    is Result.Success -> {
                        val tag = it.response.body() as Tag
                        InMemory.addTag(context, tag)
                        listener?.onResult(true, tag)
                    }
                    is Result.Failure -> {
                        listener?.onResult(false, null)
                    }
                }
            }
        }
    }
}
