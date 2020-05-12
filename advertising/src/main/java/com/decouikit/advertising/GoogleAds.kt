package com.decouikit.advertising

import android.content.Context
import android.view.ViewGroup
import com.decouikit.advertising.model.AdEventListener
import com.decouikit.advertising.model.AdsConfigItem
import com.decouikit.advertising.model.AdsContract

import com.google.android.gms.ads.*
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener

class GoogleAds(
    private val adsItem: AdsConfigItem,
    private val adsContainer: ViewGroup?,
    private val listener: AdEventListener?
) : AdsContract {

    private var enabledAds: Boolean = true
    private var mRewardedVideoAd: RewardedVideoAd? = null
    private var adView: AdView? = null

    init {
        MobileAds.initialize(adsContainer?.context)
    }

    override fun showBanner() {
        if (!enabledAds) {
            return
        }
        if (adsItem.bannerAdUnitId.isEmpty()) {
            return
        }

        if (adView == null) {
            adView = AdView(getContext())
            adView?.adSize = AdSize.SMART_BANNER
            adView?.adUnitId = adsItem.bannerAdUnitId
            adsContainer?.addView(adView)
        }

        adView?.loadAd(AdRequest.Builder().build())
    }

    override fun removeBanner() {
        if (adView != null) {
            adsContainer?.removeView(adView)
            adView = null
        }
    }

    override fun showInterstitial() {
        if (!enabledAds) {
            return
        }
        if (adsItem.interstitialAdUnitId.isEmpty()) {
            return
        }
        val mInterstitialAd = InterstitialAd(getContext())
        mInterstitialAd.adUnitId = adsItem.interstitialAdUnitId
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                listener?.onEventInterstitialAdLoaded()
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                }
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Code to be executed when an ad request fails.
                listener?.onEventInterstitialAdFailedToLoad(errorCode)
            }

            override fun onAdOpened() {
                // Code to be executed when the ad is displayed.
                listener?.onEventInterstitialAdOpened()
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                listener?.onEventInterstitialAdLeftApplication()
            }

            override fun onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                listener?.onEventInterstitialAdClosed()
            }
        }
    }

    override fun removeInterstitial() {
        //DO NOT NEED THIS METHOD
    }

    override fun showRewardedVideo() {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext())
        mRewardedVideoAd?.rewardedVideoAdListener = object : RewardedVideoAdListener {
            override fun onRewardedVideoAdClosed() {
                listener?.onEventRewardedVideoAdClosed()

            }

            override fun onRewardedVideoAdLeftApplication() {
                listener?.onEventRewardedVideoAdLeftApplication()
            }

            override fun onRewardedVideoAdLoaded() {
                listener?.onEventRewardedVideoAdLoaded()
                if (mRewardedVideoAd?.isLoaded == true) {
                    mRewardedVideoAd?.show()
                }
            }

            override fun onRewardedVideoAdOpened() {
                listener?.onEventRewardedVideoAdOpen()
            }

            override fun onRewardedVideoCompleted() {
                listener?.onEventRewardedVideoCompleted()
            }

            override fun onRewarded(item: RewardItem?) {
                mRewardedVideoAd?.destroy(getContext())
                listener?.onEventRewarded(item?.type, item?.amount ?: 0)
            }

            override fun onRewardedVideoStarted() {
                listener?.onEventRewardedVideoStarted()
            }

            override fun onRewardedVideoAdFailedToLoad(errorCode: Int) {
                mRewardedVideoAd?.destroy(getContext())
                listener?.onEventRewardedVideoAdFailedToLoad(errorCode)
            }
        }
        mRewardedVideoAd?.loadAd(adsItem.rewardedVideoAdUnitId, AdRequest.Builder().build())
    }

    override fun pauseRewardedVideo() {
        mRewardedVideoAd?.pause(getContext())
    }

    override fun resumeRewardedVideo() {
        mRewardedVideoAd?.resume(getContext())
    }

    override fun destroyRewardedVideo() {
        mRewardedVideoAd?.destroy(getContext())
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