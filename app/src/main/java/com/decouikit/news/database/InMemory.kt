package com.decouikit.news.database

import android.content.Context
import android.util.Log
import com.decouikit.news.network.dto.*
import org.jetbrains.anko.collections.forEachReversedByIndex

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

    private var BOOKMARK: MutableList<PostItem> = mutableListOf()
    private var BOOKMARK_MAP = mutableMapOf<Int, PostItem>()

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
        BOOKMARK.clear()
        BOOKMARK_MAP.clear()
    }

    fun setCategoryList(categoryList: List<Category>) {
        val categoryFilter = CategoryFilter()
        categoryFilter.filterCategoryList(categoryList)

        CATEGORY = categoryFilter.category
        CATEGORY_ALL = categoryFilter.categoryAll
        CATEGORY_MAP = categoryFilter.categoryMap
        CATEGORY_MAP_ALL = categoryFilter.categoryMapAll
    }

    fun getCategoryList(context: Context): ArrayList<Category> {
        if (CATEGORY.isEmpty()) {
            loadCategories(context)
        }
        return CATEGORY as ArrayList<Category>
    }

    //USER
    fun loadCategories(context: Context) {
        if (CATEGORY.isEmpty()) {
            CATEGORY.addAll(Preference(context).loadCategories())
            if (CATEGORY.isNotEmpty()) {
                CATEGORY.forEach { CATEGORY_MAP[it.id] = it }
            }
        }
    }

    fun addCategory(context: Context, category: Category) {
        if (!MEDIA_MAP.containsKey(category.id)) {
            CATEGORY_MAP[category.id] = category
            CATEGORY.add(category)
        }
        Preference(context).persisCategories(CATEGORY as ArrayList<Category>)
    }

    fun getCategoryById(categoryId: Int): Category? {
        return CATEGORY_MAP_ALL[categoryId]
    }

    //USER
    fun loadUsers(context: Context) {
        if (USER.isEmpty()) {
            USER.addAll(Preference(context).loadUsers())
            if (USER.isNotEmpty()) {
                USER.forEach { USER_MAP[it.id] = it }
            }
        }
    }

    fun addUser(context: Context, user: User) {
        if (!USER_MAP.containsKey(user.id)) {
            USER_MAP[user.id] = user
            USER.add(user)
        }
        Preference(context).persistUsers(USER as ArrayList<User>)
    }

    fun getUserById(userId: Int): User? {
        return USER_MAP[userId]
    }

    fun getUsers(context: Context): ArrayList<User> {
        if (USER.isEmpty()) {
            loadUsers(context)
        }
        return USER as ArrayList<User>
    }

    fun removeUser(context: Context, userItem: User) {
        if (USER_MAP.containsKey(userItem.id)) {
            val user = USER_MAP[userItem.id]
            USER_MAP.remove(userItem.id)
            USER.remove(user)
        }
        Preference(context).persistUsers(USER as ArrayList<User>)
    }

    //BOOKMARK
    fun getBookmarkById(id: Int): PostItem? {
        return BOOKMARK_MAP[id]
    }

    fun loadBookmark(context: Context) {
        if (BOOKMARK.isEmpty()) {
            BOOKMARK.addAll(Preference(context).loadBookmark())
            if (BOOKMARK.isNotEmpty()) {
                BOOKMARK.forEach { BOOKMARK_MAP[it.id] = it }
            }
        }
    }

    fun addBookmark(context: Context, post: PostItem) {
        if (!BOOKMARK_MAP.containsKey(post.id)) {
            BOOKMARK_MAP[post.id] = post
            BOOKMARK.add(post)
        }
        Preference(context).persistBookmark(BOOKMARK as ArrayList<PostItem>)
    }

    fun getBookmarks(context: Context): ArrayList<PostItem> {
        if (BOOKMARK.isEmpty()) {
            loadBookmark(context)
        }
        return BOOKMARK as ArrayList<PostItem>
    }

    fun removeBookmark(context: Context, postItem: PostItem) {
        if (BOOKMARK_MAP.containsKey(postItem.id)) {
            val post = BOOKMARK_MAP[postItem.id]
            BOOKMARK_MAP.remove(postItem.id)
            BOOKMARK.remove(post)
        }
        Preference(context).persistBookmark(BOOKMARK as ArrayList<PostItem>)
    }

    fun clearAllBookmark(context: Context) {
        BOOKMARK.clear()
        BOOKMARK_MAP.clear()
        Preference(context).persistBookmark(BOOKMARK as ArrayList<PostItem>)
    }

    //NOTIFICATION
    fun getPostNotificationById(id: Int): PostItem? {
        return NOTIFICATION_MAP[id]
    }

    fun loadNotificationPosts(context: Context) {
        if (NOTIFICATION.isEmpty()) {
            NOTIFICATION.addAll(Preference(context).loadNotificationPosts())
            if (NOTIFICATION.isNotEmpty()) {
                NOTIFICATION.forEach { NOTIFICATION_MAP[it.id] = it }
            }
        }
    }

    fun addNotificationPosts(context: Context, post: PostItem) {
        if (NOTIFICATION_MAP.containsKey(post.id)) {
            return
        }

        if (NOTIFICATION.isEmpty()) {
            loadNotificationPosts(context)
        }
        NOTIFICATION_MAP[post.id] = post
        NOTIFICATION.add(post)
        Preference(context).persisNotificationPosts(NOTIFICATION as ArrayList<PostItem>)
    }

    fun getNotificationPosts(context: Context): List<PostItem> {
        if (NOTIFICATION.isEmpty()) {
            loadNotificationPosts(context)
        }
        val result: ArrayList<PostItem> = ArrayList()
        NOTIFICATION.forEachReversedByIndex {
            result.add(it)
        }
        return result
    }

    fun removeNotificationPosts(context: Context, postId: Int) {
        if (NOTIFICATION_MAP.containsKey(postId)) {
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

    fun getTags(context: Context): ArrayList<Tag>? {
        if (TAGS.isEmpty()) {
            loadTag(context)
        }
        return TAGS as ArrayList<Tag>
    }

    fun loadTag(context: Context) {
        if (TAGS.isEmpty()) {
            TAGS.addAll(Preference(context).loadTags())
            if (TAGS.isNotEmpty()) {
                TAGS.forEach { TAGS_MAP[it.id] = it }
            }
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
        if (MEDIA.isEmpty()) {
            MEDIA.addAll(Preference(context).loadMedia())
            if (MEDIA.isNotEmpty()) {
                MEDIA.forEach { MEDIA_MAP[it.id] = it }
            }
        }
    }

    fun addMedia(context: Context, media: MediaItem) {
        if (!MEDIA_MAP.containsKey(media.id)) {
            MEDIA_MAP[media.id] = media
            MEDIA.add(media)
        }
        Preference(context).persistMedia(MEDIA as ArrayList<MediaItem>)
    }

    fun getAllMedia(context: Context): ArrayList<MediaItem> {
        if (MEDIA.isEmpty()) {
            loadMedia(context)
        }
        return MEDIA as ArrayList<MediaItem>
    }
}