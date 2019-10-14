package com.decouikit.news.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.decouikit.news.events.InternetAccess
import com.decouikit.news.utils.NetworkUtil
import org.greenrobot.eventbus.EventBus

class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("TEST", "onReceive:" + NetworkUtil.isNetworkConnected(context))
        EventBus.getDefault().post(InternetAccess(NetworkUtil.isNetworkConnected(context)))
    }
}