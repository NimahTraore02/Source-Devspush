package com.decouikit.news.network

import com.decouikit.news.network.dto.Tag
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TagService {
    @GET("tag?")
    fun getTagList(@Query("page") page: Int = 1, @Query("per_page") per_page: Int = 100): Call<List<Tag>>

    @GET("tags/{id}")
    fun getTagById(@Path("id") id: String): Call<Tag>
}