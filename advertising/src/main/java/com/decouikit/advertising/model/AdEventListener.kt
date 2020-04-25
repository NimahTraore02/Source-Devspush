package com.decouikit.advertising.model

interface AdEventListener {
    fun onEventInterstitialAdLoaded()
    fun onEventInterstitialAdOpened()
    fun onEventInterstitialAdLeftApplication()
    fun onEventInterstitialAdClosed()
    fun onEventInterstitialAdFailedToLoad(errorCode: Int)

    fun onEventRewardedVideoAdLeftApplication()
    fun onEventRewardedVideoAdLoaded()
    fun onEventRewardedVideoAdClosed()
    fun onEventRewardedVideoAdOpen()
    fun onEventRewardedVideoCompleted()
    fun onEventRewarded(type: String?, amount: Int?)
    fun onEventRewardedVideoStarted()
    fun onEventRewardedVideoAdFailedToLoad(errorCode: Int)
}