package com.decouikit.news.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.decouikit.news.R
import com.decouikit.news.activities.common.BaseActivity
import com.decouikit.news.activities.common.NavigationActivity
import com.decouikit.news.interfaces.SyncListener
import com.decouikit.news.network.sync.SyncApi

class SplashActivity : BaseActivity(), SyncListener {
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
        SyncApi.sync(this)
    }
}

