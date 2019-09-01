package com.decouikit.news.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.decouikit.news.R
import com.decouikit.news.activities.base.MainActivity
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.network.*

class SplashActivity : Activity() {

    private var requestCounter = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val userService = RetrofitClientInstance.retrofitInstance?.create(UserService::class.java)
        val categoryService = RetrofitClientInstance.retrofitInstance?.create(CategoryService::class.java)
        val tagService = RetrofitClientInstance.retrofitInstance?.create(TagService::class.java)
        val mediaService = RetrofitClientInstance.retrofitInstance?.create(MediaService::class.java)

        mediaService?.getMediaList()?.enqueue(result = {
            requestCounter--
            when (it) {
                is Result.Success -> {
                    if (it.response.body() != null) {
                        InMemory.setMediaList(it.response.body())
                    }
                }
            }
            openApp()
        })
        userService?.getUserList()?.enqueue(result = { it ->
            requestCounter--
            when (it) {
                is Result.Success -> {
                    if (it.response.body() != null) {
                        InMemory.setUserList(it.response.body())
                    }
                }
            }
            openApp()
        })
        categoryService?.getCategoryList()?.enqueue(result = {
            requestCounter--
            when (it) {
                is Result.Success -> {
                    if (it.response.body() != null) {
                        InMemory.setCategoryList(it.response.body())
                    }
                }
            }
            openApp()
        })
        tagService?.getTagList()?.enqueue(result = {
            requestCounter--
            when (it) {
                is Result.Success -> {
                    if (it.response.body() != null) {
                        InMemory.setTagsList(it.response.body())
                    }

                }
            }
            openApp()
        })
    }

    private fun openApp() {
        if (requestCounter == 0) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}