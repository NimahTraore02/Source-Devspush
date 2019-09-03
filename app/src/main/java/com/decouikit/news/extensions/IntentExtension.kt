package com.decouikit.news.extensions

import android.content.Intent

fun Intent.loadDeepLinkUrl(): String {
    if (Intent.ACTION_VIEW == action &&  data != null) {
        return data.toString()
    }
    return ""
}