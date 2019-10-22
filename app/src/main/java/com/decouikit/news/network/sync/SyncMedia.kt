package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.interfaces.Sync
import com.decouikit.news.interfaces.SyncListener
import com.decouikit.news.network.MediaService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.MediaItem
import org.jetbrains.anko.doAsync

object SyncMedia : Sync {
    private val mediaList = mutableListOf<MediaItem>()
    var pageNumber = 1
    override fun sync(context: Context, listener: SyncListener?) {
        val mediaService =
            RetrofitClientInstance.getRetrofitInstance(context)?.create(MediaService::class.java)
        doAsync {
            mediaService?.getMediaList(page = pageNumber)?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (!it.response.body().isNullOrEmpty()) {
                            mediaList.addAll(it.response.body() as List<MediaItem>)
                            pageNumber++
                            if (pageNumber < 3) {
                                sync(context, listener)
                                return@enqueue
                            }
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
