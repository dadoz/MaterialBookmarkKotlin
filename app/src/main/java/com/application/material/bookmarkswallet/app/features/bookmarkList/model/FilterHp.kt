package com.application.material.bookmarkswallet.app.features.bookmarkList.model

import androidx.annotation.Keep
import com.application.material.bookmarkswallet.app.R

@Keep
enum class FilterHp(override val labelRes: Int, override val iconRes: Int? = null) : FilterType {
    SORT_BY_DATE(labelRes = R.string.filter_hp_sort_by_date),
    SORT_BY_NAME(labelRes = R.string.filter_hp_sort_by_date),
    PINNED(labelRes = R.string.filter_hp_pinned)
}

@Keep
interface FilterType {
    val labelRes: Int
    val iconRes: Int?
}