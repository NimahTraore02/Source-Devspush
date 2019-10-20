package com.decouikit.news.network

import android.content.Context
import com.decouikit.news.database.Config
import com.decouikit.news.database.InMemory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstance {

    private var retrofit: Retrofit? = null

    fun getRetrofitInstance(context: Context): Retrofit? {
        if (retrofit == null) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)  // <-- this is the important line!

            val lang = Config.getBaseUrl(context)
            if (lang != null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(lang.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build()
            }
        }
        return retrofit
    }

    fun clear() {
        retrofit = null
    }
}