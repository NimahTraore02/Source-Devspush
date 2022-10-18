package com.decouikit.news.network.sync

import android.content.Context
import com.decouikit.news.database.InMemory
import com.decouikit.news.network.RetrofitClientInstance
import com.decouikit.news.network.UserService
import com.decouikit.news.network.dto.User
import retrofit2.awaitResponse

object SyncUsers {
    suspend fun getUserById(id: Int, context: Context): User? {
        val service = getService(context)
        val user = InMemory.getUserById(id)
        if (user != null) {
            return user
        } else {
            val response = service?.getUserById(id.toString())?.awaitResponse()
            if (response?.isSuccessful == true) {
                try {
                    val userItem = response.body() as User
                    InMemory.addUser(context, userItem)
                    return userItem
                } catch (e: Exception) {
                }
            }
            return null
        }
    }

    private fun getService(context: Context): UserService? {
        return RetrofitClientInstance
            .getRetrofitInstance(context)
            ?.create(UserService::class.java)
    }
}
