package com.decouikit.news.utils

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.View
import com.decouikit.news.R
import com.decouikit.news.activities.NewsApplication
import com.google.android.gms.ads.*
import com.google.android.gms.ads.doubleclick.PublisherAdRequest
import com.google.android.gms.ads.doubleclick.PublisherAdView
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd
import kotlinx.android.synthetic.main.app_bar_main.*

public class AdMob(private val mContext: Context, private var adView: PublisherAdView?) {

    private var mInterstitialAd: PublisherInterstitialAd? = null

    private val adRequest: PublisherAdRequest
        get() = PublisherAdRequest.Builder()
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
        val interstitialId = mContext.getString(R.string.application_ad_id)
        if (!TextUtils.isEmpty(interstitialId)) {
            mInterstitialAd = PublisherInterstitialAd(mContext)
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
        val interstitialId = mContext.getString(R.string.application_ad_id)
        MobileAds.initialize(mContext, interstitialId)
        if (adView != null) {
            val bannerId = mContext.getString(R.string.banner_id)
            adView?.visibility = if (!TextUtils.isEmpty(bannerId)) View.VISIBLE else View.GONE
        }
    }

    fun requestInterstitialAd() {
        (mContext as Activity).runOnUiThread(Runnable {
            if (mInterstitialAd != null) {
                val interstitialId = mContext.getString(R.string.interstitial_id)
                if (!TextUtils.isEmpty(interstitialId) && NewsApplication.isActivityVisible()) {
                    mInterstitialAd?.adUnitId = mContext.getString(R.string.interstitial_id)
                    mInterstitialAd?.loadAd(adRequest)
                }
            }
        })
    }

    fun requestBannerAds() {
        (mContext as Activity).runOnUiThread(Runnable {
            if (adView != null) {
                val bannerId = mContext.getString(R.string.banner_id)
                if (!TextUtils.isEmpty(bannerId)) {
                    adView?.loadAd(adRequest)
                }
            }
        })
    }

    companion object {
        private val TAG = "ADMOB"
    }
}