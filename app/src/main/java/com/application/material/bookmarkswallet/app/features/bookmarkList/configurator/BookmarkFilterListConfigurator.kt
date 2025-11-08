package com.application.material.bookmarkswallet.app.features.bookmarkList.configurator

import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkListType.GRID
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterHp.PINNED
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterHp.SORT_BY_DATE

internal val filterHpList = listOf(
    SORT_BY_DATE,
    PINNED
)
internal val filterDefaultListType = listOf(GRID)
