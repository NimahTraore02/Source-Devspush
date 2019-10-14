package com.decouikit.news.database
//installDays - default 10, 0 means install day.
//launchTimes - default 10
//remindInterval - default 1
//showLaterButton - default true

data class RateMeConfig(
    val installDays: Int = 10,
    val launchTimes: Int = 10,
    val remindInterval:Int = 1,
    val showLaterButton:Boolean = false,
    val debug:Boolean = false
)