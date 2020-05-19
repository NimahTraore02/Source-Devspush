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
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.network.sync.SyncCategory
import com.decouikit.news.network.sync.SyncPost
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        animation()
        InMemory.clear()
        GlobalScope.launch(Dispatchers.IO) {
            SyncCategory.sync(applicationContext)
            var postItem: PostItem? = null
            if (intent.loadDeepLinkUrl().isNotEmpty()) {
                postItem = SyncPost.getPostById(applicationContext, intent.loadDeepLinkUrl())
            }
            openApp(postItem)
        }
    }

    private fun openApp(postItem: PostItem?) {
        if (postItem != null) {
            openSinglePage(postItem)
        } else {
            if (!Preference(applicationContext).isIntroPageShown && Config.isIntroPageEnabled()) {
                openIntroPage()
            } else {
                openMainPage()
            }
        }
    }

    private fun animation() {
        ivLogo.alpha = 1.0f
        val anim = AnimationUtils.loadAnimation(this, R.anim.logo_anim)
        ivLogo.startAnimation(anim)
    }
}