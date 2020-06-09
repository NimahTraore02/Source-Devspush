package com.decouikit.news.database

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.decouikit.advertising.FacebookAds
import com.decouikit.advertising.GoogleAds
import com.decouikit.advertising.model.AdEventListener
import com.decouikit.advertising.model.AdsConfigItem
import com.decouikit.advertising.model.AdsContract
import com.decouikit.news.R
import com.decouikit.news.activities.common.BottomNavigationActivity
import com.decouikit.news.activities.common.NavigationActivity
import com.decouikit.news.activities.common.SideMenuWithBottomNavigationActivity
import com.decouikit.news.adapters.common.CategoryListAdapterType
import com.decouikit.news.adapters.common.CommonListAdapterType
import com.decouikit.news.billing.GooglePlayBilling
import com.decouikit.news.billing.model.BillingConfigItem
import com.decouikit.news.billing.model.BillingContract
import com.decouikit.news.billing.model.BillingEventListener
import com.decouikit.news.network.dto.*

object Config {

    fun listOfLanguages(): List<Language> {
        val languages = mutableListOf<Language>()
        languages.add(
            Language(
                baseUrl = "https://deconews.decouikit.com/wp-json/wp/v2/",
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

    fun getDefaultNavigationStyle(): Class<*> = SideMenuWithBottomNavigationActivity::class.java

    fun bottomNavigationStyle(): Class<*> = BottomNavigationActivity::class.java

    fun drawerNavigationStyle(): Class<*> = NavigationActivity::class.java

    //if you want "All" screen at tab beginning
    fun isAllScreenEnabled(): Boolean = true

    //Show interstitial ad after 10 opens of single post
    // if you don't want interstitial ads just put value -1
    fun promptForInterstitialCounter(): Int = 10

    fun getNumberOfItemPerPage(): Int = 5

    //It's better to have more items per page on search, because of scroll behavior at the bottom
    fun getNumberOfSearchedItemsPerPage(): Int = 10

    fun getOrder(): String = "desc"

    fun getNumberOfItemForSlider(): Int = 3

    fun isFeaturesPostsGetFromSticky(): Boolean = false

    fun getDefaultValueForPushNotification(): Boolean = true

    fun getDefaultValueForRTL(): Boolean = false

    //Visibility for options on Settings screen
    fun isRtlOptionVisible(): Boolean = true
    fun isLanguageOptionVisible(): Boolean = true
    fun isRecentNewsListOptionVisible(): Boolean = true
    fun isViewAllListOptionVisible(): Boolean = true
    fun isSearchListOptionVisible(): Boolean = true
    fun isNotificationListOptionVisible(): Boolean = true
    fun isRecentListInPostOptionVisible(): Boolean = true
    fun isBookmarkListOptionVisible(): Boolean = true
    /*
    Setting list types
    IMPORTANT! -> You can change list type here only if option for that list above is set to false
     */
    fun getRecentAdapterConfig(): CommonListAdapterType = CommonListAdapterType.ADAPTER_VERSION_3
    fun getViewAllAdapterConfig(): CommonListAdapterType = CommonListAdapterType.ADAPTER_VERSION_1
    fun getSearchAdapterConfig(): CommonListAdapterType = CommonListAdapterType.ADAPTER_VERSION_1
    fun getNotificationAdapterConfig(): CommonListAdapterType = CommonListAdapterType.ADAPTER_VERSION_1
    fun getRecentNewsFromPostAdapterConfig(): CommonListAdapterType = CommonListAdapterType.ADAPTER_VERSION_1
    fun getBookmarkAdapterConfig(): CommonListAdapterType = CommonListAdapterType.ADAPTER_VERSION_2

    //List type for Categories
    fun getCategoryAdapterConfig(): CategoryListAdapterType = CategoryListAdapterType.ADAPTER_VERSION_2

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

    fun getAdsType(): AdsType = AdsType.GOOGLE

    fun getAdsProvider(adsContainer: ViewGroup?, listener: AdEventListener): AdsContract? {
        if (getAdsType() == AdsType.NONE) {
            return null
        }
        return if (AdsType.FACEBOOK == getAdsType()) {
            FacebookAds(AdsConfigItem(
                bannerAdUnitId = "232044561380871_232073974711263",
                interstitialAdUnitId = "232044561380871_232073981377929",
                testDeviceId = "0564736e-7d56-4a71-8232-77b7d9696329"
            ), adsContainer, listener)
        } else {
            GoogleAds(AdsConfigItem(
                bannerAdUnitId = "ca-app-pub-3940256099942544/6300978111",
                interstitialAdUnitId = "ca-app-pub-3940256099942544/1033173712",
                testDeviceId = "D4660E67112CAF17CE018F3C95A8F64D"
             ), adsContainer, listener)
        }
    }

    fun isWritingCommentEnabled() = true

    fun isReadingCommentEnabled() = true

    fun getBillingContract(activity: Activity, billingEventListener: BillingEventListener) : BillingContract? {
        return GooglePlayBilling(activity, BillingConfigItem(), billingEventListener)
    }

    //This is only for showing or hiding button on About screen
    fun isRemoveAdsButtonEnabled() = true
}