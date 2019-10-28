package com.decouikit.news.network.sync

import android.content.Context
import android.util.Log
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.interfaces.MediaListener
import com.decouikit.news.network.MediaService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.MediaItem
import java.lang.Exception

object SyncMedia {

    fun getMediaById(id: Int, context: Context, listener: MediaListener?) {
        val mediaService =
            RetrofitClientInstance.getRetrofitInstance(context)?.create(MediaService::class.java)
        val mediaItem = InMemory.getMediaById(id)
        if (mediaItem != null) {
            listener?.onResult(true, mediaItem)
        } else {
            mediaService?.getMediaById(id.toString())?.enqueue {
                when (it) {
                    is Result.Success -> {
                        try {
                            val mediaItem = it.response.body() as MediaItem
                            InMemory.addMedia(context, mediaItem)
                            listener?.onResult(true, mediaItem)
                        } catch (e: Exception) {
                            Log.e("TEST", e.toString(), e)
                        }
                    }
                    is Result.Failure -> {
                        listener?.onResult(false, null)
                    }
                }
            }
        }
    }
}