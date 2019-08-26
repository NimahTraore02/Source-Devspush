package com.decouikit.news.network

import com.decouikit.news.network.dto.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("users?")
    fun getUserList(@Query("page") page: Int = 1, @Query("per_page") per_page: Int = 100): Call<List<User>>

    @GET("users/")
    fun getUserById(@Query("id") id: String): Call<User>
}