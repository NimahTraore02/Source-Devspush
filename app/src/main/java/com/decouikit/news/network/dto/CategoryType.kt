package com.decouikit.news.network.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class CategoryType : Parcelable {

    ALL, FEATURED, RECENT;

    companion object {
        fun getType(type: Int): CategoryType {
            return when (type) {
                0 -> ALL
                1 -> FEATURED
                2 -> RECENT
                else -> ALL
            }
        }
    }
}