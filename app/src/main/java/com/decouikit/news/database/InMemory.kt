package com.decouikit.news.database

import com.decouikit.news.network.dto.Category
import com.decouikit.news.network.dto.MediaItem
import com.decouikit.news.network.dto.Tag
import com.decouikit.news.network.dto.User

object InMemory {
    private var CATEGORY: MutableList<Category> = mutableListOf()
    private var CATEGORY_MAP = mutableMapOf<Int, Category>()

    private var MEDIA: MutableList<MediaItem> = mutableListOf()
    private var MEDIA_MAP = mutableMapOf<Int, MediaItem>()

    private var USER: MutableList<User> = mutableListOf()
    private var USER_MAP = mutableMapOf<Int, User>()

    private var TAGS: MutableList<Tag> = mutableListOf()
    private var TAGS_MAP = mutableMapOf<Int, Tag>()

    fun clear() {
        CATEGORY.clear()
        MEDIA.clear()
        USER.clear()
        TAGS.clear()
        CATEGORY_MAP.clear()
        MEDIA_MAP.clear()
        USER_MAP.clear()
        TAGS_MAP.clear()
    }

    fun setCategoryList(categoryList: List<Category>) {
        //CHECK INCLUDE CATEGORY
        categoryList.forEach {
            if (Config.isExcludeCategoryEnabled()) {
                if (!Config.isCategoryExcluded(it) && it.count > 0) {
                    CATEGORY.add(it)
                }
            } else {
                if (Config.isCategoryIncluded(it) && it.count > 0) {
                    CATEGORY.add(it)
                }
            }
        }
        if (CATEGORY.isNotEmpty()) {
            CATEGORY.forEach { CATEGORY_MAP.plus((Pair(it.id, it))) }
        }
    }

    fun getCategoryList(): List<Category> = CATEGORY

    fun setTagsList(tagsList: List<Tag>) {
        TAGS.addAll(tagsList)
        if (TAGS.isNotEmpty()) {
            TAGS.forEach { TAGS_MAP.plus((Pair(it.id, it))) }
        }
    }

    fun getTagsList(): List<Tag> = TAGS

    fun setUserList(userList: List<User>) {
        USER.addAll(userList)
        if (USER.isNotEmpty()) {
            USER.forEach { USER_MAP.plus((Pair(it.id, it))) }
        }
    }

    fun getUserList(): List<User> = USER

    fun setMediaList(mediaList: List<MediaItem>) {
        MEDIA.addAll(mediaList)
        if (MEDIA.isNotEmpty()) {
            MEDIA.forEach { MEDIA_MAP.plus((Pair(it.id, it))) }
        }
    }

    fun getMediaList(): List<MediaItem> = MEDIA

    fun getCategoryById(categoryId: Int): Category? {
        return CATEGORY_MAP[categoryId]
    }

    fun getUserById(userId: Int): User? {
        return USER_MAP[userId]
    }

    fun getMediaById(mediaId: Int): MediaItem? {
        return MEDIA_MAP[mediaId]
    }

    fun getTagById(tagId: Int): Tag? {
        return TAGS_MAP[tagId]
    }
}