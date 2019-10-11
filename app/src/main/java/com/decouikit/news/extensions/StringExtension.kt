package com.decouikit.news.extensions

import android.os.Build
import android.text.Html
import android.text.Html.fromHtml

fun String.fromHtmlToString(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        fromHtml(this, Html.FROM_HTML_MODE_COMPACT).toString()
    } else {
        fromHtml(this).toString()
    }
}
