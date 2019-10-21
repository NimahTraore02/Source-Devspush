package com.decouikit.news.extensions

import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.core.text.HtmlCompat.fromHtml

fun String.fromHtmlToString(): String {
    return fromHtml(this, FROM_HTML_MODE_COMPACT).toString()
}
