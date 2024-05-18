package com.application.material.bookmarkswallet.app.data.remote

import com.application.material.bookmarkswallet.app.models.BookmarkInfo
import io.reactivex.rxjava3.core.Observable

class BookmarkDataSourceRemote(var bookmarkInfoService: BookmarkInfoService) {
    fun getBookmarkInfo(url: String): Observable<BookmarkInfo> {
        return bookmarkInfoService.retrieveBookmarkInfo(url)
    }
}