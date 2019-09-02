package com.decouikit.news.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.decouikit.news.R
import com.decouikit.news.activities.base.MainActivity
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.network.*
import org.jetbrains.anko.doAsync

class SplashActivity : Activity() {

    private var requestCounter = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val userService = RetrofitClientInstance.retrofitInstance?.create(UserService::class.java)
        val categoryService =
            RetrofitClientInstance.retrofitInstance?.create(CategoryService::class.java)
        val tagService = RetrofitClientInstance.retrofitInstance?.create(TagService::class.java)
        val mediaService = RetrofitClientInstance.retrofitInstance?.create(MediaService::class.java)
        val postsService = RetrofitClientInstance.retrofitInstance?.create(PostsService::class.java)
        val commentsService = RetrofitClientInstance.retrofitInstance?.create(CommentsService::class.java)

        doAsync {
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
        }
        doAsync {
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
        }
        doAsync {
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
        }
        doAsync {
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

        doAsync {
            postsService?.getPostsList(1, 10)?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (it.response.body() != null) {
                            Log.e("TEST", "POSTS:" + it.response.body().size)
                        }
                    }
                }
            })
        }

        doAsync {
            postsService?.getPostsByCategory("4", 1, 10)?.enqueue(result = {
                when (it) {
                    is Result.Success -> {
                        if (it.response.body() != null) {
                            Log.e("TEST", "POSTS:" + it.response.body().size)
                        }
                    }
                }
            })
        }

        with(Handler()) {

            doAsync {
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
            }
            doAsync {
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
            }
            doAsync {
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
            }
            doAsync {
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

            doAsync {
                postsService?.getPostsList(1, 10)?.enqueue(result = {
                    when (it) {
                        is Result.Success -> {
                            if (it.response.body() != null) {
                                Log.e("TEST", "POSTS:" + it.response.body().size)
                            }
                        }
                    }
                })
            }

            doAsync {
                postsService?.getPostsByCategory("4", 1, 10)?.enqueue(result = {
                    when (it) {
                        is Result.Success -> {
                            if (it.response.body() != null) {
                                Log.e("TEST", "POSTS:" + it.response.body().size)
                            }
                        }
                    }
                })
            }

            postDelayed({
                doAsync {
                    commentsService?.getCommentListPostId("2626", 1, 10)?.enqueue(result = {
                        Log.e("TEST", "getCommentListPostId:" + 2626)

                        when (it) {
                            is Result.Success -> {
                                if (it.response.body() != null) {
                                    Log.e("TEST", "getCommentListPostId:" + it.response.body().size)
                                }
                            }
                            is Result.Failure -> {
                                Log.e("TEST", "Failure")
                            }
                        }
                    })
                }
            }, 3000)
        }
    }

    private fun openApp() {
        if (requestCounter == 0) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}

