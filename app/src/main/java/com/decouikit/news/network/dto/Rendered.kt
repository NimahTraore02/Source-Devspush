package com.decouikit.news.network.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Rendered(
    @SerializedName("rendered") val rendered: String,
    @SerializedName("protected") val protected: Boolean
) : Parcelable