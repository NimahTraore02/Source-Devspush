package com.decouikit.news.firebase

import android.util.Log
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.getUrlFromString
import com.decouikit.news.interfaces.PostListener
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
        Log.e("TEST", "onMessageReceived:firebase")
        if (remoteMessage.data != null) {
            try {
                val customNotification =
                    Gson().fromJson(remoteMessage.data["custom"], CustomNotification::class.java)
                SyncPost.getPostById(
                    this,
                    customNotification.url.getUrlFromString(),
                    object : PostListener {
                        override fun onSuccess(post: PostItem) {
                            post.categoryName =
                                InMemory.getCategoryById(post.categories[0])?.name ?: ""
                            InMemory.addNotificationPosts(applicationContext, post)
                        }

                        override fun onError(error: Throwable?) {
                        }
                    })
            } catch (e: Exception) {
                Log.e("FirebaseService", e.localizedMessage, e)
            }
        } else if (remoteMessage.notification != null) {
            NotificationUtil.showNotification(
                this,
                remoteMessage.notification?.title ?: "",
                remoteMessage.notification?.body ?: ""
            )
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}

