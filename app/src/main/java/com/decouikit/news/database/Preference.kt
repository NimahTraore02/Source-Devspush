package com.decouikit.news.database

import android.content.Context
import android.content.SharedPreferences
import com.decouikit.news.R

class Preference(context: Context) {

    private var prefs = context.getSharedPreferences("shared_data", Context.MODE_PRIVATE)

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var colorTheme: Int
        get() = prefs.getInt("THEME_KEY", R.style.AppTheme)
        set(value) = prefs.edit { it.putInt("THEME_KEY", value) }

    var aboutInformation: String
        get() = prefs.getString("ABOUT_INFORMATION", "") ?: ""
        set(value) = prefs.edit { it.putString("ABOUT_INFORMATION", value) }


    var isPushNotificationEnabled: Boolean
        get() = prefs.getBoolean("IS_PUSH_NOTIFICATION_ENABLED", false)
        set(value) = prefs.edit { it.putBoolean("IS_PUSH_NOTIFICATION_ENABLED", value) }

    var isLightColorSelected: Boolean
        get() = prefs.getBoolean("IS_LIGHT_COLOR_SELECTED", false)
        set(value) = prefs.edit { it.putBoolean("IS_LIGHT_COLOR_SELECTED", value) }

}