package com.decouikit.news.database

import android.content.Context
import com.decouikit.news.R
import com.decouikit.news.activities.common.BottomNavigationActivity
import com.decouikit.news.activities.common.NavigationActivity
import com.decouikit.news.network.dto.Category

class Config {
    companion object {

        fun getBaseUrl(): String {
            return "https://deconews.decouikit.com/wp-json/wp/v2/"
        }

        fun getDefaultTheme(): Int {
            //Set default theme light or dark
            return getLightTheme()
        }

        fun getLightTheme():Int {
            return R.style.AppTheme
        }

        fun getDarkTheme():Int {
            return R.style.AppThemeDark
        }

        fun getDefaultNavigationStyle(): Class<*> {
            return drawerNavigationStyle()
        }

        fun bottomNavigationStyle(): Class<*> {
            return BottomNavigationActivity::class.java
        }

        fun drawerNavigationStyle(): Class<*> {
            return NavigationActivity::class.java
        }
        //Show interstitial after 10 opens single post
        // if you don't want interstitial ads just put value -1
        fun promptForInterstitialCounter(): Int {
            return 10
        }

        fun getNumberOfItemPerPage(): Int {
            return 10
        }

        fun getNumberOfItemForSlider(): Int {
            return 3
        }

        fun getDefaultValueForPushNotification(): Boolean {
            return true
        }

        fun getDefaultValueForRTL(): Boolean {
            return false
        }

        fun getFacebookUrl(): String {
            return "https://www.facebook.com"
        }

        fun getTwitterUrl(): String {
            return "https://twitter.com"
        }

        fun getYoutubeUrl(): String {
            return "https://www.youtube.com/"
        }

        fun getInstagramUrl(): String {
            return "https://www.instagram.com"
        }
        
        //If you want to exclude some category from list add that category in this array
        private fun getListOfExcludedCategories(): ArrayList<String> {
            val items = arrayListOf<String>()
//            items.add("Category name")
//            items.add("Architecture")
            return items
        }

        fun isCategoryExcluded(category: Category): Boolean {
            return getListOfExcludedCategories().contains(category.name)
        }

        fun getAboutList(context: Context?): ArrayList<String> {
            val items = arrayListOf<String>()
            context?.getString(R.string.about_text_2)?.let { items.add(it) }
            context?.getString(R.string.about_text_3)?.let { items.add(it) }
            context?.getString(R.string.about_text_4)?.let { items.add(it) }
            context?.getString(R.string.about_text_5)?.let { items.add(it) }
            context?.getString(R.string.about_text_6)?.let { items.add(it) }
            context?.getString(R.string.about_text_7)?.let { items.add(it) }
            context?.getString(R.string.about_text_8)?.let { items.add(it) }
            context?.getString(R.string.about_text_9)?.let { items.add(it) }
            context?.getString(R.string.about_text_10)?.let { items.add(it) }
            return items
        }
    }
}