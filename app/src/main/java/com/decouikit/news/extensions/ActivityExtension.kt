package com.decouikit.news.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.decouikit.news.activities.PostActivity
import com.decouikit.news.activities.ViewAllActivity
import com.decouikit.news.activities.WizardActivity
import com.decouikit.news.database.Config
import com.decouikit.news.database.Preference
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.utils.NewsConstants
import com.google.gson.Gson

fun Activity.openMainPage() {
        Preference(this).isIntroPageShown = true
        this.startActivity(Intent(this, Config.getDefaultNavigationStyle()))
        this.finish()
}

fun Activity.openIntroPage() {
    this.startActivity(Intent(this, WizardActivity::class.java))
    this.finish()
}

fun Activity.openSinglePage(postItem: PostItem) {
    val intentMain = Intent(this, Config.getDefaultNavigationStyle())
    val intentPost = Intent(this, PostActivity::class.java)
    intentPost.putExtra(NewsConstants.POST_ITEM, Gson().toJson(postItem))
    startActivities(arrayOf(intentMain, intentPost))
    finish()
}

fun Activity.hideSoftKeyboard() {
    val inputMethodManager = this.getSystemService(
        Activity.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(
        this.currentFocus?.windowToken, 0
    )
}

fun Activity.viewAll(context: Context, categoryId: Int?, categoryName: String, categoryType: Int? = 0) {
    val intent = Intent(context, ViewAllActivity::class.java)
    intent.putExtra(NewsConstants.CATEGORY_ID, categoryId)
    intent.putExtra(NewsConstants.CATEGORY_NAME, categoryName)
    intent.putExtra(NewsConstants.CATEGORY_FEATURES, categoryType)
    context.startActivity(intent)
}