package com.decouikit.news.adapters.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.interfaces.OnHashTagClickListener
import com.decouikit.news.network.dto.Tag
import kotlinx.android.synthetic.main.adapter_hashtag_item.view.*


class HashTagViewHolder(
    private val view: View,
    private val listener: OnHashTagClickListener
) :
    RecyclerView.ViewHolder(view), View.OnClickListener {

    private lateinit var tag: Tag

    init {
        view.tvHashTag.setOnClickListener(this)
    }

    fun bind(item: Tag) {
        tag = item
        view.tvHashTag.text = view.context.getString(R.string.hash_tag, item.name)
    }

    override fun onClick(v: View?) {
        listener.onHashtagClick(tag)
    }
}