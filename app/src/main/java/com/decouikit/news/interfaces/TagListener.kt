package com.decouikit.news.interfaces

import com.decouikit.news.network.dto.Tag

interface TagListener {
    fun onResult(isSuccess: Boolean, tag: Tag?)
}