package com.application.material.bookmarkswallet.app.data

import android.app.Application
import com.application.material.bookmarkswallet.app.application.BookmarkApplication
import com.application.material.bookmarkswallet.app.data.local.BookmarkDataSourceLocal
import com.application.material.bookmarkswallet.app.data.remote.BookmarkDataSourceRemote
import com.application.material.bookmarkswallet.app.data.remote.BookmarkInfoService
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.BookmarkInfo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkListDataRepository @Inject constructor(val application: Application) {
    //todo this shit no please :( but no di in this jurassic world
    private val bookmarkInfoService: BookmarkInfoService =
        (application as BookmarkApplication).retrofitClient.create(
            BookmarkInfoService::class.java
        )

    private val bookmarkDataSourceLocal: BookmarkDataSourceLocal =
        BookmarkDataSourceLocal(context = application)
    private val bookmarkDataSourceRemote: BookmarkDataSourceRemote = BookmarkDataSourceRemote(
        bookmarkInfoService = bookmarkInfoService
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