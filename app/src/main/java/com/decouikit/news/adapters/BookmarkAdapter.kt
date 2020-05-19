package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.adapters.common.CommonListAdapterType
import com.decouikit.news.adapters.holder.BookmarkViewHolder
import com.decouikit.news.interfaces.RemoveBookmarkListener
import com.decouikit.news.network.dto.PostItem
import java.util.*

class BookmarkAdapter(
    private var items: ArrayList<PostItem>,
    private var mListener: RemoveBookmarkListener,
    private var adapterType: CommonListAdapterType
) : RecyclerView.Adapter<BookmarkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val layout = when(adapterType) {
            CommonListAdapterType.ADAPTER_VERSION_1 -> {
                R.layout.adapter_view_all_item
            }
            CommonListAdapterType.ADAPTER_VERSION_2 -> {
                R.layout.adapter_featured_news_item
            }
            CommonListAdapterType.ADAPTER_VERSION_3 -> {
                R.layout.adapter_recent_news_item
            }
        }
        return BookmarkViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(layout, parent, false), mListener
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