package com.application.dev.david.materialbookmarkkot.data.local

import androidx.room.*
import com.application.dev.david.materialbookmarkkot.models.Bookmark

@Dao
interface BookmarkDao {
    @Insert
    fun insertAll(bookmark: List<Bookmark>)

    @Insert
    fun insert(bookmark: Bookmark)

    @Update
    fun update(bookmark: List<Bookmark>)

    @Delete
    fun delete(bookmark: Bookmark)

    @Query("SELECT * FROM mb_bookmark")
    fun getBookmarks(): List<Bookmark>
}