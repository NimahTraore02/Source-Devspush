package com.decouikit.news.billing.model

interface BillingContract {
    fun removeAds()
    fun isItemPurchased(): Boolean
    fun purchasedToken(): String
}