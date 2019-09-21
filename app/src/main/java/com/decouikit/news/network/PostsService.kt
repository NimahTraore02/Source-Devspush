package com.decouikit.news.network

import com.decouikit.news.network.dto.PostItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostsService {
    @GET("posts?")
    fun getPostsList(@Query("page") page: Int = 1, @Query("per_page") per_page: Int = 30): Call<List<PostItem>>

    @GET("posts/")
    fun getPostsByCategory(@Query("categories") categories: String, @Query("page") page: Int = 1, @Query("per_page") per_page: Int = 30): Call<List<PostItem>>

    @GET("posts/{id}")
    fun getPostsById(@Path("id") id: String): Call<PostItem>
}