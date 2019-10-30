package com.decouikit.news.extensions

import android.graphics.Matrix
import android.text.TextUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.decouikit.news.R
import com.decouikit.news.database.InMemory
import com.decouikit.news.interfaces.ResultListener
import com.decouikit.news.network.dto.MediaItem
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.network.sync.SyncMedia

//
// Created by Dragan Koprena on 10/22/19.
//
fun ImageView.load(imageUrl: String?, isRounded: Boolean = false) {
    if (imageUrl == null) {
        return
    }
    if (isRounded) {
        Glide.with(context)
            .load(imageUrl)
            .apply(RequestOptions.circleCropTransform())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
    } else {
        Glide
            .with(this)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
    }
}

fun ImageView.load(postItem: PostItem) {

    if (TextUtils.isEmpty(postItem.source_url)) {
        load("")
        SyncMedia.getMediaById(
            postItem.featured_media,
            context,
            object : ResultListener<MediaItem> {
                override fun onResult(value: MediaItem?) {
                    value?.let {
                        postItem.source_url = it.source_url
                        load(it.source_url)
                    }
                }
            })
    } else {
        load(postItem.source_url)
    }
}

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