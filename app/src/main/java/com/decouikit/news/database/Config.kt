package com.decouikit.news.database

import android.content.Context
import com.decouikit.news.R
import com.decouikit.news.activities.common.BottomNavigationActivity
import com.decouikit.news.activities.common.NavigationActivity
import com.decouikit.news.network.dto.Category
import com.decouikit.news.network.dto.WizardItemModel


object Config {

    fun listOfLanguages(): List<Language> {
        val languages = mutableListOf<Language>()
        languages.add(
            Language(
                baseUrl = "https://ebmnews.com/wp-json/wp/v2/",
                language = "English",
                languageCode = "en"
            )
        )
        languages.add(
            Language(
                baseUrl = "https://hindi.ebmnews.com/wp-json/wp/v2/",
                language = "Hindi",
                languageCode = "hi"
            )
        )
        languages.add(
            Language(
                baseUrl = "https://malayalam.ebmnews.com//wp-json/wp/v2/",
                language = "Malayalam",
                languageCode = "ml"
            )
        )
        return languages
    }

    fun getDefaultValueForLanguage(): String {
        return "en"
    }

    fun getDefaultTheme(): Int {
        //Set default theme light or dark
        return getLightTheme()
    }

    fun getLightTheme(): Int {
        return R.style.AppTheme
    }

    fun getDarkTheme(): Int {
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

    fun getPurchaseLink(): String {
        return "https://devspush.com/category/android"
    }

    fun isRateMeEnabled(): Boolean = true

    fun getRateMeConfig(): RateMeConfig = RateMeConfig()

    //If you want to exclude some category from list add that category in this array
    private fun getListOfExcludedCategories(): ArrayList<String> {
        val items = arrayListOf<String>()
//            items.add("Category name")
//            items.add("Architecture")
        return items
    }

    fun getListOfIncludedCategories(): ArrayList<String> {
        val items = arrayListOf<String>()
        items.add("National")
        items.add("World")
        items.add("Sports")
        items.add("Entertainment")
        items.add("Technology")
        items.add("Business")
        items.add("Health")
        items.add("Automotive")
        items.add("Crime")
        return items
    }

    fun isExcludeCategoryEnabled(): Boolean {
        return false
    }

    fun isCategoryIncluded(category: Category): Boolean {
        return getListOfIncludedCategories().contains(category.name)
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

    fun isIntroPageEnabled(): Boolean = true

    fun getIntroData(context: Context): List<WizardItemModel> {
        val pageOne = WizardItemModel(
            context.getString(R.string.wizard_page_one_title),
            context.getString(R.string.wizard_page_one_subtitle),
            "https://firebasestorage.googleapis.com/v0/b/deco-fef08.appspot.com/o/wizard%2F1.jpg?alt=media&token=a6ee513a-cd8f-47b0-8fa5-7e31d032e8e7"
        )

        val pageTwo = WizardItemModel(
            context.getString(R.string.wizard_page_two_title),
            context.getString(R.string.wizard_page_two_subtitle),
            "https://firebasestorage.googleapis.com/v0/b/deco-fef08.appspot.com/o/wizard%2F2.jpg?alt=media&token=ca473522-2688-4f6f-ad3c-0df6bd5ecd36"
        )

        val pageThree = WizardItemModel(
            context.getString(R.string.wizard_page_three_title),
            context.getString(R.string.wizard_page_three_subtitle),
            "https://firebasestorage.googleapis.com/v0/b/deco-fef08.appspot.com/o/wizard%2F3.jpg?alt=media&token=b0b0fd45-5420-4454-8fe8-ddbb73449a5c"
        )

        return arrayListOf(pageOne, pageTwo, pageThree)
    }

    fun getLanguageByName(lang: String): Language? {
        var language: Language? = null
        for (l in listOfLanguages()) {
            if (l.language == lang) {
                language = l
            }
        }
        return language
    }

    fun getBaseUrl(context: Context): Language? {
        val code = Preference(context).languageCode
        val languages = listOfLanguages()
        for (lang in languages) {
            if (lang.languageCode == code) {
                return lang
            }
        }
        if (languages.isNotEmpty()) {
            return languages[0]
        }
        return null
    }

    fun getLanguageIndexByCode(code: String): Int {
        var result = -1
        val languages = listOfLanguages()
        languages.forEachIndexed { index, language ->
            if (language.languageCode == code) {
                result = index
            }
        }
        return result
    }

    fun listLanguageNames(): List<String> {
        val languages = mutableListOf<String>()
        for (lang in listOfLanguages()) {
            languages.add(lang.language)
        }
        return languages
    }
}