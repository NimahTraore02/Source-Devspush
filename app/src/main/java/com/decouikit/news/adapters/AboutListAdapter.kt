package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.adapters.holder.AboutViewHolder

class AboutListAdapter(private val items: ArrayList<String>) :
    RecyclerView.Adapter<AboutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder {
        return AboutViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_about_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AboutViewHolder, position: Int) =
        holder.bind(items[position])
}