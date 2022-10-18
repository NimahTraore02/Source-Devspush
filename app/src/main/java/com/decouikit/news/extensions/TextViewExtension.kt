package com.decouikit.news.extensions

import android.text.TextUtils
import android.widget.TextView
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.network.sync.SyncCategory
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun TextView.setHtml(content: String) {
    this.text = content.decode()
}

fun TextView.loadCategoryName(postItem: PostItem) {
    if (TextUtils.isEmpty(postItem.categoryName)) {
        GlobalScope.launch(Dispatchers.Main) {
            val category = null //SyncCategory.getCategoryById(postItem.categories[0], context)
            text = if (postItem.job_listing_type.isEmpty()) "" else (postItem.metas["_job_type"] as LinkedTreeMap<*, *>)[postItem.job_listing_type[0].toString()].toString()
        }
    } else {
        this.text = postItem.categoryName.decode()
    }
}