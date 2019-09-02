package com.decouikit.news.network

import com.decouikit.news.network.dto.CommentItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CommentsService {
    @GET("comments?")
    fun getCommentListPostId(
        @Query("post") post: String,
        @Query("page") page: Int = 1,
        @Query("per_page") per_page: Int = 30
    ): Call<List<CommentItem>>
}