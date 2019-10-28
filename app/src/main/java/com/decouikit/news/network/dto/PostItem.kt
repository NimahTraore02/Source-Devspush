package com.decouikit.news.network.dto

import android.view.View
import com.decouikit.news.utils.NewsConstants
import com.google.gson.annotations.SerializedName

data class PostItem(
    @SerializedName("id") val id: Int,
    @SerializedName("date") val date: String,
    @SerializedName("date_gmt") val date_gmt: String,

    @SerializedName("modified") val modified: String,
    @SerializedName("modified_gmt") val modified_gmt: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("status") val status: String,
    @SerializedName("type") val type: String,
    @SerializedName("link") val link: String,
    @SerializedName("title") val title: Rendered,
    @SerializedName("guid") val guid: Rendered,
    @SerializedName("content") val content: Rendered,
    @SerializedName("author") val author: Int,
    @SerializedName("comment_status") val comment_status: String,
    @SerializedName("ping_status") val ping_status: String,
    @SerializedName("template") val template: String,
    @SerializedName("featured_media") val featured_media: Int,
    @SerializedName("sticky") val sticky: Boolean,
    @SerializedName("format") val format: String,
    @SerializedName("categories") val categories: List<Int>,
    @SerializedName("tags") val tags: List<Int>,
    var source_url:String, var categoryName: String = "", var isBookmarked: Boolean = false
) {
    fun getCommentVisible(): Int {
        return if (comment_status == NewsConstants.OPEN_COMMENT_STATUS) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
