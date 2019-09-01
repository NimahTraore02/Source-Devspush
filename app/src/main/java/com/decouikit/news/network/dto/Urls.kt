package com.decouikit.news.network.dto

import com.google.gson.annotations.SerializedName

data class Urls(
    @SerializedName("24") val urlS: String,
    @SerializedName("48") val urlM: String,
    @SerializedName("96") val urlL: String
)

