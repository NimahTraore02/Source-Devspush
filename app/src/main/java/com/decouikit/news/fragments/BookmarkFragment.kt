package com.decouikit.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.decouikit.news.R
import com.decouikit.news.adapters.BookmarkAdapter
import com.decouikit.news.database.Preference
import com.decouikit.news.interfaces.RemoveBookmarkListener
import com.decouikit.news.network.dto.PostItem
import kotlinx.android.synthetic.main.fragment_bookmark.view.*

class BookmarkFragment : Fragment(), View.OnClickListener, RemoveBookmarkListener {

    private lateinit var itemView: View
    private lateinit var adapter: BookmarkAdapter
    private lateinit var bookmarkedList: ArrayList<PostItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        itemView = inflater.inflate(R.layout.fragment_bookmark, container, false)
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLayout()
        initListeners()
    }

    private fun initLayout() {
        bookmarkedList = Preference(itemView.context).getBookmarkedNews()
        adapter = BookmarkAdapter(bookmarkedList, this)
        itemView.rvItems.layoutManager = LinearLayoutManager(itemView.context)
        itemView.rvItems.adapter = adapter
    }

    private fun initListeners() {
        itemView.tvClearAll.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v) {
            itemView.tvClearAll -> {
                bookmarkedList.removeAll { true }
                Preference(itemView.context).setBookmarkedNews(arrayListOf())
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun removeBookmark(item: PostItem) {
        bookmarkedList.remove(item)
        Preference(itemView.context).setBookmarkedNews(bookmarkedList)
        adapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(): BookmarkFragment {
            return BookmarkFragment()
        }
    }
}