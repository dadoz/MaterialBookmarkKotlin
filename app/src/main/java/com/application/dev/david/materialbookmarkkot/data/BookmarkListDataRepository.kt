package com.application.dev.david.materialbookmarkkot.data

import android.content.Context
import com.application.dev.david.materialbookmarkkot.data.local.BookmarkDataSourceLocal
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import io.reactivex.Observable

class BookmarkListDataRepository(val context: Context) {
    private val bookmarkDataSourceLocal : BookmarkDataSourceLocal = BookmarkDataSourceLocal(context)

    fun getBookmarks(): Observable<List<Bookmark>> {
//        if (local != 0) else remote
        return Observable.just(bookmarkDataSourceLocal.getBookmarks())
    }

    fun addBookmark(bookmark: Bookmark) {
        bookmarkDataSourceLocal.insertBookmark(bookmark)
    }
}