package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkActionTypeEnum.EDIT_ACTION
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkActionTypeEnum.PIN_ACTION
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkActionTypeEnum.SHARE_ACTION

val actionPreviewBookmarkList = listOf(
    Pair(
        first = EDIT_ACTION,
        second = R.drawable.ic_edit_dark
    ),
    Pair(
        first = PIN_ACTION,
        second =
            R.drawable.ic_pin_new_dark
    ),
    Pair(
        first = SHARE_ACTION,
        second =
            R.drawable.ic_share_dark
    )
)