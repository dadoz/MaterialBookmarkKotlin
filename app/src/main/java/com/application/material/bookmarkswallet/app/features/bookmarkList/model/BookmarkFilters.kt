package com.application.material.bookmarkswallet.app.features.bookmarkList.model

import com.application.material.bookmarkswallet.app.features.bookmarkList.model.ListViewTypeEnum.IS_GRID
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.SortOrderListEnum.IS_ASCENDING
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.SortTypeListEnum.IS_BY_TITLE

enum class ListViewTypeEnum { IS_GRID, IS_LIST }
enum class SortOrderListEnum { IS_ASCENDING, IS_DESCENDING }
enum class SortTypeListEnum { IS_BY_TITLE, IS_BY_DATE }
enum class StarFilterTypeEnum { IS_STAR_VIEW, IS_DEFAULT_VIEW }

val bookmarkFilter = BookmarkFilter(
    IS_GRID,
    IS_ASCENDING,
    IS_BY_TITLE
)

data class BookmarkFilter(
    val listViewTypeDefVal: ListViewTypeEnum,
    val sortOrderListDefVal: SortOrderListEnum,
    val sortTypeListDefVal: SortTypeListEnum,
)



