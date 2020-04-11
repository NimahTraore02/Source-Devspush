package com.decouikit.news.extensions

import android.graphics.Matrix
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.decouikit.news.R
import com.decouikit.news.database.InMemory
import com.decouikit.news.network.dto.PostItem

fun ImageView.setBookmarkIcon(isBookmarked: Boolean) {
    if (isBookmarked) {
        this.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_bookmark_red))
    } else {
        this.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_bookmark))
    }
}

fun ImageView.setBookmarkIcon(postItem: PostItem) {
    setBookmarkIcon(InMemory.getBookmarkById(postItem.id) != null)
}

fun ImageView.rotate(angle: Float, imageView: ImageView) {
    val matrix = Matrix()
    imageView.scaleType = ImageView.ScaleType.MATRIX   //required
    matrix.postRotate(angle, pivotX, pivotY)
    imageView.imageMatrix = matrix
}