package com.application.material.bookmarkswallet.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.Bookmark

@Dao
interface BookmarkDao {
    @Insert
    fun insertAllBookmarks(bookmark: List<Bookmark>)

    @Insert
    fun insertBookmark(bookmark: Bookmark)

    @Update
    fun updateBookmark(bookmark: Bookmark): Int

    @Delete
    fun deleteBookmark(bookmark: Bookmark): Int

    @Query("SELECT * FROM mb_bookmark")
    fun getBookmarks(): List<Bookmark>

    @Query("SELECT * FROM mb_bookmark WHERE url=:id")
    fun findBookmarkById(id: String): Bookmark
}