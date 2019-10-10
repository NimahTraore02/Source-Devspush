package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.*
import com.decouikit.news.interfaces.OpenPostListener
import com.decouikit.news.network.dto.PostItem
import kotlinx.android.synthetic.main.adapter_view_all_item.view.*
import java.util.*

class ViewAllAdapter(private var items: ArrayList<PostItem>,
                     private var listener: OpenPostListener)
    : RecyclerView.Adapter<ViewAllAdapter.ViewHolder>() {

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
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun removeItem(item: PostItem) {
        items.remove(item)
        notifyDataSetChanged()
    }

    fun removeAllItems() {
        items.removeAll { true }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(private val view: View, private val listener: OpenPostListener)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var item: PostItem

        init {
            view.itemParent.setOnClickListener(this)
            view.ivBookmark.setOnClickListener(this)
        }

        fun bind(item: PostItem) {
            this.item = item
            view.ivItemBg.load(item.source_url)
            view.tvItemTitle.setHtml(item.title.rendered)
            view.tvItemDate.text = Date().getDateFromString(item.date)?.getCalendarDate()
            view.tvItemTag.text = item.categoryName
            if (Preference(view.context).getBookmarkedNews().contains(item)){
                view.ivBookmark.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_bookmark_red))
            } else {
                view.ivBookmark.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_bookmark))
            }
        }

        override fun onClick(v: View) {
            when(v) {
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