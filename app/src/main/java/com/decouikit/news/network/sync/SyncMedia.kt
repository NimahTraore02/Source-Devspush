package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.InMemory
import com.decouikit.news.network.MediaService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.MediaItem
import retrofit2.awaitResponse

object SyncMedia {

    suspend fun getMediaById(id: Int, context: Context): MediaItem? {
        val service = getService(context)
        val media = InMemory.getMediaById(id)
        if (media != null) {
            return media
        } else {
            val response = service?.getMediaById(id.toString())?.awaitResponse()
            if (response?.isSuccessful == true) {
                try {
                    val mediaItem = response.body() as MediaItem
                    InMemory.addMedia(context, mediaItem)
                    return mediaItem
                } catch (e: Exception) { }
            }
            return null
        }
    }

    private fun getService(context: Context): MediaService? {
        return RetrofitClientInstance
            .getRetrofitInstance(context)
            ?.create(MediaService::class.java)
    }
}