package com.decouikit.news.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.decouikit.news.R
import com.decouikit.news.adapters.AboutListAdapter
import com.decouikit.news.database.Config
import kotlinx.android.synthetic.main.fragment_about.view.*

class AboutFragment : Fragment() {

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
    }

    private fun initLayout() {
        val items = Config.getAboutList(context)
        itemView.rvAbout.layoutManager = LinearLayoutManager(itemView.context)
        itemView.rvAbout.adapter = AboutListAdapter(items)
        itemView.btnPurchase.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Config.getPurchaseLink())
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance(): AboutFragment {
            return AboutFragment()
        }
    }
}