package com.decouikit.news.billing.util

import android.content.Context
import android.content.SharedPreferences

class Preference(context: Context) {

    private var prefs = context.getSharedPreferences("shared_data_billing", Context.MODE_PRIVATE)

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }
    var isItemPurchesed: Boolean
        get() = prefs.getBoolean("IS_ITEM_PURCHASED", false)
        set(value) = prefs.edit { it.putBoolean("IS_ITEM_PURCHASED", value) }

    var purchasedToken: String
        get() = prefs.getString("PURCHASED_TOKEN", "") ?: ""
        set(value) = prefs.edit { it.putString("PURCHASED_TOKEN", value) }
}