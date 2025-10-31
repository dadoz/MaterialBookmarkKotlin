package com.application.material.bookmarkswallet.app.features.bookmarkList.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortOrderListEnum.IS_ASCENDING
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortOrderListEnum.IS_DESCENDING
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortTypeListEnum.IS_BY_DATE
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortTypeListEnum.IS_BY_TITLE
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_DEFAULT_VIEW
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_STAR_VIEW
import com.application.material.bookmarkswallet.app.utils.EMPTY
import java.util.Locale

/***
 * add inflate function on ViewGroup item
 */
fun View.inflate(
    @LayoutRes layoutRes: Int,
    attachToRoot: Boolean = false
): View {
    return LayoutInflater.from(this.context)
        .inflate(layoutRes, rootView as? ViewGroup, attachToRoot)
}


/***
 * compsable with filter on first char title
 */
fun List<Bookmark>.sortListBySortViewTypeComposable(
    sortOrderList: Int,
    sortTypeList: Int
) =
    when (sortTypeList) {
        IS_BY_TITLE.ordinal -> sortListByTitleGenericComposable(sortOrderList)

        IS_BY_DATE.ordinal -> sortListByDateGenericComposable(sortOrderList)

        else -> this
    }

/***
 * compsable with filter on first char title
 */
fun List<Bookmark>.sortListByTitleFirstCharComposable(sortOrderList: Int) =
    this
        .toMutableList()
        .sortWith { bookmark1, bookmark2 ->
            when (sortOrderList) {
                IS_ASCENDING.ordinal -> (bookmark1.title ?: EMPTY).compareTo(
                    bookmark2.title ?: EMPTY
                )

                IS_DESCENDING.ordinal -> (bookmark2.title ?: EMPTY).compareTo(
                    bookmark1.title ?: EMPTY
                )

                else -> 0
            }
        }.let {
            this
        }

/***
 * compsable with filter on first char title
 */
fun List<Bookmark>.sortListByTitleOrDateComposable(
    sortOrderList: Int,
    sortTypeList: Int
) =
    when (sortTypeList) {
        IS_BY_TITLE.ordinal -> sortListByTitleFirstCharComposable(sortOrderList = sortOrderList)

        IS_BY_DATE.ordinal -> sortListByDateGenericComposable(sortOrderList = sortOrderList)

        else -> sortListByTitleFirstCharComposable(sortOrderList = sortOrderList)
    }

/***
 * compsable with filter on first char title
 */
fun List<Bookmark>.sortListByTitleGenericComposable(sortOrderList: Int) =
    this
        .toMutableList()
        .sortWith { item1, item2 ->
            when (sortOrderList) {
                IS_ASCENDING.ordinal -> (item1.title?.lowercase(Locale.getDefault())
                    ?: EMPTY).compareTo(
                    item2.title?.lowercase(Locale.getDefault()) ?: EMPTY
                )

                IS_DESCENDING.ordinal -> (item2.title?.lowercase(Locale.getDefault())
                    ?: EMPTY).compareTo(
                    item1.title?.lowercase(Locale.getDefault()) ?: EMPTY
                )

                else -> 0
            }
        }
        .let { this }

/***
 * compsable with filter on first char title
 */
fun List<Bookmark>.sortListByDateGenericComposable(sortOrderList: Int) =
    this
        .toMutableList()
        .sortWith { item1, item2 ->
            when (sortOrderList) {
                IS_ASCENDING.ordinal -> (item1.timestamp?.time
                    ?: 0).compareTo(item2.timestamp?.time ?: 0)

                IS_DESCENDING.ordinal -> (item2.timestamp?.time
                    ?: 0).compareTo(item1.timestamp?.time ?: 0)

                else -> 0
            }
        }
        .let { this }

/**
 * composable to add Header on bookmark
 */
fun List<Bookmark>.getBookmarkWithHeadersListComposable(
    starFilterType: StarFilterTypeEnum,
    sortTypeList: Int
): List<Bookmark> =
    when (starFilterType) {
        IS_DEFAULT_VIEW -> {
            this
                .map { setHeaderLabelBySortType(sortTypeList) }
//                                .map { charRes -> charRes.uppercase(locale = Locale.getDefault()) }
//                                .map { label -> BookmarkHeader(label) }
            this
        }

        IS_STAR_VIEW -> this
    }

/**
 *
 */
fun List<Bookmark>.setHeaderLabelBySortType(sortTypeList: Int) =
    this
        .map { bookmark ->
            when (sortTypeList) {
                IS_BY_TITLE.ordinal -> {
                    bookmark.title?.let {
                        if (it.isBlank()) "..." else it.lowercase(Locale.getDefault())[0].toString()
                    } ?: "..."
                }

                IS_BY_DATE.ordinal -> bookmark.timestamp?.toString()
                    ?: EMPTY

                else -> EMPTY
            }
        }
        .distinct()

/**
 * composable to add Header on bookmark
 */
fun List<Bookmark>.filterByStarTypeComposable(
    starFilterType: StarFilterTypeEnum
) =
    this
        .filter { bookmark ->
            when (starFilterType) {
                IS_STAR_VIEW -> bookmark.isLike
                else -> true
            }
        }
        .toList()
