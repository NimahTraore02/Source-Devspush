package com.decouikit.news.network.sync

import android.content.Context
import android.util.Log
import com.decouikit.news.network.RetrofitClientInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object SyncTags2 {
    suspend fun getTagList(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = getServices(context)?.getTagList()?.awaitResponse()
            if (response?.isSuccessful == true) {
                Log.e("TEST", "TAGs:${response.body()?.size}")
            } else {
                Log.e("TEST", "TAGs:${response?.errorBody()}")
            }
        }
    }

    private suspend fun getServices(context: Context): TagService2? {
        return RetrofitClientInstance.getRetrofitInstance(context)?.create(TagService2::class.java)
    }
}
