package com.decouikit.news.utils

import com.decouikit.news.adapters.common.CommonListAdapterType

object AdapterListTypeUtil {

    fun getAdapterTypeFromValue(value: Int): CommonListAdapterType {
        return when(value) {
            CommonListAdapterType.ADAPTER_VERSION_1.id -> {
                CommonListAdapterType.ADAPTER_VERSION_1
            }
            CommonListAdapterType.ADAPTER_VERSION_2.id -> {
                CommonListAdapterType.ADAPTER_VERSION_2
            }
            CommonListAdapterType.ADAPTER_VERSION_3.id -> {
                CommonListAdapterType.ADAPTER_VERSION_3
            }
            else -> CommonListAdapterType.ADAPTER_VERSION_1
        }
    }
}