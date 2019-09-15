package com.decouikit.news.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

class ParallaxScrollView : ScrollView {

    private var mOnScrollChangedListener: OnScrollChangedListener? = null

    interface OnScrollChangedListener {
        fun onScrollChanged(deltaX: Int, deltaY: Int)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener!!.onScrollChanged(l - oldl, t - oldt)
        }
    }

    fun setOnScrollChangedListener(listener: OnScrollChangedListener) {
        mOnScrollChangedListener = listener
    }
}