package com.decouikit.news.database

import com.decouikit.news.network.dto.Category
import com.decouikit.news.network.dto.User

object InMemory {
    private var CATEGORY: List<Category> = arrayListOf()
    private var USER: List<User> = arrayListOf()
    private var CATEGORY_MAP = mapOf<Int, Category>()
    private var USER_MAP = mapOf<Int, User>()

    fun setCategoryList(categoryList: List<Category>) {
        CATEGORY = categoryList
        if (CATEGORY.isNotEmpty()) {
            CATEGORY.forEach { CATEGORY_MAP.plus((Pair(it.id, it))) }
        }
    }

    fun getCategoryList(): List<Category> = CATEGORY

    fun setUserList(userList: List<User>) {
        USER = userList
        if (USER.isNotEmpty()) {
            USER.forEach { USER_MAP.plus((Pair(it.id, it))) }
        }
    }

    fun getUserList(): List<User> = USER

    fun getCategoryById(categoryId: Int): Category? {
        return CATEGORY_MAP[categoryId]
    }

    fun getUserById(userId: Int): User? {
        return USER_MAP[userId]
    }
}