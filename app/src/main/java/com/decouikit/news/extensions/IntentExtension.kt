package com.decouikit.news.extensions

import android.content.Intent
import android.net.Uri

fun Intent.loadDeepLinkUrl(): String {
    var postId = ""
    if (Intent.ACTION_VIEW == action && data != null) {
        try {
            if (data.toString().isNotEmpty()) {
                val uri = Uri.parse(data.toString())
                postId = uri.lastPathSegment.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return postId
    }
    return postId
}