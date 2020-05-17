package com.decouikit.news.billing

import android.app.Activity
import android.util.Log
import com.android.billingclient.api.*
import com.decouikit.news.billing.model.BillingConfigItem
import com.decouikit.news.billing.model.BillingContract
import com.decouikit.news.billing.model.BillingEventListener
import com.decouikit.news.billing.util.Preference

class GooglePlayBilling(
    val activity: Activity,
    val config: BillingConfigItem,
    private val listenerBilling: BillingEventListener
) : BillingContract {
    private val TAG = GooglePlayBilling::class.qualifiedName

    private var mBillingClient: BillingClient
    private var mPreference: Preference = Preference(activity)

    private var removeAdsSkuDetails: SkuDetails? = null

    init {

        mBillingClient = BillingClient.newBuilder(activity)
            .setListener { billingResult, purchaseList ->
                run {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchaseList != null) {
                        handlePayment(purchaseList[0])
                    }
                }
            }
            .enablePendingPurchases()
            .build()

        mBillingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                Log.e(TAG, "onBillingSetupFinished")
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    getPurchaseItems()
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.e(TAG, "onBillingServiceDisconnected")
            }
        })
    }


    private fun handlePayment(purchase: Purchase?) {
        if (purchase != null && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            mPreference.isItemPurchased = true
            mPreference.purchasedToken = purchase.purchaseToken
            listenerBilling.purchasesSuccess()
            if (!purchase.isAcknowledged) {
                getAcknowledgePurchase(purchase.purchaseToken)
            }
        }
    }

    private fun getAcknowledgePurchase(token: String) {
        if (token.isNotEmpty()) {
            val params = AcknowledgePurchaseParams.newBuilder()
            params.setPurchaseToken(token)
            mBillingClient.acknowledgePurchase(params.build()) { }
        }
    }

    fun getPurchaseItems() {
        Log.e(TAG, "getPurchaseItems")
        val skuList: MutableList<String> = mutableListOf(config.skuItem)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        mBillingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            if (
                billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                skuDetailsList != null && skuDetailsList.size > 0
            ) {
                removeAdsSkuDetails = skuDetailsList[0]
            }
        }
    }

    override fun removeAds() {
        if (removeAdsSkuDetails != null) {
            val flowParams = BillingFlowParams
                .newBuilder()
                .setSkuDetails(removeAdsSkuDetails)
                .build()
            mBillingClient.launchBillingFlow(activity, flowParams)
        } else {
            Log.e(TAG, "RemoveAdsSkuDetails is null")
        }
    }

    override fun isItemPurchased(): Boolean {
        return mPreference.isItemPurchased
    }

    override fun purchasedToken(): String {
        return mPreference.purchasedToken
    }
}