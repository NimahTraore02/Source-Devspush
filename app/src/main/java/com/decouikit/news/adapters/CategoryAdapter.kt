package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.network.dto.Category
import kotlinx.android.synthetic.main.adapter_category_item.view.*

class CategoryAdapter(private val items: ArrayList<Category>): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_category_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }


    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Category) {
            view.tvCategoryName.text = item.name
            if(item.count == 1) {
                view.tvCategoryPostsNumber.text = view.context.getString(R.string.post)
            }else {
                view.tvCategoryPostsNumber.text = view.context.getString(R.string.posts, item.count)
            }
        }
    }
}