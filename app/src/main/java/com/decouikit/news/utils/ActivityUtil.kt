package com.decouikit.news.utils

import android.content.Intent
import android.provider.SyncStateContract
import android.util.Log
import androidx.fragment.app.FragmentActivity

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
}