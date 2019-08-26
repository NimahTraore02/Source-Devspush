package com.decouikit.news.network

import com.decouikit.news.network.dto.Category
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MediaService {
    @GET("media?")
    fun getMediaList(@Query("page") page: Int = 1, @Query("per_page") per_page: Int = 100): Call<List<Category>>

    @GET("media/")
    fun getCategoryById(@Query("id") id: String): Call<Category>
}