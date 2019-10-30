package com.decouikit.news.interfaces

interface ResultListener<T> {
    fun onResult(value: T?)
}