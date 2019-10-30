package com.decouikit.news.interfaces

interface ResultListener<T> {
    fun onResult(tag: T?)
}