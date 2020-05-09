package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.adapters.holder.RecentNewsViewHolder
import com.decouikit.news.adapters.holder.ViewAllViewHolder
import com.decouikit.news.interfaces.OpenPostListener
import com.decouikit.news.network.dto.PostItem
import java.util.*

class ViewAllAdapter(
    private var items: ArrayList<PostItem>
) : RecyclerView.Adapter<ViewAllViewHolder>() {

    private var listener: OpenPostListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllViewHolder {
        return ViewAllViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_view_all_item, parent, false)
        )
    }

    fun setItemClickListener(listener: OpenPostListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int = items.size

    fun setData(items: ArrayList<PostItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setSearchedData(items: ArrayList<PostItem>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun removeItem(item: PostItem) {
        items.remove(item)
        notifyDataSetChanged()
    }

    fun removeItemByPosition(position: Int) {
        items.removeAt(position)
        notifyDataSetChanged()
    }

    fun getItemByPosition(position: Int): PostItem = items[position]

    fun removeAllItems() {
        items.removeAll { true }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewAllViewHolder, position: Int) {
        listener?.let { holder.bind(items[position], it) }
    }

}