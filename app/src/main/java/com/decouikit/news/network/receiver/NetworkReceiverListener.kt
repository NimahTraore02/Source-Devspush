package com.decouikit.news.network.receiver

interface NetworkReceiverListener {
    fun onNetworkConnectionChanged(isConnected: Boolean)
}