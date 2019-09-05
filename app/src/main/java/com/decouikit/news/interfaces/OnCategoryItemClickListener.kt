package com.decouikit.news.interfaces

import com.decouikit.news.network.dto.Category

interface OnCategoryItemClickListener {
    fun onCategoryItemClick(item: Category)
}