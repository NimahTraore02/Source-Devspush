package com.decouikit.news.extensions

fun Int.categoryToString(): String? {
    return if (this > 0) {
        this.toString()
    } else {
        null
    }
}
