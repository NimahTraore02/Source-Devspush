package com.decouikit.news.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.webkit.WebChromeClient
import android.widget.FrameLayout
import com.decouikit.news.activities.PostActivity

class UriChromeClient(private val activity: PostActivity) : WebChromeClient() {

    private var mCustomView: View? = null
    private var mCustomViewCallback: CustomViewCallback? = null
    private var mOriginalSystemUiVisibility: Int = 0

    override fun getDefaultVideoPoster(): Bitmap? {
        return if (mCustomView == null) {
            null
        } else BitmapFactory.decodeResource(activity.resources, 213083767)
    }


    override fun onHideCustomView() {
        (activity.window.decorView as FrameLayout).removeView(this.mCustomView)
        this.mCustomView = null
        activity.window.decorView.systemUiVisibility = this.mOriginalSystemUiVisibility
        this.mCustomViewCallback?.onCustomViewHidden()
        this.mCustomViewCallback = null
    }

    override fun onShowCustomView(
        paramView: View,
        paramCustomViewCallback: CustomViewCallback
    ) {
        if (this.mCustomView != null) {
            onHideCustomView()
            return
        }
        this.mCustomView = paramView
        this.mOriginalSystemUiVisibility = activity.window.decorView.systemUiVisibility
        this.mCustomViewCallback = paramCustomViewCallback
        (activity.window.decorView as FrameLayout).addView(
            this.mCustomView,
            FrameLayout.LayoutParams(-1, -1)
        )
        activity.window.decorView.systemUiVisibility = 3846
    }
}