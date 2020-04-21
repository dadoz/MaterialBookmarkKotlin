package com.application.dev.david.materialbookmarkkot.application

import android.app.Application
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.ListViewTypeEnum.IS_GRID
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.SortOrderListEnum.IS_ASCENDING
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.SortTypeListEnum.IS_BY_TITLE

class BookmarkApplication: Application() {
    public val bookmarkFilters: BookmarkFilter by lazy {
        BookmarkFilter(IS_GRID, IS_ASCENDING, IS_BY_TITLE, this)
    }

    override fun onCreate() {
        super.onCreate()
    }


}