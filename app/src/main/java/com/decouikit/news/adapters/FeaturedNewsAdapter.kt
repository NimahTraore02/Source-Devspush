package com.decouikit.news.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.decouikit.news.R
import com.decouikit.news.extensions.getCalendarDate
import com.decouikit.news.extensions.getDateFromString
import com.decouikit.news.extensions.load
import com.decouikit.news.network.dto.MediaItem
import com.decouikit.news.network.dto.PostItem
import kotlinx.android.synthetic.main.adapter_featured_news_item.view.*
import kotlinx.android.synthetic.main.adapter_featured_news_item.view.ivItemBg
import kotlinx.android.synthetic.main.adapter_featured_news_item.view.tvItemDate
import kotlinx.android.synthetic.main.adapter_featured_news_item.view.tvItemTitle
import java.util.*

class FeaturedNewsAdapter(private var postItems: List<PostItem>,
                          private var context: Context) : PagerAdapter() {

    override fun getCount(): Int {
        return postItems.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater
                .from(context)
                .inflate(R.layout.adapter_featured_news_item, container, false)

        view.tvTag.text = postItems[position].categoryName
        view.tvItemTitle.text = postItems[position].title.rendered
        view.tvItemDate.text = Date().getDateFromString(postItems[position].date)?.getCalendarDate()
        view.ivItemBg.load(postItems[position].source_url)

        container.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}