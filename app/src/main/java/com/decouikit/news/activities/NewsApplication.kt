package com.decouikit.news.activities

import android.app.Application
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
