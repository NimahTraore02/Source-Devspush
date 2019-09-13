package com.decouikit.news.activities.common

import android.app.Activity
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import com.decouikit.news.database.Preference
import com.decouikit.news.utils.ActivityUtil
import com.google.gson.Gson

open class BaseActivity : Activity() {

    val gson by lazy {
        Gson()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(Preference(this).colorTheme)
    }
}