package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkActionTypeEnum.DELETE_ACTION
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkActionTypeEnum.EDIT_ACTION
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkActionTypeEnum.PIN_ACTION
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkActionTypeEnum.SHARE_ACTION

val actionPreviewBookmarkList = listOf(
    Triple(
        first = PIN_ACTION,
        second = R.drawable.ic_pin_new_dark,
        third = R.string.pin_bookmark_label,
    ),
    Triple(
        first = EDIT_ACTION,
        second = R.drawable.ic_edit_dark,
        third = R.string.edit_bookmark_label,

        ),
    Triple(
        first = SHARE_ACTION,
        second = R.drawable.ic_share_dark,
        third = R.string.share_bookmark_label,
    ),
    Triple(
        first = DELETE_ACTION,
        second =
            R.drawable.ic_delete_dark,
        third = R.string.delete_bookmark_label,
    )
)

val actionPinBookmark =
    Pair(
        first = PIN_ACTION,
        second =
            R.drawable.ic_pin_new_dark
    )
