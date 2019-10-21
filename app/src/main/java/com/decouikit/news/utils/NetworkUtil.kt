package com.decouikit.news.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.net.ConnectivityManagerCompat

object NetworkUtil {
    fun isActiveNetworkMetered(context: Context?): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null && ConnectivityManagerCompat.isActiveNetworkMetered(cm as ConnectivityManager))
    }
}