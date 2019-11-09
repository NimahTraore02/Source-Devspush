package com.decouikit.news.adapters

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.*
import com.decouikit.news.interfaces.OpenPostListener
import com.decouikit.news.network.dto.PostItem
import kotlinx.android.synthetic.main.adapter_view_all_item.view.*
import java.util.*

class ViewAllAdapter(
    private var items: ArrayList<PostItem>,
    private var listener: OpenPostListener
) : RecyclerView.Adapter<ViewAllAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_view_all_item, parent, false), listener
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(items: ArrayList<PostItem>) {
        this.items.clear()
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

    fun getItemByPosition(position: Int): PostItem {
        return items[position]
    }

    fun removeAllItems() {
        items.removeAll { true }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(private val view: View, private val listener: OpenPostListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var item: PostItem

        init {
            view.itemParent.setOnClickListener(this)
            view.ivBookmark.setOnClickListener(this)
        }

        fun bind(item: PostItem) {
            this.item = item
            view.ivItemBg.load(item)
            view.tvItemTitle.setHtml(item.title.rendered)
            view.tvItemDate.text = Date().getDateFromString(item.date)?.getCalendarDate()
            if (TextUtils.isEmpty(item.categoryName)) {
                view.tvItemTag.text = InMemory.getCategoryById(item.categories[0])?.name?:""
            } else {
                view.tvItemTag.text = item.categoryName
            }
            view.ivBookmark.setBookmarkIcon(item)
        }

        override fun onClick(v: View) {
            when (v) {
                v.itemParent -> {
                    listener.openPost(v, item)
                }
                v.ivBookmark -> {
                    v.bookmark(v.context, item, v.ivBookmark)
                }
            }
        }
    }
}