package com.decouikit.news.firebase

import android.util.Log
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.getUrlFromString
import com.decouikit.news.interfaces.ResultListener
import com.decouikit.news.network.dto.CustomNotification
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.network.sync.SyncPost
import com.decouikit.news.notification.NotificationUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        try {
            val customNotification =
                Gson().fromJson(remoteMessage.data["custom"], CustomNotification::class.java)
            SyncPost.getPostById(
                this,
                customNotification.url.getUrlFromString(),
                object : ResultListener<PostItem> {
                    override fun onResult(value: PostItem?) {
                        if (value != null) {
                            value.categoryName = InMemory.getCategoryById(value.categories[0])?.name ?: ""
                            InMemory.addNotificationPosts(applicationContext, value)
                        }
                    }
                })
        } catch (e: Exception) {
            Log.e("FirebaseService", e.localizedMessage, e)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}

