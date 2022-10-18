package com.decouikit.news.interfaces

import com.decouikit.news.network.dto.PostItem

interface RemoveBookmarkListener {
    fun removeBookmark(item: PostItem)
}