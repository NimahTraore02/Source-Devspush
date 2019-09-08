package com.decouikit.news.interfaces

import com.decouikit.news.network.dto.PostItem

interface AddBookmarkListener {
    fun addBookmark(items: List<PostItem>)
}