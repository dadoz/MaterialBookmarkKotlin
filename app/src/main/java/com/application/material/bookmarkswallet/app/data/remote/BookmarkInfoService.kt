package com.application.material.bookmarkswallet.app.data.remote

import com.application.material.bookmarkswallet.app.BuildConfig
import com.application.material.bookmarkswallet.app.models.BookmarkIconInfo
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface BookmarkInfoService {

    @GET("search")
    suspend fun retrieveBookmarkInfo(
        @Query("q") query: String,
        @Header("Authorization") auth: String = "Bearer + ${BuildConfig.LOGODEV_API_KEY}"
    ): List<BookmarkIconInfo>
}
