package com.application.material.bookmarkswallet.app.features.bookmarkList.extension

import com.application.material.bookmarkswallet.app.features.bookmarkList.model.Bookmark
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterHp

fun MutableMap<FilterHp, Boolean>.resetMapToDefault() =
    this.onEach {
        this[it.key] = false
    }

fun List<Bookmark>.sortByTimestampDefault(): List<Bookmark> =
    sortedBy { bookmark ->
        bookmark.timestamp
    }
