package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.extensions.*
import com.decouikit.news.interfaces.RemoveBookmarkListener
import com.decouikit.news.network.dto.PostItem
import kotlinx.android.synthetic.main.adapter_view_all_item.view.*
import java.util.*

class BookmarkAdapter(private var items: ArrayList<PostItem>,
                      private var mListener: RemoveBookmarkListener)
    : RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_view_all_item, parent, false), mListener
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(items: ArrayList<PostItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(private val view: View,
                     private val mListener: RemoveBookmarkListener)
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
            view.ivBookmark.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_bookmark_red))
        }

        override fun onClick(v: View) {
            when (v) {
                view.itemParent -> {
                    v.openPostActivity(v.context, item)
                }
                view.ivBookmark -> {
                    mListener.removeBookmark(item)
                }
            }
        }
    }
}