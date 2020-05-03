package com.decouikit.news.network.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class AvatarUrl(
    @SerializedName("24") val url24: String,
    @SerializedName("48") val url48: String,
    @SerializedName("96") val url96: String
): Parcelable