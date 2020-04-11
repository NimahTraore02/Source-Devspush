package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.extensions.getCalendarDate
import com.decouikit.news.extensions.getDateFromString
import com.decouikit.news.extensions.setHtml
import com.decouikit.news.network.dto.CommentItem
import com.decouikit.news.utils.ImageLoadingUtil
import kotlinx.android.synthetic.main.adapter_comment_item.view.*
import java.util.*

class CommentsAdapter(private var items: ArrayList<CommentItem>) :
    RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_comment_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setData(items: ArrayList<CommentItem>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun removeAllItems() {
        items.removeAll { true }
        notifyDataSetChanged()
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: CommentItem) {
            ImageLoadingUtil.load(item.author_avatar_urls.url48, view.ivAvatar, true)
            view.tvName.text = item.authorName
            view.tvCommentDate.text = Date().getDateFromString(item.date)?.getCalendarDate()
            view.tvCommentText.setHtml(item.content.rendered)
        }
    }
}