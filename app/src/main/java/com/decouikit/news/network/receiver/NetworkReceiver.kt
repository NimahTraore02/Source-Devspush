package com.decouikit.news.network.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager


class NetworkReceiver(var connectivityReceiverListener: NetworkReceiverListener) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        connectivityReceiverListener.onNetworkConnectionChanged(isConnectedOrConnecting(context))
    }

    private fun isConnectedOrConnecting(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}