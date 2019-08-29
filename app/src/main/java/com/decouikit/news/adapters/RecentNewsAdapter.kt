package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import kotlinx.android.synthetic.main.adapter_recent_news_item.view.*

class RecentNewsAdapter(private val items: ArrayList<String>)
    : RecyclerView.Adapter<RecentNewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_recent_news_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(text: String) {
            view.tvItemTag.text = text
        }
    }
}