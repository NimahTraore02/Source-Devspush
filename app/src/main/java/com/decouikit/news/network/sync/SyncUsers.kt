package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.interfaces.Sync
import com.decouikit.news.interfaces.SyncListener
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.UserService
import com.decouikit.news.network.dto.User
import org.jetbrains.anko.doAsync

object SyncUsers : Sync {
    private val users = mutableListOf<User>()
    private var pageNumber = 1
    override fun sync(context: Context, listener: SyncListener?) {
        val userService =
            RetrofitClientInstance.getRetrofitInstance(context)?.create(UserService::class.java)
        doAsync {
            userService?.getUserList(page = pageNumber)?.enqueue(result = { it ->
                when (it) {
                    is Result.Success -> {
                        if (!it.response.body().isNullOrEmpty()) {
                            users.addAll(it.response.body() as List<User>)
                            pageNumber++
                            sync(context, listener)
                            return@enqueue
                        }
                        pageNumber = 1
                        InMemory.setUserList(users)
                        listener?.finish(true)
                    }
                    is Result.Failure -> {
                        pageNumber = 1
                        InMemory.setUserList(users)
                        listener?.finish(false)
                    }
                }
            })
        }
    }
}
