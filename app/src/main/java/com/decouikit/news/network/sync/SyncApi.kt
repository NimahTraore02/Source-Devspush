package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.InMemory

object SyncApi {
    suspend fun sync(context: Context): Boolean {
        val syncResult = SyncCategory.sync(context)
        InMemory.loadTag(context)
        InMemory.loadNotificationPosts(context)
        InMemory.loadBookmark(context)
        InMemory.loadUsers(context)
        return syncResult
    }
}