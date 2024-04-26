package com.application.material.bookmarkswallet.app.data.local

import androidx.room.*
import com.application.material.bookmarkswallet.app.models.Bookmark

@Dao
interface BookmarkDao {
    @Insert
    fun insertAllBookmarks(bookmark: List<Bookmark>)

    @Insert
    fun insertBookmark(bookmark: Bookmark)

    @Update
    fun updateBookmark(bookmark: Bookmark)

    @Delete
    fun deleteBookmark(bookmark: Bookmark)

    @Query("SELECT * FROM mb_bookmark")
    fun getBookmarks(): MutableList<Bookmark>

    @Query("SELECT * FROM mb_bookmark WHERE url=:id")
    fun findBookmarkById(id: String): Bookmark
}