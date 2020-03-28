package com.application.dev.david.materialbookmarkkot.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.application.dev.david.materialbookmarkkot.data.local.db.AppDatabase
import com.application.dev.david.materialbookmarkkot.models.Bookmark

class BookmarkDataSourceLocal(var context: Context) {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE mb_bookmark ADD COLUMN isStar INTEGER NOT NULL DEFAULT 0")
        }
    }

    private val database : AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "db-bookmarks")
        .addMigrations(MIGRATION_1_2)
        .allowMainThreadQueries()   //Allows room to do operation on main thread
        .build()

    /**
     * get bookmarks
     */
    fun getBookmarks(): MutableList<Bookmark> {
        return database.bookmarkDao.getBookmarks()
    }

    fun insertBookmark(bookmark: Bookmark) {
        database.bookmarkDao.insertBookmark(bookmark)
    }

    fun updateBookmark(bookmark: Bookmark) {
        database.bookmarkDao.updateBookmark(bookmark)
    }

    fun findBookmarkById(id: String): Bookmark = database.bookmarkDao.findBookmarkById(id)

    fun deleteBookmark(bookmark: Bookmark) {
        database.bookmarkDao.deleteBookmark(bookmark)
    }



}