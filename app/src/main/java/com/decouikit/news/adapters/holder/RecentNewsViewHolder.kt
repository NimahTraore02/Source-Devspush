package com.decouikit.news.adapters.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.extensions.*
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.utils.ImageLoadingUtil
import kotlinx.android.synthetic.main.adapter_recent_news_item.view.*
import java.util.*


class RecentNewsViewHolder(private val view: View) : RecyclerView.ViewHolder(view),
    View.OnClickListener {

    private lateinit var item: PostItem

    init {
        view.setOnClickListener(this)
    }

    fun bind(item: PostItem) {
        this.item = item
        view.tvItemTag.loadCategoryName(item)
        view.tvItemTitle.setHtml(item.title.rendered)
        view.tvItemDate.text = Date().getDateFromString(item.date)?.getCalendarDate()
        ImageLoadingUtil.load(view.context, item, view.ivItemBg)
    }

    override fun onClick(v: View) {
        v.openPostActivity(v.context, item)
    }
}