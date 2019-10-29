package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.InMemory
import com.decouikit.news.interfaces.SyncListener

object SyncApi : SyncListener {
    private var syncApiCounter = 0
    private var isSyncSuccess = true
    private var listener: SyncListener? = null
    private val services = arrayListOf(SyncUsers, SyncCategory)

    fun sync(context: Context, listener: SyncListener?) {
        this.listener = listener
        syncApiCounter = services.size
        services.forEach { it.sync(context, this) }
        InMemory.loadTag(context)
        InMemory.loadNotificationPosts(context)
        InMemory.loadBookmark(context)
    }

    override fun finish(success: Boolean) {
        syncApiCounter--
        isSyncSuccess = isSyncSuccess && success
        if (syncApiCounter == 0) {
            listener?.finish(isSyncSuccess)
        }
    }

}