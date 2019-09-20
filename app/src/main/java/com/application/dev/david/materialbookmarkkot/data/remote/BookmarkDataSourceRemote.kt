package com.application.dev.david.materialbookmarkkot.data.remote

import BookmarkInfoService
import android.content.Context
import com.application.dev.david.materialbookmarkkot.models.BookmarkInfo
import io.reactivex.Observable

class BookmarkDataSourceRemote(var context: Context, var bookmarkInfoService: Any?) {
//    /**
//     * get bookmarks
//     */
//    fun getBookmarks(): List<Bookmark> {
//    }
//
//    fun insertBookmark(bookmark: Bookmark) {
//    }
    fun getBookmarkInfo(url: String) : Observable<BookmarkInfo>? {
        return (bookmarkInfoService as BookmarkInfoService).retrieveBookmarkInfo(url)
    }
}