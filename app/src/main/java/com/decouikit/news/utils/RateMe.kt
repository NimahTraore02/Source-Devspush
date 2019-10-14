package com.decouikit.news.utils

import android.app.Activity
import com.decouikit.news.database.Config
import hotchemi.android.rate.AppRate

object RateMe {
    fun rateApp(activity: Activity) {
        if (Config.isRateMeEnabled()) {
            val config = Config.getRateMeConfig()
            AppRate.with(activity)
                .setInstallDays(config.installDays)
                .setLaunchTimes(config.launchTimes) // default 10
                .setRemindInterval(config.remindInterval) // default 1
                .setShowLaterButton(config.showLaterButton) // default true
                .setDebug(config.debug) // default false
                .monitor()

            // Show a dialog if meets conditions
            AppRate.showRateDialogIfMeetsConditions(activity)

        }
    }
}