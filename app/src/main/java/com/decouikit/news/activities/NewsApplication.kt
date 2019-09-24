package com.decouikit.news.activities

import android.app.Application
import android.content.Intent
import android.util.Log
import com.decouikit.news.database.Config
import com.decouikit.news.database.Preference
import com.decouikit.news.notification.OneSignalNotificationOpenHandler
import com.google.android.gms.ads.MobileAds
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal


class NewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // OneSignal Initialization
        if (Preference(context = this).isPushNotificationEnabled) {
            OneSignal.startInit(this)
                .setNotificationOpenedHandler(OneSignalNotificationOpenHandler(this))
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()
        } else {
            OneSignal.setSubscription(false)
        }
        MobileAds.initialize(this) {}
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
