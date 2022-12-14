package com.decouikit.news.extensions

import android.content.Intent
import android.content.Intent.EXTRA_TITLE

fun Intent.loadDeepLinkUrl(): String {
    return if (Intent.ACTION_VIEW == action && data != null) {
        data.toString().getPostIdFromUrl()
    } else {
        getStringExtra(EXTRA_TITLE)?.getPostIdFromUrl() ?: ""
    }
}