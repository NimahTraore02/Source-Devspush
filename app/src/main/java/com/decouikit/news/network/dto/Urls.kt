package com.decouikit.news.network.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Urls(
    @SerializedName("24") val urlS: String,
    @SerializedName("48") val urlM: String,
    @SerializedName("96") val urlL: String
) : Parcelable

