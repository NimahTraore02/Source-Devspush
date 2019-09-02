package com.decouikit.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        initListeners()
    }

    private fun initListeners() {
        itemView.btnLightMode.setOnClickListener(this)
        itemView.btnDarkMode.setOnClickListener(this)
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