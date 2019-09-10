package com.decouikit.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.decouikit.news.R
import com.decouikit.news.database.Preference
import com.decouikit.news.utils.ActivityUtil
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
                setTheme(R.style.AppTheme)
            }
            itemView.btnDarkMode -> {
                setTheme(R.style.AppThemeDark)
            }
        }
        if (v is CheckBox) {
            val isChecked: Boolean = v.isChecked
            when(v) {
                itemView.cbNotifications -> {
                    prefs?.isPushNotificationEnabled = isChecked
                }
                itemView.cbEnableRtl -> {
                    prefs?.isRtlEnabled = isChecked
                    if (isChecked) {
                        activity?.let { ActivityUtil.setLayoutDirection(it, ViewCompat.LAYOUT_DIRECTION_RTL) }
                    } else {
                        activity?.let { ActivityUtil.setLayoutDirection(it, ViewCompat.LAYOUT_DIRECTION_LOCALE) }
                    }
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