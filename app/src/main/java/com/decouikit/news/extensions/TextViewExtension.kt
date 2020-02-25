package com.decouikit.news.extensions

import android.text.TextUtils
import android.widget.TextView
import com.decouikit.news.interfaces.ResultListener
import com.decouikit.news.network.dto.Category
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.network.sync.SyncCategory

fun TextView.setHtml(content: String) {
    this.text = content.fromHtmlToString()
}

fun TextView.loadCategoryName(postItem: PostItem) {
    if (TextUtils.isEmpty(postItem.categoryName)) {
        SyncCategory.getCategoryById(
            postItem.categories[0],
            context,
            object : ResultListener<Category> {
                override fun onResult(value: Category?) {
                    value?.let {
                        text = value.name
                    }
                }
            })
    } else {
        this.text = postItem.categoryName
    }
}