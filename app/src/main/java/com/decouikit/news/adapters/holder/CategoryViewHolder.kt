package com.decouikit.news.adapters.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.interfaces.OnCategoryItemClickListener
import com.decouikit.news.network.dto.Category
import kotlinx.android.synthetic.main.adapter_category_item.view.*

class CategoryViewHolder(
    private val view: View,
    private val listener: OnCategoryItemClickListener
) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private lateinit var item: Category

    init {
        view.setOnClickListener(this)
    }

    fun bind(item: Category) {
        this.item = item
        view.tvCategoryName.text = item.name
        view.tvCategoryPostsNumber.text =
            itemView.context.resources
                .getQuantityString(R.plurals.numberOfPosts, item.count, item.count)
    }

    override fun onClick(v: View?) {
        listener.onCategoryItemClick(item)
    }
}