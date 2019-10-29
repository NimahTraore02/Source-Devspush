package com.decouikit.news.database

import android.content.Context
import com.decouikit.news.network.dto.*

object InMemory {
    private var CATEGORY: MutableList<Category> = mutableListOf()
    private var CATEGORY_MAP = mutableMapOf<Int, Category>()

    private var CATEGORY_ALL: MutableList<Category> = mutableListOf()
    private var CATEGORY_MAP_ALL = mutableMapOf<Int, Category>()

    private var USER: MutableList<User> = mutableListOf()
    private var USER_MAP = mutableMapOf<Int, User>()

    private var TAGS: MutableList<Tag> = mutableListOf()
    private var TAGS_MAP = mutableMapOf<Int, Tag>()

    private var MEDIA: MutableList<MediaItem> = mutableListOf()
    private var MEDIA_MAP = mutableMapOf<Int, MediaItem>()

    private var NOTIFICATION: MutableList<PostItem> = mutableListOf()
    private var NOTIFICATION_MAP = mutableMapOf<Int, PostItem>()

    fun clear() {
        CATEGORY.clear()
        CATEGORY_MAP.clear()
        USER.clear()
        USER_MAP.clear()
        TAGS.clear()
        TAGS_MAP.clear()
        MEDIA.clear()
        MEDIA_MAP.clear()
        NOTIFICATION.clear()
        NOTIFICATION_MAP.clear()
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
        CATEGORY_ALL.addAll(categoryList)
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
        if (CATEGORY_ALL.isNotEmpty()) {
            CATEGORY_ALL.forEach { CATEGORY_MAP_ALL[it.id] = it }
        }
    }

    fun getCategoryList(): List<Category> = CATEGORY

    fun setUserList(userList: List<User>) {
        USER.addAll(userList)
        if (USER.isNotEmpty()) {
            USER.forEach { USER_MAP[it.id] = it }
        }
    }

    fun getCategoryById(categoryId: Int): Category? {
        return CATEGORY_MAP_ALL[categoryId]
    }

    fun getUserById(userId: Int): User? {
        return USER_MAP[userId]
    }

    //NOTIFICATION
    fun getPostNotificationById(id: Int): PostItem? {
        return NOTIFICATION_MAP[id]
    }

    fun loadNotificationPosts(context: Context) {
        NOTIFICATION.addAll(Preference(context).loadNotificationPosts())
        if (NOTIFICATION.isNotEmpty()) {
            NOTIFICATION.forEach { NOTIFICATION_MAP[it.id] = it }
        }
    }

    fun addNotificationPosts(context: Context, post: PostItem) {
        if (!NOTIFICATION_MAP.containsKey(post.id)) {
            NOTIFICATION_MAP[post.id] = post
            NOTIFICATION.add(post)
        }
        Preference(context).persisNotificationPosts(NOTIFICATION as ArrayList<PostItem>)
    }

    fun getNotificationPosts(): ArrayList<PostItem> {
        return NOTIFICATION as ArrayList<PostItem>
    }

    fun removeNotificationPosts(context: Context, postId: Int) {
        if (!NOTIFICATION_MAP.containsKey(postId)) {
            val post = NOTIFICATION_MAP[postId]
            NOTIFICATION_MAP.remove(postId)
            NOTIFICATION.remove(post)
        }
        Preference(context).persisNotificationPosts(NOTIFICATION as ArrayList<PostItem>)
    }

    //TAG
    fun getTagById(tagId: Int): Tag? {
        return TAGS_MAP[tagId]
    }

    fun loadTag(context: Context) {
        TAGS.addAll(Preference(context).loadTags())
        if (TAGS.isNotEmpty()) {
            TAGS.forEach { TAGS_MAP[it.id] = it }
        }
    }

    fun addTag(context: Context, tag: Tag) {
        if (!TAGS_MAP.containsKey(tag.id)) {
            TAGS_MAP[tag.id] = tag
            TAGS.add(tag)
        }
        Preference(context).persistTags(TAGS as ArrayList<Tag>)
    }

    //MEDIA
    fun getMediaById(mediaId: Int): MediaItem? {
        return MEDIA_MAP[mediaId]
    }

    fun loadMedia(context: Context) {
        MEDIA.addAll(Preference(context).loadMedia())
        if (MEDIA.isNotEmpty()) {
            MEDIA.forEach { MEDIA_MAP[it.id] = it }
        }
    }

    fun addMedia(context: Context, media: MediaItem) {
        if (!MEDIA_MAP.containsKey(media.id)) {
            MEDIA_MAP[media.id] = media
            MEDIA.add(media)
        }
        Preference(context).persistMedia(MEDIA as ArrayList<MediaItem>)
    }
}