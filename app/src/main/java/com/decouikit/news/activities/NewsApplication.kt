package com.decouikit.news.activities

import android.app.Application
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.MemoryCategory
import com.decouikit.news.database.Preference
import com.decouikit.news.notification.OneSignalNotificationOpenHandler
import com.google.android.gms.ads.MobileAds
import com.onesignal.OneSignal

class NewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // OneSignal Initialization
        OneSignal.startInit(this)
            .setNotificationOpenedHandler(OneSignalNotificationOpenHandler(this))
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()
        OneSignal.setSubscription(Preference(context = this).isPushNotificationEnabled)
        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH)
        MobileAds.initialize(this) {}
    }

    companion object {
        private var activityVisible: Boolean = false

        fun activityResumed() {
            activityVisible = true
        }

        fun activityPaused() {
            activityVisible = false
        }
    }


}
