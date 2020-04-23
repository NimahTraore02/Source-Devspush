package com.decouikit.news.database

import android.content.Context
import com.decouikit.news.R
import com.decouikit.news.activities.common.BottomNavigationActivity
import com.decouikit.news.activities.common.NavigationActivity
import com.decouikit.news.network.dto.Category
import com.decouikit.news.network.dto.Language
import com.decouikit.news.network.dto.RateMeConfig
import com.decouikit.news.network.dto.WizardItemModel

object Config {

    fun listOfLanguages(): List<Language> {
        val languages = mutableListOf<Language>()
        languages.add(
            Language(
                baseUrl = "https://asorockpost.com/wp-json/wp/v2/",
                language = "English",
                languageCode = "en"
            )
        )
//        languages.add(
//            Language(
//                baseUrl = "SITE YOUR NEW LANGUAGE",
//                language = "NEW LANGUAGE",
//                languageCode = "SHORT CODE FOR LANGUAGE"
//            )
//        )
        return languages
    }

    fun getDefaultValueForLanguage(): String = "en"

    //Set default theme light or dark
    fun getDefaultTheme(): Int = getDarkTheme()

    fun getLightTheme(): Int = R.style.AppTheme

    fun getDarkTheme(): Int = R.style.AppThemeDark

    fun getDefaultNavigationStyle(): Class<*> = bottomNavigationStyle()

    fun bottomNavigationStyle(): Class<*> = BottomNavigationActivity::class.java

    fun drawerNavigationStyle(): Class<*> = NavigationActivity::class.java

    //if you want "All" screen at tab beginning
    fun isAllScreenEnabled(): Boolean = false

    //Show interstitial after 10 opens single post
    // if you don't want interstitial ads just put value -1
    fun promptForInterstitialCounter(): Int = 10

    fun getNumberOfItemPerPage(): Int = 10

    fun getOrder(): String = "desc"

    fun getNumberOfItemForSlider(): Int = 3

    fun isFeaturesPostsGetFromSticky(): Boolean = false

    fun getDefaultValueForPushNotification(): Boolean = true

    fun getDefaultValueForRTL(): Boolean = false

    fun getFacebookUrl(): String = "https://www.facebook.com"

    fun getTwitterUrl(): String = "https://twitter.com"

    fun getYoutubeUrl(): String = "https://www.youtube.com/"

    fun getInstagramUrl(): String = "https://www.instagram.com"

    fun getPurchaseLink(): String = "https://devspush.com/deco-news-android-mobile-app-for-wordpress"

    fun isRateMeEnabled(): Boolean = true

    fun getRateMeConfig(): RateMeConfig = RateMeConfig()

    //If you want to exclude some category from list add that category in this array
    private fun getListOfExcludedCategories(): ArrayList<String> {
        return arrayListOf()
    }

    fun getListOfIncludedCategories(): ArrayList<String> {
        return arrayListOf()
    }

    fun isExcludeCategoryEnabled(): Boolean = true

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
            "https://decouikit.com/presentationEnvato/deco-news-android/magic.png"
        )

        val pageTwo = WizardItemModel(
            context.getString(R.string.wizard_page_two_title),
            context.getString(R.string.wizard_page_two_subtitle),
            "https://decouikit.com/presentationEnvato/deco-news-android/design.png"
        )

        val pageThree = WizardItemModel(
            context.getString(R.string.wizard_page_three_title),
            context.getString(R.string.wizard_page_three_subtitle),
            "https://decouikit.com/presentationEnvato/deco-news-android/code.png"
        )

        return arrayListOf(pageOne, pageTwo, pageThree)
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