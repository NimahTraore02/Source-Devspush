package com.decouikit.news.utils

import android.content.Intent
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import com.decouikit.news.R

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

    fun setLayoutDirection(activity: FragmentActivity, layoutDirection: Int) {
        ViewCompat.setLayoutDirection(activity.findViewById(R.id.parent), layoutDirection)
    }
}