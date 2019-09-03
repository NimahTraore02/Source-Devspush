package com.decouikit.news.extensions

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

inline fun <reified T> Call<T>.enqueue(crossinline result: (Result<T>) -> Unit) {
    enqueue(object: Callback<T> {
        override fun onFailure(call: Call<T>, error: Throwable) {
            result(Result.Failure(call, error))
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            result(Result.Success(call, response))
        }
    })
}

public open class Result<T> {
    data class Success<T>(val call: Call<T>, val response: Response<T>) : Result<T>()
    data class Failure<T>(val call: Call<T>, val error: Throwable) : Result<T>()
}