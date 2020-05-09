package com.decouikit.news.adapters.common

import androidx.recyclerview.widget.RecyclerView
import com.decouikit.news.adapters.RecentNewsAdapter
import com.decouikit.news.adapters.ViewAllAdapter
import com.decouikit.news.adapters.holder.RecentNewsViewHolder

object AdapterFactory {
    fun getRecentAdapterByType(adapterType: AdapterType): RecyclerView.Adapter<*> {
        return when (adapterType) {
            AdapterType.ADAPTER_VERSION_1 -> {
                ViewAllAdapter(arrayListOf())
            }
            AdapterType.ADAPTER_VERSION_2 -> {
                RecentNewsAdapter(arrayListOf())
            }
        }
    }
}