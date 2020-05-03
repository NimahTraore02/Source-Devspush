package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.adapters.holder.BookmarkViewHolder
import com.decouikit.news.interfaces.RemoveBookmarkListener
import com.decouikit.news.network.dto.PostItem
import java.util.*

class BookmarkAdapter(
    private var items: ArrayList<PostItem>,
    private var mListener: RemoveBookmarkListener
) : RecyclerView.Adapter<BookmarkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        return BookmarkViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_view_all_item, parent, false), mListener
        )
    }

    fun setData(items: ArrayList<PostItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) =
        holder.bind(items[position])

}