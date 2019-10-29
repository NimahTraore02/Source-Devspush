package com.decouikit.news.network.dto

import com.google.gson.annotations.SerializedName

//
// Created by Dragan Koprena on 10/29/19.
//

data class CustomNotification(
    @SerializedName("u") val url: String,
    @SerializedName("i") val id: String
)