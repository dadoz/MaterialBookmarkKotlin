package com.application.dev.david.materialbookmarkkot.data

import android.content.Context
import com.application.dev.david.materialbookmarkkot.data.local.BookmarkDataSourceLocal
import com.application.dev.david.materialbookmarkkot.data.remote.BookmarkDataSourceRemote
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import com.application.dev.david.materialbookmarkkot.models.BookmarkInfo
import io.reactivex.Observable

class BookmarkListDataRepository(val context: Context) {
    private val bookmarkDataSourceLocal : BookmarkDataSourceLocal = BookmarkDataSourceLocal(context)
    private val bookmarkDataSourceRemote : BookmarkDataSourceRemote = BookmarkDataSourceRemote(context,
        BookmarkInfoService.create())

    fun getBookmarks(): Observable<MutableList<Bookmark>> {
//        if (local != 0) else remote
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