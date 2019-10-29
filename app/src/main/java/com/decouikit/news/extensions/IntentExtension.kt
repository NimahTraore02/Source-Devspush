package com.decouikit.news.extensions

import android.content.Intent
import android.content.Intent.EXTRA_TITLE
import android.net.Uri

fun Intent.loadDeepLinkUrl(): String {
    return if (Intent.ACTION_VIEW == action && data != null) {
        getUrlFromString(data.toString())
    } else {
        getUrlFromString(getStringExtra(EXTRA_TITLE))
    }
}

fun getUrlFromString(url:String?): String {
    try {
        if (url != null) {
            if (url.isNotEmpty()) {
                val uri = Uri.parse(url)
                return uri.lastPathSegment.toString()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}