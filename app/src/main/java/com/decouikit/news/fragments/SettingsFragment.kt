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
import com.decouikit.news.database.Config
import com.decouikit.news.database.InMemory
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.initPopupDialog
import com.decouikit.news.interfaces.ChooseLanguageDialogListener
import com.decouikit.news.interfaces.ResultListener
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.sync.SyncApi
import com.decouikit.news.notification.OneSignalNotificationOpenHandler
import com.decouikit.news.utils.ActivityUtil
import com.decouikit.news.utils.ChooseLanguageDialog
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.fragment_settings.view.tvEnableRtl

class SettingsFragment : Fragment(), View.OnClickListener, ChooseLanguageDialogListener {


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
        initVisibilityForRTLRow()
        initListeners()
    }

    private fun initLayout() {
        itemView.cbNotifications.isChecked = prefs.isPushNotificationEnabled
        itemView.cbEnableRtl.isChecked = prefs.isRtlEnabled

        selectedIndex = Config.getLanguageIndexByCode(prefs.languageCode)
        if (Config.listLanguageNames().size > selectedIndex) {
            itemView.tvLanguage.text = Config.listLanguageNames()[selectedIndex]
        }
    }

    private fun initVisibilityForRTLRow() {
        if (Config.isHideRTLButtonInSettings()) {
            itemView.tvEnableRtl.visibility = View.GONE
            itemView.cbEnableRtl.visibility = View.GONE
            itemView.divider2.visibility = View.GONE
        } else {
            itemView.tvEnableRtl.visibility = View.VISIBLE
            itemView.cbEnableRtl.visibility = View.VISIBLE
            itemView.divider2.visibility = View.VISIBLE
        }
    }

    private fun initListeners() {
        itemView.btnLightMode.setOnClickListener(this)
        itemView.btnDarkMode.setOnClickListener(this)
        itemView.cbNotifications.setOnClickListener(this)
        itemView.tvLanguage.setOnClickListener(this)
        itemView.cbEnableRtl.setOnClickListener(this)
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
            SyncApi.sync(requireContext(), object : ResultListener<Boolean> {
                override fun onResult(value: Boolean?) {
                    progressDialog.dismiss()
                    ActivityUtil.reload(activity, 0)
                }
            })
        }
    }

    private fun setTheme(theme: Int) {
        prefs.colorTheme = theme
        ActivityUtil.reload(activity, 5)
    }

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}