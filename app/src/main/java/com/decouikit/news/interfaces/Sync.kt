package com.decouikit.news.interfaces

import android.content.Context

interface Sync {
    fun sync(context: Context, listener: SyncListener?)
}