package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import kotlinx.android.synthetic.main.adapter_view_all_item.view.*

class ViewAllAdapter(private val items: ArrayList<String>): RecyclerView.Adapter<ViewAllAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_view_all_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: String) {
            view.tvItemTag.text = item
        }
    }
}