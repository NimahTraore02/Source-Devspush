package com.decouikit.news.network.dto

import com.google.gson.annotations.SerializedName

data class CommentItem(
    @SerializedName("id") val id: Int,
    @SerializedName("date") val date: String,
    @SerializedName("date_gmt") val date_gmt: String,
    @SerializedName("content") val content: Rendered,
    @SerializedName("status") val status: String,
    @SerializedName("type") val type: String,
    @SerializedName("link") val link: String,

    @SerializedName("modified") val modified: String,
    @SerializedName("modified_gmt") val modified_gmt: String,
    @SerializedName("slug") val slug: String,

    @SerializedName("title") val title: Rendered,
    @SerializedName("guid") val guid: Rendered,

    @SerializedName("post") val post: Int,
    @SerializedName("parent") val parent: Int,

    @SerializedName("author") val author: Int,
    @SerializedName("author_name") val authorName: String,
    @SerializedName("author_link") val authorLink: String,
    @SerializedName("author_avatar_urls") val author_avatar_urls: AvatarUrl
)