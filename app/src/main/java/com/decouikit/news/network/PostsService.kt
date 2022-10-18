package com.decouikit.news.network

import com.decouikit.news.database.Config
import com.decouikit.news.network.dto.PostItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostsService {
    @GET("job_listing?")
    fun getPostsList(
        @Query("categories") categories: String?,
        @Query("sticky") sticky: Boolean?,
        @Query("page") page: Int = 1,
        @Query("per_page") per_page: Int = Config.getNumberOfItemPerPage(),
        @Query("order") order: String = Config.getOrder()
    ): Call<List<PostItem>>

    @GET("job_listing")
    fun getPostsSearch(
        @Query("search") search: String,
        @Query("tags") tags: Int?,
        @Query("page") page: Int = 1,
        @Query("per_page") per_page: Int = Config.getNumberOfItemPerPage()
    ): Call<List<PostItem>>

    @GET("job_listing/{id}")
    fun getPostsById(
        @Path("id") id: String
    ): Call<PostItem>
}