package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.InMemory
import com.decouikit.news.interfaces.ResultListener

object SyncApi {

    fun sync(context: Context, listener: ResultListener<Boolean>?) {
        SyncCategory.sync(context, object: ResultListener<Boolean> {
            override fun onResult(value: Boolean?) {
                InMemory.loadTag(context)
                InMemory.loadNotificationPosts(context)
                InMemory.loadBookmark(context)
                InMemory.loadUsers(context)
                listener?.onResult(value)
            }
        })
    }
}