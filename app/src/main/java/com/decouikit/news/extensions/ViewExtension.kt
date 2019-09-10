package com.decouikit.news.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.decouikit.news.R
import com.decouikit.news.activities.CommentsActivity
import com.decouikit.news.activities.PostActivity
import com.decouikit.news.database.Preference
import com.decouikit.news.network.dto.PostItem
import com.decouikit.news.utils.NewsConstants

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

fun ImageView.setBookmarkIcon(isBookmarked: Boolean) {
    if(isBookmarked) {
        this.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_bookmark_red))
    }
    else {
        this.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_bookmark))
    }
}

fun View.bookmark(context: Context, postItem: PostItem, imageView: ImageView) {
    val item = Preference(context).getBookmarkByPostId(postItem.id)
    if(item == null) {
        Preference(context).addBookmark(postItem)
    } else {
        Preference(context).removeBookmark(postItem)
    }
    imageView.setBookmarkIcon(item == null)
}

fun View.openPostActivity(context: Context, item: PostItem) {
    val intent = Intent(context, PostActivity::class.java)
    intent.putExtra(NewsConstants.POST_ITEM, com.google.gson.Gson().toJson(item))
    context.startActivity(intent)
}

fun View.openComments(context: Context, cls: Class<*>, postId: Int) {
    val intent = Intent(context, cls)
    intent.putExtra(NewsConstants.POST_ITEM_ID, postId)
    context.startActivity(intent)
}

fun TextView.setHtml(textView: TextView, content: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        textView.text = Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT)
    } else {
        textView.text = Html.fromHtml(content)
    }
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