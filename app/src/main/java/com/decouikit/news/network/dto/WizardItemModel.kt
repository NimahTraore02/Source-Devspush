package com.decouikit.news.network.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class WizardItemModel(
    val title: String?, val subtitle: String?, var image: String?
) : Parcelable