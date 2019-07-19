package com.application.dev.david.materialbookmarkkot.data.local

import android.content.Context
import androidx.room.Room
import com.application.dev.david.materialbookmarkkot.data.local.db.AppDatabase
import com.application.dev.david.materialbookmarkkot.models.Bookmark

class BookmarkDataSourceLocal(var context: Context) {
    private val database : AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "db-bookmarks")
        .allowMainThreadQueries()   //Allows room to do operation on main thread
        .build()

    /**
     * get bookmarks
     */
    fun getBookmarks(): List<Bookmark> {
        return database.contactDAO.getBookmarks()
    }

}