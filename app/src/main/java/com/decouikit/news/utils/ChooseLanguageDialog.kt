package com.decouikit.news.utils

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.decouikit.news.R
import com.decouikit.news.interfaces.ChooseLanguageDialogListener

class ChooseLanguageDialog(
    context: Context,
    titleText: String,
    dialogLanguageList: Array<CharSequence>,
    selectedIndex: Int,
    callback: ChooseLanguageDialogListener
) : AlertDialog(context) {
    private val alertDialog: AlertDialog

    init {
        val builder = Builder(context)
        val foregroundColorSpan =
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        val ssBuilder = SpannableStringBuilder(titleText)
        ssBuilder.setSpan(
            foregroundColorSpan,
            0,
            titleText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        builder.setTitle(ssBuilder)
        builder.setSingleChoiceItems(dialogLanguageList, selectedIndex) { _, item ->
            run {
                callback.onLanguageItemChecked(item)
                alertDialog.dismiss()
            }
        }
        alertDialog = builder.create()
        alertDialog.show()
    }
}