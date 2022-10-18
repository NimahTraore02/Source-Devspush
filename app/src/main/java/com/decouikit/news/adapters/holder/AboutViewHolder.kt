package com.decouikit.news.adapters.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.adapter_about_list_item.view.*

class AboutViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: String) {
        view.tvTitle.text = item
    }
}