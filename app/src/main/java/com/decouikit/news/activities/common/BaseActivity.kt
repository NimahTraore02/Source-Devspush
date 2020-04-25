package com.decouikit.news.activities.common

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.decouikit.advertising.model.AdEventListener
import com.decouikit.news.database.Config
import com.decouikit.news.database.Preference
import com.decouikit.news.utils.ChangeLanguageUtil
import com.decouikit.news.utils.RateMe
import com.google.gson.Gson

@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity(), AdEventListener {

    protected val gson by lazy { Gson() }

    private val advertising by lazy { Config.getAdsProvider(getAdsContainer(), this) }

    protected val prefs: Preference by lazy { Preference(this) }

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
        Log.e("Test" , "showBannerAds")
        advertising.showBanner()

    }

    protected fun showInterstitialAds() {
        Log.e("Test" , "showBannerAds")
        advertising.showInterstitial()
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

    override fun onEventInterstitialAdLoaded() {

    }

    override fun onEventInterstitialAdOpened() {

    }

    override fun onEventInterstitialAdLeftApplication() {

    }

    override fun onEventInterstitialAdClosed() {

    }

    override fun onEventRewardedVideoAdLeftApplication() {

    }

    override fun onEventInterstitialAdFailedToLoad(errorCode: Int) {

    }

    override fun onEventRewardedVideoAdLoaded() {

    }

    override fun onEventRewardedVideoAdClosed() {

    }

    override fun onEventRewardedVideoAdOpen() {

    }

    override fun onEventRewardedVideoCompleted() {

    }

    override fun onEventRewarded(type: String?, amount: Int?) {

    }

    override fun onEventRewardedVideoStarted() {

    }

    override fun onEventRewardedVideoAdFailedToLoad(errorCode: Int) {

    }

    open fun getAdsContainer(): ViewGroup? = null

    open fun isRunning(): Boolean = false
}