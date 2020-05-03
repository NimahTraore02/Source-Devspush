package com.decouikit.news.adapters.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.extensions.getCalendarDate
import com.decouikit.news.extensions.getDateFromString
import com.decouikit.news.extensions.setHtml
import com.decouikit.news.network.dto.CommentItem
import com.decouikit.news.utils.ImageLoadingUtil
import kotlinx.android.synthetic.main.adapter_comment_item.view.*
import java.util.*

class CommentViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: CommentItem) {
        ImageLoadingUtil.load(item.author_avatar_urls.url48, view.ivAvatar, true)
        view.tvName.text = item.authorName
        view.tvCommentDate.text = Date().getDateFromString(item.date)?.getCalendarDate()
        view.tvCommentText.setHtml(item.content.rendered)
    }
}