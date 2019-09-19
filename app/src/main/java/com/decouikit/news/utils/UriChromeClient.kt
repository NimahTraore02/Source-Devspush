package com.decouikit.news.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.decouikit.news.activities.PostActivity

class UriChromeClient(private val activity: PostActivity, val listener: FullscreenInterface) : WebChromeClient() {

    private var mCustomView: View? = null
    private var mCustomViewCallback: CustomViewCallback? = null
    private var mOriginalSystemUiVisibility: Int = 0

    override fun getDefaultVideoPoster(): Bitmap? {
        return if (mCustomView == null) {
            null
        } else BitmapFactory.decodeResource(activity.resources, 213083767)
    }


    override fun onHideCustomView() {
        listener.hideFullscreen()
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
        listener.showFullscreen()
        if (this.mCustomView != null) {
            onHideCustomView()
            return
        }
        this.mCustomView = paramView
        this.mOriginalSystemUiVisibility = activity.window.decorView.systemUiVisibility
        this.mCustomViewCallback = paramCustomViewCallback
        (activity.window.decorView as ViewGroup).addView(
            this.mCustomView,
            FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )
        activity.window.decorView.systemUiVisibility = 3846
    }

    interface FullscreenInterface {
        fun showFullscreen()
        fun hideFullscreen()
    }
}