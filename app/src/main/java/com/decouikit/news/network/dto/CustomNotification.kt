package com.decouikit.news.network.dto

import com.google.gson.annotations.SerializedName

data class CustomNotification(
    @SerializedName("u") val url: String,
    @SerializedName("i") val id: String
)