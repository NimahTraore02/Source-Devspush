package com.decouikit.advertising.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AdsConfigItem(
    var bannerAdUnitId: String = "",
    var interstitialAdUnitId: String = "",
    var rewardedVideoAdUnitId: String = "",
    var testDeviceId: String = ""
) : Parcelable
