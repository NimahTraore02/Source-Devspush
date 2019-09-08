package com.decouikit.news.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.decouikit.news.R
import com.decouikit.news.database.Preference
import com.decouikit.news.extensions.getCalendarDate
import com.decouikit.news.extensions.getDateFromString
import com.decouikit.news.extensions.load
import com.decouikit.news.extensions.setBookmarkIcon
import com.decouikit.news.interfaces.AddBookmarkListener
import com.decouikit.news.network.dto.MediaItem
import com.decouikit.news.network.dto.PostItem
import kotlinx.android.synthetic.main.adapter_featured_news_item.view.*
import kotlinx.android.synthetic.main.adapter_featured_news_item.view.ivItemBg
import kotlinx.android.synthetic.main.adapter_featured_news_item.view.tvItemDate
import kotlinx.android.synthetic.main.adapter_featured_news_item.view.tvItemTitle
import java.util.*

class FeaturedNewsAdapter(private var postItems: List<PostItem>,
                          private var context: Context,
                          private var listener: AddBookmarkListener)
    : PagerAdapter(), View.OnClickListener {

    private lateinit var view: View

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
        initLayout(view, position)
        initListener(view)
        container.addView(view, 0)
        this.view = view
        return view
    }

    private fun initLayout(view: View, position: Int) {
        view.tvTag.text = postItems[position].categoryName
        view.tvItemTitle.text = postItems[position].title.rendered
        view.tvItemDate.text = Date().getDateFromString(postItems[position].date)?.getCalendarDate()
        view.ivItemBg.load(postItems[position].source_url)
        for (bookmarkItem in Preference(view.context).getBookmarkedNews()) {
            if (postItems[position].id == bookmarkItem.id) {
                view.ivBookmark.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_bookmark_red))
            }else {
                view.ivBookmark.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_bookmark))
            }
        }
    }

    private fun initListener(view: View) {
        view.ivBookmark.setOnClickListener(this)
    }

    fun setBookmarkIconColor(isBookmarked: Boolean) {
        view.ivBookmark.setBookmarkIcon(isBookmarked)
    }

    override fun onClick(v: View) {
        when(v) {
            v.ivBookmark -> {
//                val postItem = Preference(v.context).getBookmarkByPostId(postItems[position].id)
//                if(postItem == null) {
//                    Preference(v.context).addBookmark(postItems[position])
//                } else {
//                    Preference(v.context).removeBookmark(postItems[position])
//                }
//                v.ivBookmark.setBookmarkIcon(postItem == null)
                listener.addBookmark(postItems)
            }
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}