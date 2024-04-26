package com.application.material.bookmarkswallet.app.data.remote

import BookmarkInfoService
import android.content.Context
import com.application.material.bookmarkswallet.app.models.BookmarkInfo
import io.reactivex.rxjava3.core.Observable

class BookmarkDataSourceRemote(var context: Context, var bookmarkInfoService: Any?) {
    //    /**
//     * get bookmarks
//     */
//    fun getBookmarks(): List<Bookmark> {
//    }
//
//    fun insertBookmark(bookmark: Bookmark) {
//    }
    fun getBookmarkInfo(url: String): Observable<BookmarkInfo>? {
        return (bookmarkInfoService as BookmarkInfoService).retrieveBookmarkInfo(url)
    }
}