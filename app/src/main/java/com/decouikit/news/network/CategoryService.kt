package com.decouikit.news.network

import com.decouikit.news.network.dto.Category
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryService {
    @GET("categories?")
    fun getCategoryList(@Query("page") page: Int = 1, @Query("per_page") per_page: Int = 100): Call<List<Category>>

    @GET("categories/{id}")
    fun getCategoryById(@Path("id") id: String): Call<Category>
}