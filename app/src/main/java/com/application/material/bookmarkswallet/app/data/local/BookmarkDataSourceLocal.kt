package com.application.material.bookmarkswallet.app.data.local

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.application.material.bookmarkswallet.app.data.local.db.AppDatabase
import com.application.material.bookmarkswallet.app.di.models.Response
import com.application.material.bookmarkswallet.app.models.Bookmark
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
    fun getBookmarks(): Flow<Response<List<Bookmark>>> = flow {
        database.bookmarkDao.getBookmarks()
            .let {
                Response.Success(it)
            }
            .also {
                emit(it)
            }
    }

    fun insertBookmark(bookmark: Bookmark) {
        database.bookmarkDao.insertBookmark(bookmark)
    }

    fun updateBookmark(bookmark: Bookmark): Flow<Boolean> = flow {
        database.bookmarkDao.updateBookmark(bookmark)
            .also {
                emit(it.toString().toBoolean())
            }
    }

    fun findBookmarkById(id: String): Flow<Bookmark> = flow {
        database.bookmarkDao.findBookmarkById(id)
            .also {
                emit(it)
            }
    }

    fun deleteBookmark(bookmark: Bookmark): Flow<Boolean> = flow {
        database.bookmarkDao.deleteBookmark(bookmark)
            .also {
                emit(value = true)
            }
    }
}