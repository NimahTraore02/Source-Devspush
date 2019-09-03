package com.decouikit.news.activities

import android.app.Application
import android.content.Intent
import com.decouikit.news.activities.base.MainActivity
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal

class NewsApplication : Application(), OneSignal.NotificationOpenedHandler {


    override fun onCreate() {
        super.onCreate()
        // OneSignal Initialization
        OneSignal.startInit(this)
            .setNotificationOpenedHandler(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()
    }

    override fun notificationOpened(result: OSNotificationOpenResult) {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (result.notification.payload.launchURL != null) {
            intent.putExtra(Intent.EXTRA_TEXT, result.notification.payload.launchURL)
            intent.type = "text/plain"
        }
        startActivity(intent)
    }


    companion object {
        private var activityVisible: Boolean = false

        fun isActivityVisible(): Boolean {
            return activityVisible
        }

        fun activityResumed() {
            activityVisible = true
        }

        fun activityPaused() {
            activityVisible = false
        }
    }


}
