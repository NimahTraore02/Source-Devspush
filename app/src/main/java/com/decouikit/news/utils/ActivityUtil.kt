package com.decouikit.news.utils

import android.app.Activity
import android.content.Intent
import android.view.Menu
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import com.decouikit.news.R
import com.google.android.material.appbar.AppBarLayout

object ActivityUtil {

    fun reload(activity: FragmentActivity?, fragmentPosition: Int? = -1) {
        val intent = activity?.intent
        activity?.overridePendingTransition(0, 0)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent?.putExtra(NewsConstants.FRAGMENT_POSITION, fragmentPosition)
        activity?.finish()
        activity?.overridePendingTransition(0, 0)
        activity?.startActivity(intent)
    }

    fun setLayoutDirection(activity: FragmentActivity, layoutDirection: Int, parent: Int) {
        ViewCompat.setLayoutDirection(activity.findViewById(parent), layoutDirection)
    }

    fun setAppBarElevation(appBar: AppBarLayout, elevation: Float) {
        appBar.elevation = elevation
    }

    fun setSearchVisibility(menuItem: Menu, isVisible: Boolean) {
        menuItem.findItem(R.id.action_search).isVisible = isVisible
    }
}