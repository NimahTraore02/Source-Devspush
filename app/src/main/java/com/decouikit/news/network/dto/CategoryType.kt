package com.decouikit.news.network.dto

enum class CategoryType {

    ALL, FEATURED, RECENT;

    companion object {
        fun getType(type: Int): CategoryType {
            return when (type) {
                0 -> ALL
                1 -> FEATURED
                2 -> RECENT
                else -> ALL
            }
        }
    }
}