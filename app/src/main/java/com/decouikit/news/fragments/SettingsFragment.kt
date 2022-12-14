package com.decouikit.news.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.decouikit.news.R
import com.decouikit.news.adapters.common.CommonListAdapterType
import com.decouikit.news.database.Config
import com.decouikit.news.database.InMemory
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.initPopupDialog
import com.decouikit.news.extensions.setIconsForListType
import com.decouikit.news.interfaces.ChooseLanguageDialogListener
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.sync.SyncApi
import com.decouikit.news.notification.OneSignalNotificationOpenHandler
import com.decouikit.news.utils.*
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.fragment_settings.view.tvEnableRtl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsFragment : Fragment(), View.OnClickListener, ChooseLanguageDialogListener,
    OnListTypeChangeListener {


    private lateinit var itemView: View
    private lateinit var progressDialog: Dialog

    private var selectedIndex = 0

    private val prefs: Preference by lazy {
        Preference(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        itemView = inflater.inflate(R.layout.fragment_settings, container, false)
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        initListeners()
    }

    private fun initLayout() {
        itemView.cbNotifications.isChecked = prefs.isPushNotificationEnabled
        itemView.cbEnableRtl.isChecked = prefs.isRtlEnabled

        initVisibilityForRTLRow()

        initVisibilityForLanguage()

        selectedIndex = Config.getLanguageIndexByCode(prefs.languageCode)
        if (Config.listLanguageNames().size > selectedIndex) {
            itemView.tvLanguage.text = Config.listLanguageNames()[selectedIndex]
        }

        initVisibilityForRecentNews()
        initVisibilityForViewAll()
        initVisibilityForSearch()
        initVisibilityForNotification()
        initVisibilityForRecentNewsInPost()
        initVisibilityForBookmark()

        setListTypeIcons()
    }

    private fun initVisibilityForRTLRow() {
        itemView.tvEnableRtl.visibility =  if (Config.isRtlOptionVisible()) View.VISIBLE else View.GONE
        itemView.cbEnableRtl.visibility = if (Config.isRtlOptionVisible()) View.VISIBLE else View.GONE
        itemView.divider2.visibility = if (Config.isRtlOptionVisible()) View.VISIBLE else View.GONE
    }

    private fun initVisibilityForLanguage() {
        itemView.tvEnableLanguage.visibility =  if (Config.isLanguageOptionVisible()) View.VISIBLE else View.GONE
        itemView.tvLanguage.visibility = if (Config.isLanguageOptionVisible()) View.VISIBLE else View.GONE
        itemView.divider3.visibility = if (Config.isLanguageOptionVisible()) View.VISIBLE else View.GONE
    }

    private fun initVisibilityForRecentNews() {
        itemView.tvRecentNews.visibility =  if (Config.isRecentNewsListOptionVisible()) View.VISIBLE else View.GONE
        itemView.ivRecentNews.visibility = if (Config.isRecentNewsListOptionVisible()) View.VISIBLE else View.GONE
        itemView.cbRecentNews.visibility = if (Config.isRecentNewsListOptionVisible()) View.VISIBLE else View.GONE
        itemView.divider4.visibility = if (Config.isRecentNewsListOptionVisible()) View.VISIBLE else View.GONE
    }

    private fun initVisibilityForViewAll() {
        itemView.tvViewAll.visibility =  if (Config.isViewAllListOptionVisible()) View.VISIBLE else View.GONE
        itemView.ivViewAll.visibility = if (Config.isViewAllListOptionVisible()) View.VISIBLE else View.GONE
        itemView.cbViewAll.visibility = if (Config.isViewAllListOptionVisible()) View.VISIBLE else View.GONE
        itemView.divider5.visibility = if (Config.isViewAllListOptionVisible()) View.VISIBLE else View.GONE
    }

    private fun initVisibilityForSearch() {
        itemView.tvSearch.visibility =  if (Config.isSearchListOptionVisible()) View.VISIBLE else View.GONE
        itemView.ivSearch.visibility = if (Config.isSearchListOptionVisible()) View.VISIBLE else View.GONE
        itemView.cbSearch.visibility = if (Config.isSearchListOptionVisible()) View.VISIBLE else View.GONE
        itemView.divider6.visibility = if (Config.isSearchListOptionVisible()) View.VISIBLE else View.GONE
    }

    private fun initVisibilityForNotification() {
        itemView.tvNotification.visibility =  if (Config.isNotificationListOptionVisible()) View.VISIBLE else View.GONE
        itemView.ivNotification.visibility = if (Config.isNotificationListOptionVisible()) View.VISIBLE else View.GONE
        itemView.cbNotification.visibility = if (Config.isNotificationListOptionVisible()) View.VISIBLE else View.GONE
        itemView.divider7.visibility = if (Config.isNotificationListOptionVisible()) View.VISIBLE else View.GONE
    }

    private fun initVisibilityForRecentNewsInPost() {
        itemView.tvRecentNewsFromPost.visibility =  if (Config.isRecentListInPostOptionVisible()) View.VISIBLE else View.GONE
        itemView.ivRecentNewsFromPost.visibility = if (Config.isRecentListInPostOptionVisible()) View.VISIBLE else View.GONE
        itemView.cbRecentNewsFromPost.visibility = if (Config.isRecentListInPostOptionVisible()) View.VISIBLE else View.GONE
        itemView.divider8.visibility = if (Config.isRecentListInPostOptionVisible()) View.VISIBLE else View.GONE
    }

    private fun initVisibilityForBookmark() {
        itemView.tvBookmark.visibility =  if (Config.isBookmarkListOptionVisible()) View.VISIBLE else View.GONE
        itemView.ivBookmark.visibility = if (Config.isBookmarkListOptionVisible()) View.VISIBLE else View.GONE
        itemView.cbBookmark.visibility = if (Config.isBookmarkListOptionVisible()) View.VISIBLE else View.GONE
        itemView.divider9.visibility = if (Config.isBookmarkListOptionVisible()) View.VISIBLE else View.GONE
    }

    private fun setListTypeIcons() {
        itemView.ivRecentNews.setIconsForListType(AdapterListTypeUtil.getAdapterTypeFromValue(prefs.recentAdapterStyle))
        itemView.ivViewAll.setIconsForListType(AdapterListTypeUtil.getAdapterTypeFromValue(prefs.viewAllAdapterStyle))
        itemView.ivSearch.setIconsForListType(AdapterListTypeUtil.getAdapterTypeFromValue(prefs.searchAdapterStyle))
        itemView.ivNotification.setIconsForListType(AdapterListTypeUtil.getAdapterTypeFromValue(prefs.notificationAdapterStyle))
        itemView.ivRecentNewsFromPost.setIconsForListType(AdapterListTypeUtil.getAdapterTypeFromValue(prefs.recentFromPostAdapterStyle))
        itemView.ivBookmark.setIconsForListType(AdapterListTypeUtil.getAdapterTypeFromValue(prefs.bookmarkAdapterStyle))
    }

    private fun initListeners() {
        itemView.btnLightMode.setOnClickListener(this)
        itemView.btnDarkMode.setOnClickListener(this)
        itemView.cbNotifications.setOnClickListener(this)
        itemView.tvLanguage.setOnClickListener(this)
        itemView.cbEnableRtl.setOnClickListener(this)

        itemView.tvRecentNews.setOnClickListener(this)
        itemView.cbRecentNews.setOnClickListener(this)
        itemView.ivRecentNews.setOnClickListener(this)

        itemView.tvViewAll.setOnClickListener(this)
        itemView.cbViewAll.setOnClickListener(this)
        itemView.ivViewAll.setOnClickListener(this)

        itemView.tvSearch.setOnClickListener(this)
        itemView.cbSearch.setOnClickListener(this)
        itemView.ivSearch.setOnClickListener(this)

        itemView.tvNotification.setOnClickListener(this)
        itemView.cbNotification.setOnClickListener(this)
        itemView.ivNotification.setOnClickListener(this)

        itemView.tvRecentNewsFromPost.setOnClickListener(this)
        itemView.cbRecentNewsFromPost.setOnClickListener(this)
        itemView.ivRecentNewsFromPost.setOnClickListener(this)

        itemView.tvBookmark.setOnClickListener(this)
        itemView.cbBookmark.setOnClickListener(this)
        itemView.ivBookmark.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v) {
            itemView.btnLightMode -> {
                setTheme(Config.getLightTheme())
            }
            itemView.btnDarkMode -> {
                setTheme(Config.getDarkTheme())
            }
            itemView.cbNotifications -> {
                val isChecked: Boolean = (v as CheckBox).isChecked
                prefs.isPushNotificationEnabled = isChecked
                if (isChecked) {
                    OneSignal.startInit(requireContext().applicationContext)
                        .setNotificationOpenedHandler(OneSignalNotificationOpenHandler(requireContext().applicationContext))
                        .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                        .unsubscribeWhenNotificationsAreDisabled(true)
                        .init()
                }
                OneSignal.setSubscription(isChecked)
            }
            itemView.cbEnableRtl -> {
                val isChecked: Boolean = (v as CheckBox).isChecked
                prefs.isRtlEnabled = isChecked
                if (isChecked) {
                    activity?.let {
                        ActivityUtil.setLayoutDirection(
                            it,
                            ViewCompat.LAYOUT_DIRECTION_RTL,
                            R.id.parent
                        )
                    }
                } else {
                    activity?.let {
                        ActivityUtil.setLayoutDirection(
                            it,
                            ViewCompat.LAYOUT_DIRECTION_LOCALE,
                            R.id.parent
                        )
                    }
                }
                ActivityUtil.reload(activity, 5)
            }
            itemView.tvLanguage -> {
                ChooseLanguageDialog(
                    itemView.context,
                    getString(R.string.choose_lang),
                    Config.listLanguageNames().toTypedArray(), selectedIndex, this
                )
            }
            itemView.tvRecentNews,
            itemView.ivRecentNews,
            itemView.cbRecentNews -> {
                activity?.let { ChooseListStyleDialog.showDialog(it, ListType.RECENT_NEWS, this) }
            }
            itemView.tvViewAll,
            itemView.ivViewAll,
            itemView.cbViewAll -> {
                activity?.let { ChooseListStyleDialog.showDialog(it, ListType.VIEW_ALL, this) }
            }
            itemView.tvSearch,
            itemView.ivSearch,
            itemView.cbSearch -> {
                activity?.let { ChooseListStyleDialog.showDialog(it, ListType.SEARCH, this) }
            }
            itemView.tvNotification,
            itemView.ivNotification,
            itemView.cbNotification -> {
                activity?.let { ChooseListStyleDialog.showDialog(it, ListType.NOTIFICATION, this) }
            }
            itemView.tvRecentNewsFromPost,
            itemView.ivRecentNewsFromPost,
            itemView.cbRecentNewsFromPost -> {
                activity?.let { ChooseListStyleDialog.showDialog(it, ListType.RECENT_NEWS_FROM_POST, this) }
            }
            itemView.tvBookmark,
            itemView.ivBookmark,
            itemView.cbBookmark -> {
                activity?.let { ChooseListStyleDialog.showDialog(it, ListType.BOOKMARK, this) }
            }
        }
    }

    override fun onLanguageItemChecked(selectedIndex: Int) {
        progressDialog = Dialog(itemView.context)
        progressDialog.initPopupDialog()
        progressDialog.show()
        val lang = Config.listOfLanguages()[selectedIndex]
        val code = prefs.languageCode
        if (lang.languageCode != code) {
            itemView.tvLanguage.text = lang.language
            RetrofitClientInstance.clear()
            InMemory.clear()
            prefs.languageCode = lang.languageCode

            GlobalScope.launch(Dispatchers.IO) {
                SyncApi.sync(requireContext())
                progressDialog.dismiss()
                ActivityUtil.reload(activity, 0)
            }
        }
    }

    private fun setTheme(theme: Int) {
        prefs.colorTheme = theme
        ActivityUtil.reload(activity, 5)
    }

    override fun onListTypeChange(adapterType: CommonListAdapterType, listType: ListType) {
        when (listType) {
            ListType.RECENT_NEWS -> {
                prefs.recentAdapterStyle = adapterType.id
            }
            ListType.VIEW_ALL -> {
                prefs.viewAllAdapterStyle = adapterType.id
            }
            ListType.SEARCH -> {
                prefs.searchAdapterStyle = adapterType.id
            }
            ListType.NOTIFICATION -> {
                prefs.notificationAdapterStyle = adapterType.id
            }
            ListType.RECENT_NEWS_FROM_POST -> {
                prefs.recentFromPostAdapterStyle = adapterType.id
            }
            ListType.BOOKMARK -> {
                prefs.bookmarkAdapterStyle = adapterType.id
            }
        }
        activity?.runOnUiThread {
            setListTypeIcons()
        }
    }

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}