package com.decouikit.news.activities

import android.app.Application
import com.onesignal.OneSignal

public class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // OneSignal Initialization
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init();
    }
}
