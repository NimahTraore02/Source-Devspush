package com.decouikit.news.network.dto

//
// Created by Dragan Koprena on 10/22/19.
//

enum class CategoryType(type: Int) {
    ALL(0), FEATURED(1), RECENT(2);

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