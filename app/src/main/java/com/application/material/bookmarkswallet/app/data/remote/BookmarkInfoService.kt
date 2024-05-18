package com.application.material.bookmarkswallet.app.data.remote

import com.application.material.bookmarkswallet.app.BuildConfig
import com.application.material.bookmarkswallet.app.models.BookmarkInfo
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface BookmarkInfoService {
    @GET("extract")
    fun retrieveBookmarkInfo(
        @Query("url") url: String,
        @Query("api_key") apiKey: String = BuildConfig.JSONLINK_API_KEY
    ): Observable<BookmarkInfo>
}
