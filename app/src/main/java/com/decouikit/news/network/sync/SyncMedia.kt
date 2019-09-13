package com.decouikit.news.network.sync

import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.interfaces.Sync
import com.decouikit.news.interfaces.SyncListener
import com.decouikit.news.network.MediaService
import com.decouikit.news.network.RetrofitClientInstance
import org.jetbrains.anko.doAsync

object SyncMedia : Sync {
    override fun sync(listener: SyncListener?) {
        val mediaService = RetrofitClientInstance.retrofitInstance?.create(MediaService::class.java)
        doAsync {
            mediaService?.getMediaList()?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (it.response.body() != null) {
                            InMemory.setMediaList(it.response.body())
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
