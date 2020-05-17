package com.decouikit.advertising

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import com.decouikit.advertising.model.AdEventListener
import com.decouikit.advertising.model.AdsConfigItem
import com.decouikit.advertising.model.AdsContract
import com.facebook.ads.*


class FacebookAds(
    private val adsItem: AdsConfigItem,
    private val adsContainer: ViewGroup?,
    private val listener: AdEventListener?
) : AdsContract {

    companion object {
        var enabledAds: Boolean = true
    }

    private var adView: AdView? = null
    private var mInterstitialAd: InterstitialAd? = null
    private var mRewardedVideoAd: RewardedVideoAd? = null

    init {
        if (!AudienceNetworkAds.isInitialized(getContext())) {
            AudienceNetworkAds
                .buildInitSettings(getContext())
                .withInitListener { }
                .initialize()
            AudienceNetworkAds.initialize(getContext())
            AdSettings.addTestDevice(adsItem.testDeviceId)
        }
    }

    override fun showBanner() {
        if (!enabledAds) {
            return
        }
        if (adsItem.bannerAdUnitId.isEmpty()) {
            return
        }
        adView = AdView(getContext(), adsItem.bannerAdUnitId, AdSize.BANNER_HEIGHT_50)
        adsContainer?.addView(adView)
        val loadAdConfig = adView
            ?.buildLoadAdConfig()
            ?.build()
        adView?.loadAd(loadAdConfig)
    }

    override fun removeBanner() {
        adView?.destroy()
    }

    override fun showInterstitial() {
        if (!enabledAds) {
            return
        }
        if (adsItem.interstitialAdUnitId.isEmpty()) {
            return
        }
        mInterstitialAd = InterstitialAd(getContext(), adsItem.interstitialAdUnitId)
        val loadAdConfig = mInterstitialAd
            ?.buildLoadAdConfig()
            ?.withAdListener(object : InterstitialAdListener {
                override fun onInterstitialDisplayed(p0: Ad?) {
                    listener?.onEventInterstitialAdOpened()
                }

                override fun onAdClicked(p0: Ad?) {
                    listener?.onEventInterstitialAdOpened()
                }

                override fun onInterstitialDismissed(p0: Ad?) {
                    listener?.onEventInterstitialAdClosed()
                }

                override fun onError(p0: Ad?, p1: AdError?) {
                    listener?.onEventInterstitialAdFailedToLoad(p1?.errorCode?:-1)
                }

                override fun onAdLoaded(p0: Ad?) {
                    listener?.onEventInterstitialAdLoaded()
                    if (mInterstitialAd?.isAdLoaded == true) {
                        mInterstitialAd?.show()
                    }
                }

                override fun onLoggingImpression(p0: Ad?) {
                    listener?.onEventInterstitialAdOpened()
                }
            })
            ?.build()
        mInterstitialAd?.loadAd(loadAdConfig)
    }

    override fun removeInterstitial() {
        mInterstitialAd?.destroy()
    }

    override fun showRewardedVideo() {
        mRewardedVideoAd = RewardedVideoAd(getContext(), adsItem.rewardedVideoAdUnitId)
        val loadAdConfig = mRewardedVideoAd
            ?.buildLoadAdConfig()
            ?.withAdListener(object : RewardedVideoAdListener{
                override fun onRewardedVideoClosed() {
                    listener?.onEventRewardedVideoAdClosed()
                }

                override fun onAdClicked(p0: Ad?) {
                    listener?.onEventRewardedVideoAdOpen()
                }

                override fun onRewardedVideoCompleted() {
                    listener?.onEventRewardedVideoCompleted()
                }

                override fun onError(p0: Ad?, p1: AdError?) {
                    listener?.onEventRewardedVideoAdFailedToLoad(p1?.errorCode?:-1)
                }

                override fun onAdLoaded(p0: Ad?) {
                    listener?.onEventRewardedVideoAdLoaded()
                    if (mRewardedVideoAd?.isAdLoaded == true) {
                        mRewardedVideoAd?.show()
                    }
                }

                override fun onLoggingImpression(p0: Ad?) {
                    listener?.onEventRewarded("", 0)
                }

            })
            ?.build()
        mRewardedVideoAd?.loadAd(loadAdConfig)
    }

    override fun pauseRewardedVideo() {

    }

    override fun resumeRewardedVideo() {
    }

    override fun destroyRewardedVideo() {
        mRewardedVideoAd?.destroy()
    }

    override fun getContext(): Context? {
        return adsContainer?.context
    }

    override fun getContainer(): ViewGroup? {
        return adsContainer
    }

    override fun enabledAds(enabled: Boolean) {
        enabledAds = enabled
    }
}