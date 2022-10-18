package com.decouikit.news.network.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class MediaItem(
    @SerializedName("id") val id: Int,
    @SerializedName("date") val date: String,
    @SerializedName("date_gmt") val date_gmt: String,

    @SerializedName("modified") val modified: String,
    @SerializedName("modified_gmt") val modified_gmt: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("status") val status: String,
    @SerializedName("type") val type: String,
    @SerializedName("link") val link: String,
    @SerializedName("author") val author: Int,
    @SerializedName("comment_status") val comment_status: String,
    @SerializedName("ping_status") val ping_status: String,
    @SerializedName("template") val template: String,

    @SerializedName("alt_text") val alt_text: String,
    @SerializedName("media_type") val media_type: String,
    @SerializedName("mime_type") val mime_type: String,
    @SerializedName("post") val post: String,
    @SerializedName("source_url") val source_url: String
) : Parcelable