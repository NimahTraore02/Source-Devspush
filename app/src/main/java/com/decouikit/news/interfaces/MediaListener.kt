package com.decouikit.news.interfaces

import com.decouikit.news.network.dto.MediaItem

interface MediaListener {
    fun onResult(isSuccess: Boolean, mediaItem: MediaItem?)
}