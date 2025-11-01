package com.application.material.bookmarkswallet.app.data.remote

import com.application.material.bookmarkswallet.app.di.models.Response
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkIconInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookmarkDataSourceRemote @Inject constructor(var bookmarkInfoService: BookmarkInfoService) {
    fun getBookmarkInfo(url: String): Flow<Response<List<BookmarkIconInfo>>> = flow {
        emit(
            Response.Success(
                bookmarkInfoService.retrieveBookmarkInfo(url)
            ) as Response<List<BookmarkIconInfo>>
        )
    }.catch { e ->
        e.printStackTrace()
        emit(Response.Error(exception = e))
    }

    fun getBookmarkInfoNew(url: String): Flow<Response<List<BookmarkIconInfo>>> = flow {
        emit(
            Response.Success(
                bookmarkInfoService.retrieveBookmarkInfo(
                    query = url
                )
            ) as Response<List<BookmarkIconInfo>>
        )
    }
        .catch { e ->
            e.printStackTrace()
            emit(Response.Error(exception = e))
        }

}