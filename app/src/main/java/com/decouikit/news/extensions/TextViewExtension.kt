package com.decouikit.news.extensions

import android.text.TextUtils
import android.widget.TextView
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.network.sync.SyncCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun TextView.setHtml(content: String) {
    this.text = content.decode()
}

fun TextView.loadCategoryName(postItem: PostItem) {
    if (TextUtils.isEmpty(postItem.categoryName)) {
        GlobalScope.launch(Dispatchers.Main) {
            val category = SyncCategory.getCategoryById(postItem.categories[0], context)
            text = category?.name?.decode()
        }
    } else {
        this.text = postItem.categoryName.decode()
    }
}