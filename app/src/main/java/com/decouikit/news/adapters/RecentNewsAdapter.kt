package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.adapters.holder.RecentNewsViewHolder
import com.decouikit.news.network.dto.PostItem
import java.util.*

class RecentNewsAdapter(private var items: ArrayList<PostItem>) :
    RecyclerView.Adapter<RecentNewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentNewsViewHolder {
        return RecentNewsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_recent_news_item, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    fun setData(items: ArrayList<PostItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun removeAllItems() {
        items.removeAll { true }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecentNewsViewHolder, position: Int) =
        holder.bind(items[position])


}