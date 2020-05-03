package com.decouikit.news.adapters.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.extensions.*
import com.decouikit.news.interfaces.OpenPostListener
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.utils.ImageLoadingUtil
import kotlinx.android.synthetic.main.adapter_view_all_item.view.*
import java.util.*

class ViewAllViewHolder(private val view: View, private val listener: OpenPostListener) :
    RecyclerView.ViewHolder(view), View.OnClickListener {

    private lateinit var item: PostItem

    init {
        view.itemParent.setOnClickListener(this)
        view.ivBookmark.setOnClickListener(this)
    }

    fun bind(item: PostItem) {
        this.item = item
        ImageLoadingUtil.load(view.context, item, view.ivItemBg)
        view.tvItemTitle.setHtml(item.title.rendered)
        view.tvItemDate.text = Date().getDateFromString(item.date)?.getCalendarDate()
        view.tvItemTag.loadCategoryName(item)
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