package com.decouikit.news.extensions

import android.app.Dialog
import android.view.View
import android.view.Window
import com.decouikit.news.R

fun Dialog.initPopupDialog() {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    setContentView(R.layout.dialog_popup)
    setCancelable(false)
    setCanceledOnTouchOutside(false)
}
