package com.decouikit.news.network.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
    @SerializedName("description") val description: String,
    @SerializedName("link") val link: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("avatar_urls") val avatarUrls: AvatarUrl
) : Parcelable