package com.decouikit.news.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import com.decouikit.news.R
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.*
import com.decouikit.news.network.dto.PostItem
import kotlinx.android.synthetic.main.adapter_featured_news_item.view.*
import java.util.*

class FeaturedNewsAdapter(
    private var postItems: List<PostItem>,
    private var context: Context
) : PagerAdapter() {

    private lateinit var view: View

    override fun getCount(): Int {
        return postItems.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        view = LayoutInflater
            .from(context)
            .inflate(R.layout.adapter_featured_news_item, container, false)
        initLayout(view, position)
        initListener(view, position)
        container.addView(view, 0)
        return view
    }

    private fun initLayout(view: View, position: Int) {
        view.tvTag.text = postItems[position].categoryName
        view.tvItemTitle.text = postItems[position].title.rendered
        view.tvItemDate.text = Date().getDateFromString(postItems[position].date)?.getCalendarDate()
        view.ivItemBg.load(postItems[position].source_url)
        if (Preference(view.context).getBookmarkedNews().contains(postItems[position])) {
            view.ivBookmark.setImageDrawable(
                ContextCompat.getDrawable(
                    view.context,
                    R.drawable.ic_bookmark_red
                )
            )
        } else {
            view.ivBookmark.setImageDrawable(
                ContextCompat.getDrawable(
                    view.context,
                    R.drawable.ic_bookmark
                )
            )
        }
    }

    private fun initListener(view: View, position: Int) {
        view.featuredParent.setOnClickListener {
            view.openPostActivity(view.context, postItems[position])
        }
        view.ivBookmark.setOnClickListener {
            it?.context?.let {
                view.bookmark(
                    view.context,
                    postItems[position],
                    view.ivBookmark
                )
                notifyDataSetChanged()
            }
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}