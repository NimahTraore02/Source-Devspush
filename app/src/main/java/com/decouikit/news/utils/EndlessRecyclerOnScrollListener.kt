package com.decouikit.news.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
abstract class EndlessRecyclerOnScrollListener : RecyclerView.OnScrollListener() {
    //    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

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

        if(dy > 0) //check for scroll down
        {
            val abstractTotalCount = recyclerView.adapter?.itemCount
            val totalItemCount =  recyclerView.layoutManager!!.itemCount
            val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

            if (mLoading) {
                if (abstractTotalCount != null) {
                    if (abstractTotalCount > mPreviousTotal) {
                        mLoading = false
                        mPreviousTotal = abstractTotalCount
                    }
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