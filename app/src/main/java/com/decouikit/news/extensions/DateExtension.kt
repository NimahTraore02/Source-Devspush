package com.decouikit.news.extensions

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


fun Date.getDateFromString(stringDate: String): Date? {
    var date: Date? = null
    try {
        date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(stringDate)
    } catch (e: Exception) {}
    return date
}

fun Date.getCalendarDate(): String {
    val clockTime: String

    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    clockTime = formatter.format(this)
    return clockTime
}