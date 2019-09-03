package com.decouikit.news.utils

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.View
import com.decouikit.news.activities.NewsApplication
import com.google.android.gms.ads.*

public class AdMob(private val mContext: Context, private var adView: AdView?) {

    private var mInterstitialAd: InterstitialAd? = null

    private val adRequest: AdRequest
        get() = AdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .build()

    fun destroy() {
        mInterstitialAd = null
        adView = null
    }

    init {
        initBannerAds()
        initInterstitialAd()
    }

    private fun initInterstitialAd() {
        val interstitialId = "ca-app-pub-9107925753247780~3957871943"
        if (!TextUtils.isEmpty(interstitialId)) {
            mInterstitialAd = InterstitialAd(mContext)
            mInterstitialAd?.adUnitId = interstitialId
            mInterstitialAd?.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    if (mInterstitialAd?.isLoaded == true) {
                        mInterstitialAd?.show()
                    }
                }
            }
        }
    }

    private fun initBannerAds() {
        MobileAds.initialize(mContext, "ca-app-pub-9107925753247780~3957871943")
        if (adView != null) {
            val bannerId = "ca-app-pub-9107925753247780~3957871943"
            adView?.visibility = if (!TextUtils.isEmpty(bannerId)) View.VISIBLE else View.GONE
        }
    }

    fun requestInterstitialAd() {
        (mContext as Activity).runOnUiThread(Runnable {
            if (mInterstitialAd != null) {
                val interstitialId = "ca-app-pub-9107925753247780~3957871943"
                if (!TextUtils.isEmpty(interstitialId) && NewsApplication.isActivityVisible()) {
                    mInterstitialAd!!.loadAd(adRequest)
                }
            }
        })
    }

    fun requestBannerAds() {
        (mContext as Activity).runOnUiThread(Runnable {
            if (adView != null) {
                val bannerId = "ca-app-pub-9107925753247780~3957871943"
                if (!TextUtils.isEmpty(bannerId)) {
                    adView!!.loadAd(adRequest)
                }
            }
        })

    }

    companion object {
        private val TAG = "ADMOB"
    }
}