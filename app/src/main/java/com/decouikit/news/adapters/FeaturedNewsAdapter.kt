package com.decouikit.news.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.decouikit.news.R
import com.decouikit.news.network.dto.MediaItem
import kotlinx.android.synthetic.main.adapter_featured_news_item.view.*

class FeaturedNewsAdapter(private var items: List<MediaItem>,
                          private var context: Context) : PagerAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater
                .from(context)
                .inflate(R.layout.adapter_featured_news_item, container, false)
        Glide.with(view.context).load(items[position].source_url).into(view.ivItemBg)
        container.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}