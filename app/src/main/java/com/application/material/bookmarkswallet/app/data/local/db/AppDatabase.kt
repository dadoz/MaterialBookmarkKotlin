package com.application.material.bookmarkswallet.app.data.local.db

import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.TypeConverters
import com.application.material.bookmarkswallet.app.data.local.BookmarkDao
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.Bookmark

@Database(entities = [Bookmark::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val bookmarkDao: BookmarkDao
}
