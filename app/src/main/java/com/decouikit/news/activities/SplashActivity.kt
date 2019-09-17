package com.decouikit.news.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.decouikit.news.R
import com.decouikit.news.activities.common.BaseActivity
import com.decouikit.news.activities.common.NavigationActivity
import com.decouikit.news.interfaces.SyncListener
import com.decouikit.news.network.sync.SyncApi
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : Activity(), SyncListener {
    override fun finish(success: Boolean) {
        // TODO RADOVAN UCITATI URL U ZA SINGLE POST
        Log.e("TEST", "URL$intent.loadDeepLinkUrl()")
        // Open navigation or Single post
        startActivity(Intent(this, NavigationActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        animation()
        MobileAds.initialize(this)
        SyncApi.sync(this)
    }

    private fun animation() {
        ivLogo.alpha = 1.0f
        val anim = AnimationUtils.loadAnimation(this, R.anim.logo_anim)
        ivLogo.startAnimation(anim)
    }
}

