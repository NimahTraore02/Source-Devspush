package com.decouikit.news.adapters.holder

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
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

    fun bind(item: Category, position: Int) {
        this.item = item
        view.tvCategoryName.text = item.name
        view.tvCategoryPostsNumber.text =
            itemView.context.resources
                .getQuantityString(R.plurals.numberOfPosts, item.count, item.count)
//
//        val color_arr =
//            intArrayOf(Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GREEN, Color.RED, Color.BLACK, Color.DKGRAY)
//
//        view.ivBg.setBackgroundColor(color_arr[position])
    }

    override fun onClick(v: View?) {
        listener.onCategoryItemClick(item)
    }
}