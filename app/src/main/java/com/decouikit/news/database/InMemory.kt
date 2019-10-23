package com.decouikit.news.database

import com.decouikit.news.network.dto.Category
import com.decouikit.news.network.dto.Tag
import com.decouikit.news.network.dto.User

object InMemory {
    private var CATEGORY: MutableList<Category> = mutableListOf()
    private var CATEGORY_MAP = mutableMapOf<Int, Category>()

    private var USER: MutableList<User> = mutableListOf()
    private var USER_MAP = mutableMapOf<Int, User>()

    private var TAGS: MutableList<Tag> = mutableListOf()
    private var TAGS_MAP = mutableMapOf<Int, Tag>()

    fun clear() {
        CATEGORY.clear()
        CATEGORY_MAP.clear()
        USER.clear()
        USER_MAP.clear()
        TAGS.clear()
        TAGS_MAP.clear()
    }

    fun setCategoryList(categoryList: List<Category>) {
        //CHECK INCLUDE CATEGORY
        val categoryTemp = mutableListOf<Category>()
        categoryList.forEach {
            if (Config.isExcludeCategoryEnabled()) {
                if (!Config.isCategoryExcluded(it) && it.count > 0) {
                    categoryTemp.add(it)
                }
            } else {
                if (Config.isCategoryIncluded(it) && it.count > 0) {
                    categoryTemp.add(it)
                }
            }
        }
        if (!Config.isExcludeCategoryEnabled()) {
            //sort category
            if (categoryList.isNotEmpty()) {
                val categoryMapTemp = mutableMapOf<String, Category>()
                categoryList.forEach { categoryMapTemp[it.name] = it }
                for (categoryName in Config.getListOfIncludedCategories()) {
                    val category = categoryMapTemp[categoryName]
                    if (category != null) {
                        CATEGORY.add(category)
                    }
                }
            }
        } else {
            CATEGORY.addAll(categoryList)
        }

        if (CATEGORY.isNotEmpty()) {
            CATEGORY.forEach { CATEGORY_MAP[it.id] = it }
        }
    }


    fun getCategoryList(): List<Category> = CATEGORY

    fun setTagsList(tagsList: List<Tag>) {
        TAGS.addAll(tagsList)
        if (TAGS.isNotEmpty()) {
            TAGS.forEach { TAGS_MAP[it.id] = it }
        }
    }

    fun getTagsList(): List<Tag> = TAGS

    fun setUserList(userList: List<User>) {
        USER.addAll(userList)
        if (USER.isNotEmpty()) {
            USER.forEach { USER_MAP[it.id] = it }
        }
    }

    fun getUserList(): List<User> = USER

    fun getCategoryById(categoryId: Int): Category? {
        return CATEGORY_MAP[categoryId]
    }

    fun getUserById(userId: Int): User? {
        return USER_MAP[userId]
    }

    fun getTagById(tagId: Int): Tag? {
        return TAGS_MAP[tagId]
    }
}