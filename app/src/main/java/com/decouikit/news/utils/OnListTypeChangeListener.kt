package com.decouikit.news.utils

import com.decouikit.news.adapters.common.CommonListAdapterType

interface OnListTypeChangeListener {
    fun onListTypeChange(adapterType: CommonListAdapterType, listType: ListType)
}