package com.decouikit.news.extensions

//
// Created by Dragan koprena on 10/27/19.
//

fun Int.categoryToString(): String? {
    return if (this > 0) {
        this.toString()
    } else {
        null
    }
}
