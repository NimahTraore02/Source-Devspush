package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.interfaces.ResultListener
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.TagService
import com.decouikit.news.network.dto.Tag
import org.jetbrains.anko.doAsync

object SyncTags {
    fun getTagById(id: Int, context: Context, listener: ResultListener<Tag>?) {
        doAsync {
            val tagService =
                RetrofitClientInstance.getRetrofitInstance(context)?.create(TagService::class.java)
            val tag = InMemory.getTagById(id)
            if (tag != null) {
                listener?.onResult(tag)
            } else {
                tagService?.getTagById(id.toString())?.enqueue {
                    when (it) {
                        is Result.Success -> {
                            try {
                                val tagItem = it.response.body() as Tag
                                InMemory.addTag(context, tagItem)
                                listener?.onResult(tagItem)
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
