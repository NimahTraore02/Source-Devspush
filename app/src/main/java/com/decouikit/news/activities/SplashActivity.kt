package com.decouikit.news.activities

import android.app.Activity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.decouikit.news.R
import com.decouikit.news.database.Config
import com.decouikit.news.database.InMemory
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.loadDeepLinkUrl
import com.decouikit.news.extensions.openIntroPage
import com.decouikit.news.extensions.openMainPage
import com.decouikit.news.extensions.openSinglePage
import com.decouikit.news.interfaces.ResultListener
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.network.sync.SyncApi
import com.decouikit.news.network.sync.SyncPost
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : Activity(), ResultListener<Boolean> {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        animation()
        MobileAds.initialize(this)
        InMemory.clear()
        SyncApi.sync(this, this)
    }

    override fun onResult(value: Boolean?) {
        SyncPost.getPostById(this, intent.loadDeepLinkUrl(), object : ResultListener<PostItem> {
            override fun onResult(value: PostItem?) {
                if (value != null) {
                    openSinglePage(value)
                } else {
                    if (!Preference(applicationContext).isIntroPageShown && Config.isIntroPageEnabled()) {
                        openIntroPage()
                    } else {
                        openMainPage()
                    }
                }
            }
        })
    }

    private fun animation() {
        ivLogo.alpha = 1.0f
        val anim = AnimationUtils.loadAnimation(this, R.anim.logo_anim)
        ivLogo.startAnimation(anim)
    }
}