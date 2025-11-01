package com.application.material.bookmarkswallet.app.features.searchBookmark.model

import androidx.annotation.Keep
import com.application.material.bookmarkswallet.app.models.Bookmark

@Keep
data class SearchResultUIState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val bookmark: Bookmark? = null
)