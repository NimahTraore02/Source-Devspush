package com.decouikit.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.decouikit.news.R
import com.decouikit.news.activities.common.BaseActivity
import com.decouikit.news.adapters.AboutListAdapter
import com.decouikit.news.database.Config
import com.decouikit.news.extensions.openExternalApp
import kotlinx.android.synthetic.main.fragment_about.view.*

class AboutFragment : Fragment(), View.OnClickListener {

    private lateinit var itemView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        itemView = inflater.inflate(R.layout.fragment_about, container, false)
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        initListeners()
    }

    private fun initLayout() {
        val items = Config.getAboutList(context)
        itemView.rvAbout.layoutManager = LinearLayoutManager(itemView.context)
        itemView.rvAbout.adapter = AboutListAdapter(items)
        itemView.btnPurchase.visibility  = if (Config.getPurchaseLink().isEmpty()) View.GONE else View.VISIBLE
        setRemoveAdsButtonVisibility()
    }

    private fun setRemoveAdsButtonVisibility() {
        if (Config.isRemoveAdsButtonEnabled()) {
            itemView.btnRemoveAds.visibility  =
                if (com.decouikit.news.billing.util.Preference(itemView.context).isItemPurchased) View.GONE else View.VISIBLE
        } else {
            itemView.btnRemoveAds.visibility = View.GONE
        }
    }

    private fun initListeners() {
        itemView.btnPurchase.setOnClickListener(this)
        itemView.btnRemoveAds.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v) {
            itemView.btnPurchase -> {
                v.openExternalApp(Config.getPurchaseLink())
            }
            itemView.btnRemoveAds -> {
                (activity as BaseActivity).billing?.removeAds()
            }
        }
    }

    companion object {
        fun newInstance(): AboutFragment {
            return AboutFragment()
        }
    }
}