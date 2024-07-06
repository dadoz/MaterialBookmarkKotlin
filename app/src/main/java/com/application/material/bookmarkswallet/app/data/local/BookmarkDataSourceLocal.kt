package com.application.material.bookmarkswallet.app.data.local

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.application.material.bookmarkswallet.app.data.local.db.AppDatabase
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.network.models.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookmarkDataSourceLocal @Inject constructor(val application: Application) {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE mb_bookmark ADD COLUMN isStar INTEGER NOT NULL DEFAULT 0")
        }
    }

    private val database: AppDatabase = Room.databaseBuilder(
        application.applicationContext,
        AppDatabase::class.java,
        "db-bookmarks"
    )
        .addMigrations(MIGRATION_1_2)
        .allowMainThreadQueries()   //Allows room to do operation on main thread
        .build()

    /**
     * get bookmarks
     */
    fun getBookmarks(): Flow<Response<List<Bookmark>>> {
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