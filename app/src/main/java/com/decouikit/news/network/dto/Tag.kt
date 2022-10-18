package com.decouikit.news.network.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Tag(
    @SerializedName("id") val id: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("description") val description: String,
    @SerializedName("link") val link: String,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("taxonomy") val taxonomy: String,
    @SerializedName("parent") val parent: Int
) : Parcelable