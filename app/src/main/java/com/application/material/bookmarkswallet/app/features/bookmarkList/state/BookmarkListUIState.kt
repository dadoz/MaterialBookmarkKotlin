package com.application.material.bookmarkswallet.app.features.bookmarkList.state

import com.application.material.bookmarkswallet.app.models.Bookmark

data class BookmarkListUIState(
    val itemList: List<Bookmark> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null,
)