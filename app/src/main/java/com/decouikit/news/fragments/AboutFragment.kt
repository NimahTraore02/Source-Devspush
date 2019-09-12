package com.decouikit.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.decouikit.news.R
import com.decouikit.news.adapters.AboutListAdapter
import kotlinx.android.synthetic.main.fragment_about.view.*

class AboutFragment : Fragment() {

    private lateinit var itemView: View
    private var items = ArrayList<String>()

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
        items.add(getString(R.string.about_text_2))
        items.add(getString(R.string.about_text_3))
        items.add(getString(R.string.about_text_4))
        items.add(getString(R.string.about_text_5))
        items.add(getString(R.string.about_text_6))
        items.add(getString(R.string.about_text_7))
        items.add(getString(R.string.about_text_8))
        items.add(getString(R.string.about_text_9))
        items.add(getString(R.string.about_text_10))

        itemView.rvAbout.layoutManager = LinearLayoutManager(itemView.context)
        itemView.rvAbout.adapter = AboutListAdapter(items)
    }

    companion object {
        fun newInstance(): AboutFragment {
            return AboutFragment()
        }
    }
}