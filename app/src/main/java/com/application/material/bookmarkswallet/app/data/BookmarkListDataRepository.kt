package com.application.material.bookmarkswallet.app.data

import BookmarkInfoService
import android.content.Context
import com.application.material.bookmarkswallet.app.data.local.BookmarkDataSourceLocal
import com.application.material.bookmarkswallet.app.data.remote.BookmarkDataSourceRemote
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.BookmarkInfo
import io.reactivex.rxjava3.core.Observable

class BookmarkListDataRepository(val context: Context) {
    private val bookmarkDataSourceLocal: BookmarkDataSourceLocal = BookmarkDataSourceLocal(context)
    private val bookmarkDataSourceRemote: BookmarkDataSourceRemote = BookmarkDataSourceRemote(
        context,
        BookmarkInfoService.create()
    )

    fun getBookmarks(): Observable<MutableList<Bookmark>> {
        return Observable.just(bookmarkDataSourceLocal.getBookmarks())
    }

    fun addBookmark(bookmark: Bookmark) {
        bookmarkDataSourceLocal.insertBookmark(bookmark)
    }

    fun findBookmarkInfo(url: String): Observable<BookmarkInfo>? {
        return bookmarkDataSourceRemote.getBookmarkInfo(url)
    }

    fun updateBookmark(bookmark: Bookmark) {
        return bookmarkDataSourceLocal.updateBookmark(bookmark)
    }

    fun findBookmarkById(id: String): Bookmark {
        return bookmarkDataSourceLocal.findBookmarkById(id)
    }

    fun deleteBookmark(bookmark: Bookmark) {
        return bookmarkDataSourceLocal.deleteBookmark(bookmark)
    }
}