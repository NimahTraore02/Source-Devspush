package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.extensions.*
import com.decouikit.news.network.dto.PostItem
import kotlinx.android.synthetic.main.adapter_recent_news_item.view.*
import java.util.*

class RecentNewsAdapter(private var items: ArrayList<PostItem>)
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

    fun setData(items: ArrayList<PostItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun removeAllItems() {
        items.removeAll { true }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var item: PostItem

        init {
            view.setOnClickListener(this)
        }

        fun bind(item: PostItem) {
            this.item = item
            view.tvItemTag.text = item.categoryName
            view.tvItemTitle.setHtml(item.title.rendered)
            view.tvItemDate.text = Date().getDateFromString(item.date)?.getCalendarDate()
            view.ivItemBg.load(item)
        }

        override fun onClick(v: View) {
            v.openPostActivity(v.context, item)
        }
    }
}