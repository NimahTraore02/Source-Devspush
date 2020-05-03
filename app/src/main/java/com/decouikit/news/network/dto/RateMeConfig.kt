package com.decouikit.news.network.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//installDays - default 10, 0 means install day.
//launchTimes - default 10
//remindInterval - default 1
//showLaterButton - default true

@Parcelize
class RateMeConfig(
    val installDays: Int = 10,
    val launchTimes: Int = 10,
    val remindInterval: Int = 1,
    val showLaterButton: Boolean = false,
    val debug: Boolean = false
) : Parcelable