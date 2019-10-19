package com.decouikit.news.network

import android.content.Context
import com.decouikit.news.database.Config
import com.decouikit.news.database.Preference
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstance {

    private var retrofit: Retrofit? = null

    fun getRetrofitInstance(context:Context):Retrofit? {
        if (retrofit == null) {
            val lang = Config.getBaseUrl(context)
            if (lang != null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(lang.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
        }
        return retrofit
    }

    fun clear() {
        retrofit = null
    }
}