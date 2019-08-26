package com.decouikit.news.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.decouikit.news.R
import com.decouikit.news.activities.base.MainActivity
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.network.CategoryService
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.UserService

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val userService = RetrofitClientInstance.retrofitInstance?.create(UserService::class.java)
        val categoryService = RetrofitClientInstance.retrofitInstance?.create(CategoryService::class.java)

        userService?.getUserList()?.enqueue(result = { it ->
            when (it) {
                is Result.Success -> {
                    InMemory.setUserList(it.response.body())
                }
            }
            categoryService?.getCategoryList()?.enqueue(result = {
                //we call the new enqueue
                when (it) {
                    is Result.Success -> {
                        InMemory.setCategoryList(it.response.body())
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            })
        })
    }
}