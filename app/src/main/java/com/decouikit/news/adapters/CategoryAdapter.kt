package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.adapters.holder.CategoryViewHolder
import com.decouikit.news.interfaces.OnCategoryItemClickListener
import com.decouikit.news.network.dto.Category

class CategoryAdapter(
    private val items: ArrayList<Category>,
    private val listener: OnCategoryItemClickListener
) : RecyclerView.Adapter<CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_category_item, parent, false), listener
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) =
        holder.bind(items[position])
}