package com.decouikit.news.network.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class CommentRequest(
    @SerializedName("post") val post: Int,
    @SerializedName("content") val content: String,
    @SerializedName("author_name") val authorName: String,
    @SerializedName("author_email") val authorEmail: String,
    @SerializedName("author_url") val authorUrl: String
) : Parcelable