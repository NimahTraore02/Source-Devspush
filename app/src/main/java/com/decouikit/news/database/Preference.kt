package com.decouikit.news.database

import android.content.Context
import android.content.SharedPreferences
import com.decouikit.news.network.dto.MediaItem
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.network.dto.Tag
import com.decouikit.news.network.dto.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Preference(context: Context) {

    private var prefs = context.getSharedPreferences("shared_data", Context.MODE_PRIVATE)

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var colorTheme: Int
        get() = prefs.getInt("THEME_KEY", Config.getDefaultTheme())
        set(value) = prefs.edit { it.putInt("THEME_KEY", value) }

    var interstitialAdCounter: Int
        get() = prefs.getInt("INTERSTITIAL_AD_COUNTER", 0)
        set(value) = prefs.edit { it.putInt("INTERSTITIAL_AD_COUNTER", value) }

    var aboutInformation: String
        get() = prefs.getString("ABOUT_INFORMATION", "") ?: ""
        set(value) = prefs.edit { it.putString("ABOUT_INFORMATION", value) }

    var isPushNotificationEnabled: Boolean
        get() = prefs.getBoolean(
            "IS_PUSH_NOTIFICATION_ENABLED",
            Config.getDefaultValueForPushNotification()
        )
        set(value) = prefs.edit { it.putBoolean("IS_PUSH_NOTIFICATION_ENABLED", value) }

    var isRtlEnabled: Boolean
        get() = prefs.getBoolean("RTL_ENABLED", Config.getDefaultValueForRTL())
        set(value) = prefs.edit { it.putBoolean("RTL_ENABLED", value) }

    var languageCode: String
        get() = prefs.getString("LANGUAGE_CODE", Config.getDefaultValueForLanguage())
            ?: Config.getDefaultValueForLanguage()
        set(value) = prefs.edit { it.putString("LANGUAGE_CODE", value) }

    var isIntroPageShown: Boolean
        get() = prefs.getBoolean("INTRO_PAGE", false)
        set(value) = prefs.edit { it.putBoolean("INTRO_PAGE", value) }

    fun isThemeLight(): Boolean {
        return colorTheme == Config.getDefaultTheme()
    }
//
//    fun setBookmarkedNews(items: ArrayList<PostItem>) {
//        val gson = Gson()
//        val json = gson.toJson(items)
//        prefs.edit().putString("BOOKMARKED_NEWS", json).apply()
//    }
//
//    fun getBookmarkedNews(): ArrayList<PostItem> {
//        val gson = Gson()
//        val json = prefs.getString("BOOKMARKED_NEWS", arrayListOf<PostItem>().toString())
//        if (json.isNullOrEmpty()) {
//            return arrayListOf()
//        }
//        val type = object : TypeToken<List<PostItem>>() {}.type
//        return gson.fromJson(json, type)
//    }
//
//    fun addBookmark(item: PostItem) {
//        val result = getBookmarkedNews()
//        result.add(item)
//        setBookmarkedNews(result)
//    }
//
//    fun removeBookmark(item: PostItem) {
//        val result = getBookmarkedNews()
//        result.remove(item)
//        setBookmarkedNews(result)
//    }
//
//    fun getBookmarkByPostId(id: Int): PostItem? {
//        for (bookmarkedItem in getBookmarkedNews()) {
//            if (bookmarkedItem.id == id) {
//                return bookmarkedItem
//            }
//        }
//        return null
//    }

    var tabPosition: Int
        get() = prefs.getInt("TAB_POSITION", 0)
        set(value) = prefs.edit { it.putInt("TAB_POSITION", value) }

    fun persistBookmark(items: ArrayList<PostItem>) {
        prefs.edit().putString("BOOKMARKED_NEWS", Gson().toJson(items)).apply()
    }

    fun loadBookmark(): ArrayList<PostItem> {
        val json = prefs.getString("BOOKMARKED_NEWS", arrayListOf<PostItem>().toString())
        if (json.isNullOrEmpty()) {
            return arrayListOf()
        }
        val type = object : TypeToken<List<PostItem>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun persistTags(items: ArrayList<Tag>) {
        prefs.edit().putString("TAGS", Gson().toJson(items)).apply()
    }

    fun loadTags(): ArrayList<Tag> {
        val json = prefs.getString("TAGS", arrayListOf<Tag>().toString())
        if (json.isNullOrEmpty()) {
            return arrayListOf()
        }
        val type = object : TypeToken<List<Tag>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun persistUsers(items: ArrayList<User>) {
        prefs.edit().putString("USERS", Gson().toJson(items)).apply()
    }

    fun loadUsers(): ArrayList<User> {
        val json = prefs.getString("USERS", arrayListOf<User>().toString())
        if (json.isNullOrEmpty()) {
            return arrayListOf()
        }
        val type = object : TypeToken<List<User>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun loadNotificationPosts(): ArrayList<PostItem> {
        val json = prefs.getString("NOTIFICATION_POSTS", arrayListOf<PostItem>().toString())
        if (json.isNullOrEmpty()) {
            return arrayListOf()
        }
        val type = object : TypeToken<List<PostItem>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun persisNotificationPosts(items: ArrayList<PostItem>) {
        prefs.edit().putString("NOTIFICATION_POSTS", Gson().toJson(items)).apply()
    }

    fun loadMedia(): ArrayList<MediaItem> {
        val json = prefs.getString("MEDIA", arrayListOf<MediaItem>().toString())
        if (json.isNullOrEmpty()) {
            return arrayListOf()
        }
        val type = object : TypeToken<List<MediaItem>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun persistMedia(items: ArrayList<MediaItem>) {
        prefs.edit().putString("MEDIA", Gson().toJson(items)).apply()
    }

}