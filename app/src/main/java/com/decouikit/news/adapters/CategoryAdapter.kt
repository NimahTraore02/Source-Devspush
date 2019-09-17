package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.interfaces.OnCategoryItemClickListener
import com.decouikit.news.network.dto.Category
import kotlinx.android.synthetic.main.adapter_category_item.view.*

class CategoryAdapter(
    private val items: ArrayList<Category>,
    private val listener: OnCategoryItemClickListener
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_category_item, parent, false), listener
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(
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
                    .getQuantityString(R.plurals.numberOfPosts, item.count, item.count);
        }

        override fun onClick(v: View?) {
            listener.onCategoryItemClick(item)
        }
    }
}