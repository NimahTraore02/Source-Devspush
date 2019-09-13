package com.decouikit.news.interfaces

import com.decouikit.news.network.dto.PostItem

interface FeaturedNewsListener {
    fun bookmarkFeaturedNews(items: List<PostItem>)

    fun openPost(items: List<PostItem>)
}