package com.decouikit.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.decouikit.news.R
import com.decouikit.news.adapters.ViewAllAdapter
import com.decouikit.news.utils.NewsConstants
import kotlinx.android.synthetic.main.fragment_view_all.view.*

class ViewAllFragment : Fragment() {

    private lateinit var itemView: View
    private lateinit var title: String

    private lateinit var items: ArrayList<String>
    private lateinit var adapter: ViewAllAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(NewsConstants.VIEW_ALL_TITLE, "").toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        itemView = inflater.inflate(R.layout.fragment_view_all, container, false)
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLayout()
    }

    private fun initLayout() {
        items = arrayListOf()
        items.add("Sport")
        items.add("Architecture")
        items.add("Design")
        items.add("Food")
        items.add("Drink")
        items.add("Design")
        items.add("Architecture")
        adapter = ViewAllAdapter(items)

        itemView.rvItems.layoutManager = LinearLayoutManager(itemView.context)
        itemView.rvItems.adapter = adapter
    }

    companion object {
        fun newInstance(title: String): ViewAllFragment {
            val fragment = ViewAllFragment()
            val args = Bundle()
            args.putString(NewsConstants.VIEW_ALL_TITLE, title)
            fragment.arguments = args
            return fragment
        }
    }
}