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
    override fun sync(context: Context, listener: SyncListener?) {
        val userService =
            RetrofitClientInstance.getRetrofitInstance(context)?.create(UserService::class.java)
        doAsync {
            userService?.getUserList()?.enqueue(result = { it ->
                when (it) {
                    is Result.Success -> {
                        if (it.response.body() != null) {
                            InMemory.setUserList(it.response.body() as List<User>)
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
