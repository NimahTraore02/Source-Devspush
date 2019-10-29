package com.decouikit.news.extensions

import android.net.Uri
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.core.text.HtmlCompat.fromHtml

fun String.fromHtmlToString(): String {
    return fromHtml(this, FROM_HTML_MODE_COMPACT).toString()
}

fun String.getUrlFromString(): String {
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