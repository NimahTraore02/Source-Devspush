package com.decouikit.news.utils

import android.content.Context
import android.text.TextUtils
import android.widget.ImageView
import com.decouikit.news.R
import com.decouikit.news.interfaces.ResultListener
import com.decouikit.news.network.dto.MediaItem
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.network.sync.SyncMedia
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_bottom_navigation.*
import org.jetbrains.anko.runOnUiThread

object ImageLoadingUtil {

    fun load(url: String?, imageView: ImageView?, isRounded: Boolean = false) {
        imageView?.context?.runOnUiThread {
            if (!isRounded) {
                Picasso.get().load(url).into(imageView)
            } else {
                Picasso.get().load(url).transform(CircleTransform()).into(imageView);
            }
        }
    }

    fun load(context: Context, postItem: PostItem, imageView: ImageView?) {
        if (TextUtils.isEmpty(postItem.source_url)) {
            SyncMedia.getMediaById(
                postItem.featured_media,
                context,
                object : ResultListener<MediaItem> {
                    override fun onResult(value: MediaItem?) {
                        value?.let {
                            postItem.source_url = it.source_url
                            load(it.source_url, imageView)
                        }
                    }
                })
        } else {
            load(postItem.source_url, imageView)
        }
    }
}