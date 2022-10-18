package com.decouikit.news.interfaces

import com.decouikit.news.network.dto.Tag

interface OnHashTagClickListener {
    fun onHashtagClick(tag: Tag)
}