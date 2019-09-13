package com.decouikit.news.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerOnScrollListener(val adapter: RecyclerView.Adapter<*>) : RecyclerView.OnScrollListener() {

    /**
     * The total number of items in the dataset after the last load
     */
    private var mPreviousTotal = 0
    /**
     * True if we are still waiting for the last set of data to load.
     */
    private var mLoading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if(dy > 0) {
            val abstractTotalCount = adapter.itemCount
            val totalItemCount =  recyclerView.layoutManager!!.itemCount
            val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

            if (mLoading) {
                if (abstractTotalCount > mPreviousTotal) {
                        mLoading = false
                        mPreviousTotal = abstractTotalCount
                    }
            }

            if (!mLoading && lastVisibleItemPosition == totalItemCount-1) {
                onLoadMore()
                mLoading = true
            }
        }
    }

    abstract fun onLoadMore()
}