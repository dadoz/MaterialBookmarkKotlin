package com.application.dev.david.materialbookmarkkot.data.local.db

import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.TypeConverters
import com.application.dev.david.materialbookmarkkot.data.local.BookmarkDao
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import java.util.*


@Database(entities = [Bookmark::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val bookmarkDao: BookmarkDao
}
