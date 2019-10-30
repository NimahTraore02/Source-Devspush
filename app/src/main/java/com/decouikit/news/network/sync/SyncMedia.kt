package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.interfaces.ResultListener
import com.decouikit.news.network.MediaService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.MediaItem
import org.jetbrains.anko.doAsync

object SyncMedia {

    fun getMediaById(id: Int, context: Context, listener: ResultListener<MediaItem>?) {
        doAsync {
            val mediaService =
                RetrofitClientInstance.getRetrofitInstance(context)
                    ?.create(MediaService::class.java)
            val media = InMemory.getMediaById(id)
            if (media != null) {
                listener?.onResult(media)
            } else {
                mediaService?.getMediaById(id.toString())?.enqueue {
                    when (it) {
                        is Result.Success -> {
                            try {
                                val mediaItem = it.response.body() as MediaItem
                                InMemory.addMedia(context, mediaItem)
                                listener?.onResult(mediaItem)
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