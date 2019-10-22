package com.decouikit.news.extensions

import android.widget.TextView

//
// Created by Dragan Koprena on 10/22/19.
//

fun TextView.setHtml(content: String) {
    this.text = content.fromHtmlToString()
}