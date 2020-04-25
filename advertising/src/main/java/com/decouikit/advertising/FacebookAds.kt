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

    private var adView: AdView? = null
    private var mInterstitialAd: InterstitialAd? = null
    private var mRewardedVideoAd: RewardedVideoAd? = null

    init {
        if (!AudienceNetworkAds.isInitialized(getContext())) {
            AudienceNetworkAds
                .buildInitSettings(getContext())
                .withInitListener {
                    Log.e("Test", "withInitListener")
                }
                .initialize()
            Log.e("Test", "init")
            AdSettings.addTestDevice(adsItem.testDeviceId)
        }
    }

    override fun showBanner() {
        if (adsItem.bannerAdUnitId.isEmpty()) {
            return
        }
        Log.e("Test", "showBanner")
        adView = AdView(getContext(), adsItem.bannerAdUnitId, AdSize.BANNER_HEIGHT_50)
        adsContainer?.addView(adView)
        val loadAdConfig = adView
            ?.buildLoadAdConfig()
            ?.withAdListener(object : AdListener{
                override fun onAdClicked(p0: Ad?) {
                    Log.e("Test", "onAdClicked")
                }

                override fun onError(p0: Ad?, p1: AdError?) {
                    Log.e("Test", "onError:" + p1?.errorMessage?:"error")
                    Log.e("Test", "onError:" + p1?.errorCode?:"-1")
                }

                override fun onAdLoaded(p0: Ad?) {
                    Log.e("Test", "onAdLoaded")
                }

                override fun onLoggingImpression(p0: Ad?) {
                    Log.e("Test", "onLoggingImpression")
                }
            })
            ?.build()
        adView?.loadAd()
    }

    override fun removeBanner() {
        adView?.destroy()
    }

    override fun showInterstitial() {
        if (adsItem.interstitialAdUnitId.isEmpty()) {
            return
        }
        Log.e("Test", "interstitialAdUnitId")
        mInterstitialAd = InterstitialAd(getContext(), adsItem.interstitialAdUnitId)
        val loadAdConfig = mInterstitialAd
            ?.buildLoadAdConfig()
            ?.withAdListener(object : InterstitialAdListener {
                override fun onInterstitialDisplayed(p0: Ad?) {
                    Log.e("Test", "onInterstitialDisplayed")
                    listener?.onEventInterstitialAdOpened()
                }

                override fun onAdClicked(p0: Ad?) {
                    Log.e("Test", "onAdClicked")
                    listener?.onEventInterstitialAdOpened()
                }

                override fun onInterstitialDismissed(p0: Ad?) {
                    Log.e("Test", "onInterstitialDismissed")
                    listener?.onEventInterstitialAdClosed()
                }

                override fun onError(p0: Ad?, p1: AdError?) {
                    Log.e("Test", "interstitialAdUnitId")
                    listener?.onEventInterstitialAdFailedToLoad(p1?.errorCode?:-1)
                }

                override fun onAdLoaded(p0: Ad?) {
                    Log.e("Test", "onAdLoaded")
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
        Log.e("Test", "loadAdConfig")
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
}