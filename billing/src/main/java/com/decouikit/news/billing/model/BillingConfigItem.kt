package com.decouikit.news.billing.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BillingConfigItem(
    val skuItem: String = "com.decouikit.news.android.remove.ads"
) : Parcelable
