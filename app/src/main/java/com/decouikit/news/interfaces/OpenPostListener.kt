package com.decouikit.news.interfaces

import android.view.View
import com.decouikit.news.network.dto.PostItem

interface OpenPostListener {
    fun openPost(view: View, item: PostItem)
}