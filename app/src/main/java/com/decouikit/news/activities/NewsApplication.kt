package com.decouikit.news.activities

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.MemoryCategory
import com.decouikit.news.database.Config
import com.decouikit.news.database.Preference
import com.decouikit.news.notification.OneSignalNotificationOpenHandler
import com.google.android.gms.ads.MobileAds
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
