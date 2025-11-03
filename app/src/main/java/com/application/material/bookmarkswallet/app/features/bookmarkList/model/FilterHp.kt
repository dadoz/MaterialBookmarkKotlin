package com.application.material.bookmarkswallet.app.features.bookmarkList.model

import androidx.annotation.Keep
import com.application.material.bookmarkswallet.app.R

@Keep
enum class FilterHp(val labelRes: Int) {
//    LIST(labelRes = R.string.filter_hp_list),
    SORT_BY_DATE(labelRes = R.string.filter_hp_sort_by_date),
    PINNED(labelRes = R.string.filter_hp_pinned)
}
