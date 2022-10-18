package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.adapters.holder.HashTagViewHolder
import com.decouikit.news.interfaces.OnHashTagClickListener
import com.decouikit.news.network.dto.Tag

class HashTagAdapter(
    private var items: ArrayList<Tag>,
    private var listener: OnHashTagClickListener
) :
    RecyclerView.Adapter<HashTagViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HashTagViewHolder {
        return HashTagViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_hashtag_item, parent, false), listener
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: HashTagViewHolder, position: Int) =
        holder.bind(items[position])

    fun setData(items: ArrayList<Tag>) {
        this.items = items
        notifyDataSetChanged()
    }
}