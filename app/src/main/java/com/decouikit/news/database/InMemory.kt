package com.decouikit.news.database

import com.decouikit.news.network.dto.Category
import com.decouikit.news.network.dto.MediaItem
import com.decouikit.news.network.dto.Tag
import com.decouikit.news.network.dto.User

object InMemory {
    private var CATEGORY: List<Category> = arrayListOf()
    private var CATEGORY_MAP = mapOf<Int, Category>()

    private var MEDIA: List<MediaItem> = arrayListOf()
    private var MEDIA_MAP = mapOf<Int, MediaItem>()

    private var USER: List<User> = arrayListOf()
    private var USER_MAP = mapOf<Int, User>()

    private var TAGS: List<Tag> = arrayListOf()
    private var TAGS_MAP = mapOf<Int, Tag>()

    fun setCategoryList(categoryList: List<Category>) {
        CATEGORY = categoryList
        if (CATEGORY.isNotEmpty()) {
            CATEGORY.forEach { CATEGORY_MAP.plus((Pair(it.id, it))) }
        }
    }

    fun getCategoryList(): List<Category> = CATEGORY

    fun setTagsList(tagsList: List<Tag>) {
        TAGS = tagsList
        if (TAGS.isNotEmpty()) {
            TAGS.forEach { TAGS_MAP.plus((Pair(it.id, it))) }
        }
    }

    fun getTagsList(): List<Tag> = TAGS

    fun setUserList(userList: List<User>) {
        USER = userList
        if (USER.isNotEmpty()) {
            USER.forEach { USER_MAP.plus((Pair(it.id, it))) }
        }
    }

    fun getUserList(): List<User> = USER

    fun setMediaList(mediaList: List<MediaItem>) {
        MEDIA = mediaList
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