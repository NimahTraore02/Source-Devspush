package com.decouikit.news.activities.common

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.decouikit.news.R
import com.decouikit.news.database.Preference
import com.decouikit.news.utils.ChangeLanguageUtil
import com.decouikit.news.utils.RateMe
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.doubleclick.PublisherAdRequest
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd
import com.google.gson.Gson
import kotlinx.android.synthetic.main.app_bar_main.*

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    protected val gson by lazy { Gson() }
    protected lateinit var mInterstitialAd: PublisherInterstitialAd
    protected val prefs: Preference by lazy { Preference(this) }

    private val adRequest: PublisherAdRequest
        get() = PublisherAdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .addTestDevice(getString(R.string.test_device_id))
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(Preference(this).colorTheme)
    }

    protected fun getLayoutDirection(): Int {
        return if (prefs.isRtlEnabled)
            ViewCompat.LAYOUT_DIRECTION_RTL
        else
            ViewCompat.LAYOUT_DIRECTION_LOCALE
    }

    protected fun showBannerAds() {
        val bannerId = getString(R.string.banner_id)
        if (bannerId.isNotEmpty()) {
            publisherAdView.visibility = View.VISIBLE
            publisherAdView.loadAd(adRequest)
        } else {
            publisherAdView.visibility = View.GONE
        }
    }

    protected fun showInterstitialAds() {
        val interstitialID = getString(R.string.interstitial_id)
        if (interstitialID.isNotEmpty()) {
            mInterstitialAd = PublisherInterstitialAd(this)
            mInterstitialAd.adUnitId = interstitialID
            mInterstitialAd.loadAd(adRequest)
            mInterstitialAd.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    if (mInterstitialAd.isLoaded) {
                        mInterstitialAd.show()
                    }
                }
            }
        }
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            ChangeLanguageUtil.wrap(newBase, Preference(newBase).languageCode)
        )
    }

    override fun onResume() {
        super.onResume()
        RateMe.rateApp(this)
    }
}