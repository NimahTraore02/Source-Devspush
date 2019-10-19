package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.interfaces.SyncListener

object SyncApi : SyncListener {
    private var syncApiCounter = 0
    private var isSyncSuccess = true
    private var listener: SyncListener? = null
    private val services = arrayListOf(SyncMedia, SyncUsers, SyncCategory, SyncTags)

    fun sync(context: Context, listener: SyncListener?) {
        this.listener = listener
        syncApiCounter = services.size
        services.forEach { it.sync(context,this) }
    }

    override fun finish(success: Boolean) {
        syncApiCounter--
        isSyncSuccess = isSyncSuccess && success
        if (syncApiCounter == 0) {
            listener?.finish(isSyncSuccess)
        }
    }

}