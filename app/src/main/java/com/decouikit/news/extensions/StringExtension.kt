package com.decouikit.news.extensions

import android.net.Uri
import android.os.Build
import android.text.Html
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.core.text.HtmlCompat.fromHtml
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun String.decode(): String {
    return fromHtml(this, FROM_HTML_MODE_COMPACT).toString()
}

fun String.getPostIdFromUrl(): String {
    try {
        if (isNotEmpty()) {
            val uri = Uri.parse(this)
            return uri.lastPathSegment.toString()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

fun String.toDate(): Date? {
    val tz = TimeZone.getTimeZone("UTC")
    val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
    df.timeZone = tz
    try {
        return df.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}