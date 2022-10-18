package com.decouikit.news.network.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Language(val baseUrl: String, val language: String, val languageCode: String) : Parcelable