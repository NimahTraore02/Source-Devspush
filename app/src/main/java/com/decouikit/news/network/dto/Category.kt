package com.decouikit.news.network.dto

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id") val id: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("description") val description: String,
    @SerializedName("link") val link: String,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("taxonomy") val taxonomy: String,
    @SerializedName("parent") val parent: Int
)