package com.decouikit.news.database

import android.content.Context
import com.decouikit.news.R

class Config {
    companion object {
        fun getAboutList(context: Context?): ArrayList<String> {
            val items = arrayListOf<String>()
            context?.getString(R.string.about_text_2)?.let { items.add(it) }
            context?.getString(R.string.about_text_3)?.let { items.add(it) }
            context?.getString(R.string.about_text_4)?.let { items.add(it) }
            context?.getString(R.string.about_text_5)?.let { items.add(it) }
            context?.getString(R.string.about_text_6)?.let { items.add(it) }
            context?.getString(R.string.about_text_7)?.let { items.add(it) }
            context?.getString(R.string.about_text_8)?.let { items.add(it) }
            context?.getString(R.string.about_text_9)?.let { items.add(it) }
            context?.getString(R.string.about_text_10)?.let { items.add(it) }
            return items
        }
    }
}