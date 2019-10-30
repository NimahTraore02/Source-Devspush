package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.interfaces.ResultListener
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.UserService
import com.decouikit.news.network.dto.User
import org.jetbrains.anko.doAsync

object SyncUsers {
    fun getUserById(id: Int, context: Context, listener: ResultListener<User>?) {
        doAsync {
            val service =
                RetrofitClientInstance.getRetrofitInstance(context)?.create(UserService::class.java)
            val user = InMemory.getUserById(id)
            if (user != null) {
                listener?.onResult(user)
            } else {
                service?.getUserById(id.toString())?.enqueue {
                    when (it) {
                        is Result.Success -> {
                            try {
                                val userItem = it.response.body() as User
                                InMemory.addUser(context, userItem)
                                listener?.onResult(userItem)
                            } catch (e: Exception) {
                                listener?.onResult(null)
                            }
                        }
                        is Result.Failure -> {
                            listener?.onResult(null)
                        }
                    }
                }
            }
        }
    }
}
