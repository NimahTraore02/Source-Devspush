package com.decouikit.news.utils

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.decouikit.news.R
import com.decouikit.news.adapters.common.CommonListAdapterType
import kotlinx.android.synthetic.main.dialog_list_settings.view.*

object ChooseListStyleDialog {

    fun showDialog(activity: Activity, listType: ListType, callback: OnListTypeChangeListener) {
        var dialog: AlertDialog? = null
        val dialogBuilder = AlertDialog.Builder(activity)
        val dialogView = activity.layoutInflater.inflate(R.layout.dialog_list_settings, null)
        dialogBuilder.setView(dialogView)

        dialogView.listType1.setOnClickListener{
            callback.onListTypeChange(CommonListAdapterType.ADAPTER_VERSION_1, listType)
            dialog?.cancel()
        }
        dialogView.listType2.setOnClickListener{
            callback.onListTypeChange(CommonListAdapterType.ADAPTER_VERSION_3, listType)
            dialog?.cancel()
        }
        dialogView.listType3.setOnClickListener{
            callback.onListTypeChange(CommonListAdapterType.ADAPTER_VERSION_2, listType)
            dialog?.cancel()
        }

        dialog = dialogBuilder.show()
    }

}