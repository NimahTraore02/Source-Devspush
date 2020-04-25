package com.decouikit.advertising.model

import android.content.Context
import android.view.ViewGroup

interface AdsContract {
    fun showBanner()
    fun removeBanner()
    fun showInterstitial()
    fun removeInterstitial()
    fun showRewardedVideo()
    fun pauseRewardedVideo()
    fun resumeRewardedVideo()
    fun destroyRewardedVideo()
    fun getContext(): Context?
    fun getContainer(): ViewGroup?
}