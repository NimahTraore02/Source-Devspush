package com.decouikit.news.network.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class CustomNotification(
    @SerializedName("u") val url: String,
    @SerializedName("i") val id: String
) : Parcelable