package com.decouikit.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.R
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.getCalendarDate
import com.decouikit.news.extensions.getDateFromString
import com.decouikit.news.extensions.load
import com.decouikit.news.extensions.setBookmarkIcon
import com.decouikit.news.network.dto.PostItem
import kotlinx.android.synthetic.main.adapter_view_all_item.view.*
import java.util.*

class ViewAllAdapter(private var items: ArrayList<PostItem>)
    : RecyclerView.Adapter<ViewAllAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_view_all_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(items: ArrayList<PostItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(private val view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var item: PostItem

        init {
            view.itemParent.setOnClickListener(this)
            view.ivBookmark.setOnClickListener(this)
        }

        fun bind(item: PostItem) {
            this.item = item
            view.ivItemBg.load(item.source_url)
            view.tvItemTitle.text = item.title.rendered
            view.tvItemDate.text = Date().getDateFromString(item.date)?.getCalendarDate()
            view.tvItemTag.text = item.categoryName

            for (bookmarkItem in Preference(view.context).getBookmarkedNews()) {
                if (item.id == bookmarkItem.id) {
                    view.ivBookmark.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_bookmark_red))
                }else {
                    view.ivBookmark.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_bookmark))
                }
            }
        }

        override fun onClick(v: View) {
            when(v) {
                view.itemParent -> {
                    Toast.makeText(v.context, "Otvori detalje",Toast.LENGTH_LONG).show()
                }
                view.ivBookmark -> {
                    val postItem = Preference(v.context).getBookmarkByPostId(item.id)
                    if(postItem == null) {
                        Preference(v.context).addBookmark(item)
                    } else {
                        Preference(v.context).removeBookmark(item)
                    }
                    view.ivBookmark.setBookmarkIcon(postItem == null)
                }
            }
        }
    }
}