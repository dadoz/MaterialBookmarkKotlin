package com.application.material.bookmarkswallet.app.application

import android.app.Application
import com.application.material.bookmarkswallet.app.BuildConfig.DEBUG
import com.application.material.bookmarkswallet.app.models.BookmarkFilter
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.ListViewTypeEnum.IS_GRID
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortOrderListEnum.IS_ASCENDING
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortTypeListEnum.IS_BY_TITLE
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BookmarkApplication : Application() {
    init {
        if (DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    val bookmarkFilters: BookmarkFilter by lazy {
        BookmarkFilter(IS_GRID, IS_ASCENDING, IS_BY_TITLE, this)
    }
}