package com.decouikit.news.extensions

import android.app.Activity
import android.content.Intent
import com.decouikit.news.activities.PostActivity
import com.decouikit.news.database.Config
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.utils.NewsConstants
import com.google.gson.Gson

fun Activity.openMainPage() {
    this.startActivity(Intent(this, Config.getDefaultNavigationStyle()))
    this.finish()
}

fun Activity.openSinglePage(postItem: PostItem) {
    val intentMain = Intent(this, Config.getDefaultNavigationStyle())
    val intentPost = Intent(this, PostActivity::class.java)
    intentPost.putExtra(NewsConstants.POST_ITEM, Gson().toJson(postItem))
    startActivities(arrayOf(intentMain, intentPost))
    finish()
}