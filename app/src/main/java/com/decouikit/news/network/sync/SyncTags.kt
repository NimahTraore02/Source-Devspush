package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.InMemory
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.TagService
import com.decouikit.news.network.dto.Tag
import retrofit2.awaitResponse

object SyncTags {

    suspend fun getTagById(id: Int, context: Context): Tag? {
        val service = getService(context)
        val tag = InMemory.getTagById(id)
        if (tag != null) {
            return tag
        } else {
            val response = service?.getTagById(id.toString())?.awaitResponse()
            if (response?.isSuccessful == true) {
                try {
                    val tagItem = response.body() as Tag
                    InMemory.addTag(context, tagItem)
                    return tagItem
                } catch (e: Exception) { }
            }

            return null
        }
    }

    private fun getService(context: Context): TagService? {
        return RetrofitClientInstance
            .getRetrofitInstance(context)
            ?.create(TagService::class.java)
    }
}
