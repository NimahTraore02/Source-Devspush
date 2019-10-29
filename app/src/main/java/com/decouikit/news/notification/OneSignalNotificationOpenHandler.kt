package com.decouikit.news.notification

import android.content.Context
import android.content.Intent
import android.util.Log
import com.decouikit.news.activities.SplashActivity
import com.decouikit.news.database.Config
import com.onesignal.OSNotification
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal

class OneSignalNotificationOpenHandler(var context: Context): OneSignal.NotificationOpenedHandler {
    override fun notificationOpened(result: OSNotificationOpenResult) {
        val intent = Intent(context, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (result.notification.payload.launchURL != null) {
            intent.putExtra(Intent.EXTRA_TITLE, result.notification.payload.launchURL)
            intent.type = "text/plain"
        }
        context.startActivity(intent)
    }
}