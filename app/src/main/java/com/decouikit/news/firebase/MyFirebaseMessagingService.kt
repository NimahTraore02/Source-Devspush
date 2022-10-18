package com.decouikit.news.firebase

import android.util.Log
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.getPostIdFromUrl
import com.decouikit.news.network.dto.CustomNotification
import com.decouikit.news.network.sync.SyncPost
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        try {
            val customNotification =
                Gson().fromJson(
                    remoteMessage.data["custom"],
                    CustomNotification::class.java
                )
            GlobalScope.launch(Dispatchers.IO) {
                val postItem = SyncPost.getPostById(
                    applicationContext,
                    customNotification.url.getPostIdFromUrl()
                )
                postItem?.let {
                    it.categoryName = InMemory.getCategoryById(it.categories[0])?.name ?: ""
                    InMemory.addNotificationPosts(applicationContext, it)
                }
            }
        } catch (e: Exception) {
            Log.e("FirebaseService", e.localizedMessage, e)
        }
    }
}

