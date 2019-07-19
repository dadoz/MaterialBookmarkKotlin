package com.application.dev.david.materialbookmarkkot.data.local.db

import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.TypeConverters
import com.application.dev.david.materialbookmarkkot.data.local.ContactDAO
import com.application.dev.david.materialbookmarkkot.models.Bookmark


@Database(entities = [Bookmark::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val contactDAO: ContactDAO
}
