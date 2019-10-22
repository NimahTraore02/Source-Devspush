package com.decouikit.news.extensions

import android.content.Context
import android.content.Intent
import android.graphics.Matrix
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.decouikit.news.R
import com.decouikit.news.activities.PostActivity
import com.decouikit.news.activities.ViewAllActivity
import com.decouikit.news.database.Preference
import com.decouikit.news.network.MediaService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.dto.MediaItem
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.utils.NewsConstants
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.doAsyncResult


fun View.pxToDp(px: Int): Int {
    return (px / resources.displayMetrics.density).toInt()
}

fun View.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}


fun ImageView.load(imageUrl: String?, isRounded: Boolean = false) {
    if (imageUrl == null) {
        return
    }
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

fun ImageView.load(postItem: PostItem) {
    if (TextUtils.isEmpty(postItem.source_url)) {
        val mediaService = RetrofitClientInstance.getRetrofitInstance(context)?.create(MediaService::class.java)
        mediaService?.getMediaById(postItem.featured_media.toString())?.enqueue {
            when (it) {
                is Result.Success -> {
                    try {
                        val mediaItem = it.response.body() as MediaItem
                        postItem.source_url = mediaItem.source_url
                        load(mediaItem.source_url)
                    } catch (e: Exception) {
                        Log.e("TEST", "Image error")
                    }
                }
            }
        }
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

fun View.bookmark(context: Context, postItem: PostItem, imageView: ImageView) {
    val item = Preference(context).getBookmarkByPostId(postItem.id)
    if (item == null) {
        Preference(context).addBookmark(postItem)
    } else {
        Preference(context).removeBookmark(postItem)
    }
    imageView.setBookmarkIcon(item == null)
}

fun View.viewAll(context: Context, categoryId: Int?, categoryName: String, categoryType: Int? = 0) {
    val intent = Intent(context, ViewAllActivity::class.java)
    intent.putExtra(NewsConstants.CATEGORY_ID, categoryId)
    intent.putExtra(NewsConstants.CATEGORY_NAME, categoryName)
    intent.putExtra(NewsConstants.CATEGORY_FEATURES, categoryType)
    context.startActivity(intent)
}

fun View.openPostActivity(context: Context, item: PostItem) {
    val intent = Intent(context, PostActivity::class.java)
    intent.putExtra(NewsConstants.POST_ITEM, Gson().toJson(item))
    context.startActivity(intent)
}

fun View.openComments(context: Context, cls: Class<*>, postId: Int) {
    val intent = Intent(context, cls)
    intent.putExtra(NewsConstants.POST_ITEM_ID, postId)
    context.startActivity(intent)
}

fun View.openExternalApp(url: String) {
    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

fun ImageView.rotate(angle: Float, imageView: ImageView) {
    val matrix = Matrix()
    imageView.scaleType = ImageView.ScaleType.MATRIX   //required
    matrix.postRotate(angle, pivotX, pivotY)
    imageView.imageMatrix = matrix
}

fun View.share(context: Context, item: PostItem) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = NewsConstants.TEXT_PLAIN
    intent.putExtra(Intent.EXTRA_TEXT, item.link)
    intent.putExtra(Intent.EXTRA_SUBJECT, item.title.rendered)
    context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_via)))
}

fun TextView.setHtml(content: String) {
    this.text = content.fromHtmlToString()
}

fun View.validationCommon(editText: EditText, error: Int): Boolean {
    return if (editText.text.toString().isEmpty()) {
        editText.error = editText.context.getString(error)
        false
    } else {
        editText.error = null
        true
    }
}

fun View.validationOfEmail(editText: EditText): Boolean {
    return if (editText.text.toString().length > 4) {
        if (!editText.text.toString().matches(NewsConstants.EMAIL_REGEX.toRegex())) {
            editText.error = editText.context.getString(R.string.email_validation_format)
            false
        } else {
            editText.error = null
            true
        }
    } else {
        if (editText.text.toString().isEmpty()) {
            editText.error = editText.context.getString(R.string.email_validation_empty)
            false
        } else {
            editText.error = editText.context.getString(R.string.email_validation_short)
            false
        }
    }
}