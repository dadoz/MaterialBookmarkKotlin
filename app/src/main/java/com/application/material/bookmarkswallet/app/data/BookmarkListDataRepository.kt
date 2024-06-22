package com.application.material.bookmarkswallet.app.data

import com.application.material.bookmarkswallet.app.data.local.BookmarkDataSourceLocal
import com.application.material.bookmarkswallet.app.data.remote.BookmarkDataSourceRemote
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.BookmarkInfo
import com.application.material.bookmarkswallet.app.network.models.Response
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkListDataRepository @Inject constructor(
    private val bookmarkDataSourceRemote: BookmarkDataSourceRemote,
    private val bookmarkDataSourceLocal: BookmarkDataSourceLocal
) {
    fun getBookmarks(): Observable<MutableList<Bookmark>> {
        return Observable.just(bookmarkDataSourceLocal.getBookmarks())
    }

    fun addBookmark(bookmark: Bookmark) {
        bookmarkDataSourceLocal.insertBookmark(bookmark)
    }

    fun findBookmarkInfo(url: String): Flow<Response<BookmarkInfo>> =
        bookmarkDataSourceRemote.getBookmarkInfo(url)

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