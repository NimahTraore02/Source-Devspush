package com.decouikit.news.network

import com.decouikit.news.network.dto.CommentItem
import com.decouikit.news.network.dto.CommentRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CommentsService {
    @GET("comments?")
    fun getCommentListPostId(
        @Query("post") post: Int,
        @Query("page") page: Int = 1,
        @Query("per_page") per_page: Int = 30
    ): Call<List<CommentItem>>

    @POST("comments")
    fun saveComment(@Body body: CommentRequest): Call<CommentItem>
}