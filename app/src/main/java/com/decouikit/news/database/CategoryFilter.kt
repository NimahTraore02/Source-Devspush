package com.decouikit.news.database

import com.decouikit.news.network.dto.Category

class CategoryFilter {

    var category: MutableList<Category> = mutableListOf()
    var categoryMap = mutableMapOf<Int, Category>()
    var categoryAll: MutableList<Category> = mutableListOf()
    var categoryMapAll = mutableMapOf<Int, Category>()

    fun filterCategoryList(categoryList: List<Category>) {
        //CHECK INCLUDE CATEGORY
        categoryAll.clear()
        categoryMapAll.clear()
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
        categoryAll.addAll(categoryTemp)
        if (!Config.isExcludeCategoryEnabled()) {
            //sort category
            if (categoryList.isNotEmpty()) {
                val categoryMapTemp = mutableMapOf<String, Category>()
                categoryList.forEach { categoryMapTemp[it.name] = it }
                for (categoryName in Config.getListOfIncludedCategories()) {
                    val categoryItem = categoryMapTemp[categoryName]
                    if (categoryItem != null) {
                        category.add(categoryItem)
                    }
                }
            }
        } else {
            category.addAll(categoryTemp)
        }

        if (category.isNotEmpty()) {
            category.forEach { categoryMap[it.id] = it }
        }
        if (categoryAll.isNotEmpty()) {
            categoryAll.forEach { categoryMapAll[it.id] = it }
        }
    }
}