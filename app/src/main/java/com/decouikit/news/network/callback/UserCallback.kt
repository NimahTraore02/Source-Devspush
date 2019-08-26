package com.decouikit.news.network.callback

import com.decouikit.news.network.dto.Category
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserCallback : Callback<List<Category>> {
    override fun onFailure(call: Call<List<Category>>?, t: Throwable?) {

    }

    override fun onResponse(call: Call<List<Category>>?, response: Response<List<Category>>?) {

    }
}
