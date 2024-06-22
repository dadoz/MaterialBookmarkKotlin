package com.application.material.bookmarkswallet.app.data.remote

import com.application.material.bookmarkswallet.app.models.BookmarkInfo
import com.application.material.bookmarkswallet.app.network.models.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookmarkDataSourceRemote @Inject constructor(var bookmarkInfoService: BookmarkInfoService) {
    fun getBookmarkInfo(url: String): Flow<Response<BookmarkInfo>> = flow {
        emit(
            Response.Success(
                bookmarkInfoService.retrieveBookmarkInfo(url)
            ) as Response<BookmarkInfo>
        )
    }.catch { e ->
        e.printStackTrace()
        emit(Response.Error(exception = e))
    }
}