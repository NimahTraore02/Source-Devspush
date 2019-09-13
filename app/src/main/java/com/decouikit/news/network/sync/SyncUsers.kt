package com.decouikit.news.network.sync

import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.Result
import com.decouikit.news.extensions.enqueue
import com.decouikit.news.interfaces.Sync
import com.decouikit.news.interfaces.SyncListener
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.UserService
import org.jetbrains.anko.doAsync

object SyncUsers : Sync {
    override fun sync(listener: SyncListener?) {
        val userService = RetrofitClientInstance.retrofitInstance?.create(UserService::class.java)
        doAsync {
            userService?.getUserList()?.enqueue(result = { it ->
                when (it) {
                    is Result.Success -> {
                        if (it.response.body() != null) {
                            InMemory.setUserList(it.response.body())
                        }
                        listener?.finish(true)
                    }
                    is Result.Failure -> {
                        listener?.finish(false)
                    }
                }
            })
        }
    }


}
