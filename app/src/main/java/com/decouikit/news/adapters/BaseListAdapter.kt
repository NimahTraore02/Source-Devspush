package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.adapters.common.CommonListAdapterType
import com.decouikit.news.adapters.holder.BaseListViewHolder
import com.decouikit.news.interfaces.OpenPostListener
import com.decouikit.news.network.dto.PostItem
import java.util.*

open class BaseListAdapter(
    private var items: ArrayList<PostItem>,
    private var adapterType: CommonListAdapterType
) :
    RecyclerView.Adapter<BaseListViewHolder>() {

    private var listener: OpenPostListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseListViewHolder {
        val layout = when (adapterType) {
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
        return BaseListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(layout, parent, false)
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

    fun removeAllItems() {
        items.removeAll { true }
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

    override fun onBindViewHolder(holder: BaseListViewHolder, position: Int) {
        listener?.let { holder.bind(items[position], it) }
    }
}