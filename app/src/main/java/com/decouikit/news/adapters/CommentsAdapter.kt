package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.adapters.holder.CommentViewHolder
import com.decouikit.news.network.dto.CommentItem
import java.util.*

class CommentsAdapter(private var items: ArrayList<CommentItem>) :
    RecyclerView.Adapter<CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_comment_item, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) = holder.bind(items[position])

    fun setData(items: ArrayList<CommentItem>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun removeAllItems() {
        items.removeAll { true }
        notifyDataSetChanged()
    }
}