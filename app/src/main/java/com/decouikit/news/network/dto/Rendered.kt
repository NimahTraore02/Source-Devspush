package com.decouikit.news.network.dto

import com.google.gson.annotations.SerializedName

data class Rendered(@SerializedName("rendered") val rendered: String,
                    @SerializedName("protected") val protected: Boolean)