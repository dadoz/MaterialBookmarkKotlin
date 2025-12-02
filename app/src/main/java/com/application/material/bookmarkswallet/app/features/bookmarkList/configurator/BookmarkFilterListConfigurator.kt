package com.application.material.bookmarkswallet.app.features.bookmarkList.configurator

import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkListType.GRID
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterHp
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterHp.PINNED
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterHp.SORT_BY_DATE
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterHp.SORT_BY_NAME

internal val filterHpList = listOf(
    SORT_BY_DATE,
    SORT_BY_NAME,
    PINNED
)
internal val filterDefaultListType = listOf(
    GRID
)
internal val filterDefaultHpListType = mapOf(
    FilterHp.PINNED to false,
    FilterHp.SORT_BY_NAME to true,
    FilterHp.SORT_BY_DATE to false
) //default value
