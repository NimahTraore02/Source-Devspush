package com.decouikit.news.network.dto

import com.google.gson.annotations.SerializedName

data class CommentRequest(
    @SerializedName("post") val post: Int,
    @SerializedName("content") val content: String,
    @SerializedName("author_name") val authorName: String,
    @SerializedName("author_email") val authorEmail: String,
    @SerializedName("author_url") val authorUrl: String
)