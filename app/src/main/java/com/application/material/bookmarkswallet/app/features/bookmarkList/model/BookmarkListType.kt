package com.application.material.bookmarkswallet.app.features.bookmarkList.model

import androidx.annotation.Keep
import com.application.material.bookmarkswallet.app.R

@Keep
enum class BookmarkListType(override val labelRes: Int, override val iconRes: Int?) : FilterType {
    LIST(
        labelRes = R.string.bookmark_list_type,
        iconRes = R.drawable.ic_list_filter_dark,
    ),
    GRID(
        labelRes = R.string.bookmark_grid_type,
        iconRes = R.drawable.ic_grid_filter_dark,
    ),
//    GROUP(
//        labelRes = R.string.bookmark_group_type,
//        iconRes = R.drawable.ic_group_filter_dark,
//    )
}