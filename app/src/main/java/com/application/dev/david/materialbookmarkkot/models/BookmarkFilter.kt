package com.application.dev.david.materialbookmarkkot.models

import android.content.Context
import android.view.View
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.SortOrderListEnum.IS_ASCENDING
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.SortOrderListEnum.IS_DESCENDING
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.SortTypeListEnum.IS_BY_DATE
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.SortTypeListEnum.IS_BY_TITLE
import com.application.dev.david.materialbookmarkkot.preferences.SharedPrefProv
import com.application.dev.david.materialbookmarkkot.preferences.intPreference

class BookmarkFilter(listViewTypeDefVal: ListViewTypeEnum, sortOrderListDefVal: SortOrderListEnum,
    sortTypeListDefVal: SortTypeListEnum, context: Context?) :
    SharedPrefProv(context) {

    var listViewType: Int by intPreference(MB_LIST_VIEW_TYPE_PREF, listViewTypeDefVal.ordinal)
    var sortOrderList: Int by intPreference(MB_SORT_ORDER_LIST_PREF, sortOrderListDefVal.ordinal)
    var sortTypeList: Int by intPreference(MB_SORT_TYPE_LIST_PREF, sortTypeListDefVal.ordinal)
    var starFilterType: StarFilterTypeEnum = StarFilterTypeEnum.IS_DEFAULT_VIEW

    enum class ListViewTypeEnum { IS_GRID, IS_LIST }
    enum class SortOrderListEnum { IS_ASCENDING, IS_DESCENDING }
    enum class SortTypeListEnum { IS_BY_TITLE, IS_BY_DATE }
    enum class StarFilterTypeEnum { IS_STAR_VIEW, IS_DEFAULT_VIEW }


    fun isGridViewType(): Boolean = listViewType == ListViewTypeEnum.IS_GRID.ordinal

    fun setListViewType() {
        listViewType = ListViewTypeEnum.IS_LIST.ordinal
    }

    fun setGridViewType() {
        listViewType = ListViewTypeEnum.IS_GRID.ordinal
    }

    fun toggleSortAscending() {
        sortOrderList = when (sortOrderList) {
            IS_ASCENDING.ordinal -> IS_DESCENDING.ordinal
            IS_DESCENDING.ordinal -> IS_ASCENDING.ordinal
            else -> sortTypeList
        }
    }

    fun setSortByTitle() {
        sortTypeList = IS_BY_TITLE.ordinal
    }
    fun setSortByDate() {
        sortTypeList = IS_BY_DATE.ordinal
    }

    fun isSortAscending(): Boolean =
        sortOrderList == IS_ASCENDING.ordinal

    fun getVisibilityBySortType(viewRequestType: SortTypeListEnum): Int =
        when (viewRequestType.ordinal == sortTypeList) {
            true -> View.GONE
            else -> View.VISIBLE
        }

    fun getVisibilityByViewType(viewType: ListViewTypeEnum): Int =
        when (viewType.ordinal == listViewType) {
            true -> View.GONE
            else -> View.VISIBLE
        }

    companion object {
        const val GRID_SPAN_COUNT: Int = 2
        const val LIST_SPAN_COUNT: Int = 1
        const val MB_LIST_VIEW_TYPE_PREF: String = "MB_LIST_VIEW_TYPE_PREF"
        const val MB_SORT_ORDER_LIST_PREF: String = "MB_SORT_ORDER_LIST_PREF"
        const val MB_SORT_TYPE_LIST_PREF: String = "MB_SORT_TYPE_LIST_PREF"

    }
}