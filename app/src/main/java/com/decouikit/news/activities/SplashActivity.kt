package com.decouikit.news.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.decouikit.news.R
import com.decouikit.news.activities.base.MainActivity

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000L)
    }
}