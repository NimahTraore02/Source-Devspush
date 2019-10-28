package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.interfaces.OnHashTagClickListener
import com.decouikit.news.network.dto.Tag
import kotlinx.android.synthetic.main.adapter_hashtag_item.view.*

class HashTagAdapter(private var items: ArrayList<Tag>,
                     private var listener: OnHashTagClickListener) :
    RecyclerView.Adapter<HashTagAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_hashtag_item, parent, false), listener
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }


    class ViewHolder(private val view: View,
                     private val listener: OnHashTagClickListener) :
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
}