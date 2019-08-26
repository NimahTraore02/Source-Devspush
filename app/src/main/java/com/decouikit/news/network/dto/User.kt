package com.decouikit.news.network.dto

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
    @SerializedName("description") val description: String,
    @SerializedName("link") val link: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("avatar_urls") val avatarUrls: AvatarUrl
)