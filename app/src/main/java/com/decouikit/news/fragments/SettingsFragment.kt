package com.decouikit.news.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.decouikit.news.R
import com.decouikit.news.database.Config
import com.decouikit.news.database.InMemory
import com.decouikit.news.database.Preference
import com.decouikit.news.interfaces.SyncListener
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.sync.SyncApi
import com.decouikit.news.notification.OneSignalNotificationOpenHandler
import com.decouikit.news.utils.ActivityUtil
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var itemView: View
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
        initSpinner()
    }

    private fun initSpinner() {
        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            Config.listLanguageNames()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter
        languageSpinner.setSelection(Config.getLanguageIndexByCode(Preference(requireContext()).languageCode))
        Handler().postDelayed({
            languageSpinner.onItemSelectedListener = this
        }, 500)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, p3: Long) {
        val lang = Config.getLanguageByName(parent?.getItemAtPosition(position) as String)
        val code = prefs.languageCode
        if (lang?.languageCode != code) {
            RetrofitClientInstance.clear()
            InMemory.clear()
            prefs.languageCode = lang?.languageCode ?: Config.getDefaultValueForLanguage()
            SyncApi.sync(requireContext(), object : SyncListener {
                override fun finish(success: Boolean) {
                    ActivityUtil.reload(activity, 5)
                }
            })
        }
    }

    private fun initListeners() {
        itemView.btnLightMode.setOnClickListener(this)
        itemView.btnDarkMode.setOnClickListener(this)
        itemView.cbNotifications.setOnClickListener(this)
//        itemView.cbEnableLang.setOnClickListener(this)
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
                    OneSignal.startInit(context)
                        .setNotificationOpenedHandler(
                            OneSignalNotificationOpenHandler(
                                requireContext()
                            )
                        )
                        .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                        .unsubscribeWhenNotificationsAreDisabled(true)
                        .init()
                } else {
                    OneSignal.setSubscription(false)
                }
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