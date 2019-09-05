package com.decouikit.news.extensions

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun View.pxToDp(px: Int): Int {
    return (px / resources.displayMetrics.density).toInt()
}

fun View.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}


fun ImageView.load(imageUrl: String, isRounded: Boolean = false) {
    if (isRounded) {
        Glide.with(context)
            .load(imageUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(this)
    } else {
        Glide
            .with(this)
            .load(imageUrl)
            .into(this)
    }
}