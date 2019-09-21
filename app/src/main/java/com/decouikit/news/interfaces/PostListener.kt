package com.decouikit.news.interfaces

import com.decouikit.news.network.dto.PostItem

interface PostListener {
    fun onSuccess(post: PostItem)
    fun onError(error: Throwable?)
}