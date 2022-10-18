package com.decouikit.news.network.dto

import android.view.View
import com.decouikit.news.utils.NewsConstants
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.Objects

class PostItem(
    val id: Int,
    val date: String,
    val date_gmt: String,
    val modified: String,
    val modified_gmt: String,
    val slug: String,
    val status: String,
    val type: String,
    val link: String,
    val title: Rendered,
    val guid: Rendered,
    val content: Rendered,
    val author: Int,
    val comment_status: String,
    val ping_status: String,
    val template: String,
    val featured_media: Int,
    val sticky: Boolean,
    val format: String,
    val categories: List<Int>,
    val tags: List<Int>,
    val job_listing_type: List<Int>,
    val metas: HashMap<String, Any>,
    var source_url: String, var categoryName: String = "", var isBookmarked: Boolean = false,
    ) {
    fun getCommentVisible(): Int {
        return if (comment_status == NewsConstants.OPEN_COMMENT_STATUS) View.VISIBLE else View.GONE
    }
}
