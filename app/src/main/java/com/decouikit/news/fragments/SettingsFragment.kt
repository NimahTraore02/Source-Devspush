package com.decouikit.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.decouikit.news.R
import com.decouikit.news.database.Config
import com.decouikit.news.database.Preference
import com.decouikit.news.notification.OneSignalNotificationOpenHandler
import com.decouikit.news.utils.ActivityUtil
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment(), View.OnClickListener {

    private lateinit var itemView: View
    private val prefs: Preference? by lazy {
        context?.let {
            Preference(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        itemView = inflater.inflate(R.layout.fragment_settings, container, false)
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLayout()
        initListeners()
    }

    private fun initLayout() {
         itemView.cbNotifications.isChecked = prefs?.isPushNotificationEnabled!!
        itemView.cbEnableRtl.isChecked = prefs?.isRtlEnabled!!
    }

    private fun initListeners() {
        itemView.btnLightMode.setOnClickListener(this)
        itemView.btnDarkMode.setOnClickListener(this)
        itemView.cbNotifications.setOnClickListener(this)
        itemView.cbEnableRtl.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v) {
            itemView.btnLightMode -> {
                setTheme(Config.getLightTheme())
            }
            itemView.btnDarkMode -> {
                setTheme(Config.getDarkTheme())
            }
        }
        if (v is CheckBox) {
            val isChecked: Boolean = v.isChecked
            when(v) {
                itemView.cbNotifications -> {
                    prefs?.isPushNotificationEnabled = isChecked
                    if (Preference(context = requireContext()).isPushNotificationEnabled) {
                        OneSignal.startInit(context)
                            .setNotificationOpenedHandler(OneSignalNotificationOpenHandler(requireContext()))
                            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                            .unsubscribeWhenNotificationsAreDisabled(true)
                            .init()
                    } else {
                        OneSignal.setSubscription(false)
                    }
                }
                itemView.cbEnableRtl -> {
                    prefs?.isRtlEnabled = isChecked
                    if (isChecked) {
                        activity?.let { ActivityUtil.setLayoutDirection(it, ViewCompat.LAYOUT_DIRECTION_RTL, R.id.parent) }
                    } else {
                        activity?.let { ActivityUtil.setLayoutDirection(it, ViewCompat.LAYOUT_DIRECTION_LOCALE, R.id.parent) }
                    }
                    ActivityUtil.reload(activity, 5)
                }
            }
        }
    }

    private fun setTheme(theme: Int) {
        prefs?.colorTheme = theme
        ActivityUtil.reload(activity, 5)
    }

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}