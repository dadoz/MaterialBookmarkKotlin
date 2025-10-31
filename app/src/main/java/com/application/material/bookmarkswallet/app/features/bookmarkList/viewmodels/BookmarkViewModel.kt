package com.application.material.bookmarkswallet.app.features.bookmarkList.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.application.material.bookmarkswallet.app.data.BookmarkListDataRepository
import com.application.material.bookmarkswallet.app.di.models.Response
import com.application.material.bookmarkswallet.app.features.bookmarkList.state.BookmarkListUIState
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.BookmarkFilter
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortOrderListEnum.IS_ASCENDING
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortOrderListEnum.IS_DESCENDING
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortTypeListEnum.IS_BY_DATE
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortTypeListEnum.IS_BY_TITLE
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_DEFAULT_VIEW
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_STAR_VIEW
import com.application.material.bookmarkswallet.app.utils.EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    application: Application,
    private val bookmarkListDataRepository: BookmarkListDataRepository
) : AndroidViewModel(application = application) {
    //todo handle Mutable only in VM Please :)
    var bookmarksLiveData: MutableLiveData<List<Bookmark>> = MutableLiveData()
    var bookmarksRemovedBookmarkPairData: MutableLiveData<Pair<List<Int>, List<Bookmark>?>> =
        MutableLiveData()
    var bookmarkListSize: MutableLiveData<String> = MutableLiveData()
    val bookmarkIconUrl: MutableLiveData<String> = MutableLiveData()

    //delete status
    private val bookmarkDeletionMutableState: MutableStateFlow<Boolean?> =
        MutableStateFlow(null)
    var bookmarkDeletionState: StateFlow<Boolean?> =
        this.bookmarkDeletionMutableState.asStateFlow()

    //state to handle
    private val bookmarkPreviewModalMutableState: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    var bookmarkPreviewModalState: StateFlow<Boolean> =
        this.bookmarkPreviewModalMutableState.asStateFlow()

    private val bookmarkListMutableState = MutableStateFlow(BookmarkListUIState())
    val bookmarkListUIState = bookmarkListMutableState.asStateFlow()

    /**
     * retrieve bookmark list version new please refer to retrieveBookmarkList
     *
     */
    fun getBookmarkList() {
        Timber.w("[BOOKMARK LIST] - get all bookmark list")
        //loading state
        bookmarkListMutableState.value = BookmarkListUIState(
            itemList = emptyList(),
            isLoading = true
        )

        //this is wrong move on VM TODO in right VM please with a collectAsState
        viewModelScope
            .launch {
                //retrieve items
                bookmarkListDataRepository.getBookmarks()
                    .collect { result ->
                        when (result) {
                            is Response.Success -> {
                                Timber.d("item ${result.data.joinToString()}")
                                //set new state
                                bookmarkListMutableState
                                    .update {
                                        it.copy(
                                            itemList = result.data
                                        )
                                    }
                            }

                            is Response.Error -> {
                                Timber.d("ERROR - retrieve list ")
                                bookmarkListMutableState
                                    .update {
                                        it.copy(
                                            error = result.exception
                                        )
                                    }
                            }
                        }
                    }
            }

    }

    /**
     * retrieve bookmark list
     */
    fun retrieveBookmarkList(bookmarkFilter: BookmarkFilter) {
        bookmarkFilter.isSearchViewType = false
        //retrieve function
        viewModelScope.launch {
            bookmarkListDataRepository.getBookmarks()
                .catch {
                    //todo handle error
                    Timber.e(it)
                }
                .collect {
                    when (it) {
                        is Response.Success -> {
                            it.data
                                .filterByStarTypeComposable(bookmarkFilter.starFilterType)
                                .let {
                                    //WHATTTTTTTTTT THE HELL????
                                    bookmarkListSize.value = it?.size?.toString() ?: EMPTY
                                    it
                                }
                                .sortListByTitleOrDateComposable(
                                    sortOrderList = bookmarkFilter.sortOrderList,
                                    sortTypeList = bookmarkFilter.sortTypeList
                                )
                                .sortListBySortViewTypeComposable(
                                    sortOrderList = bookmarkFilter.sortOrderList,
                                    sortTypeList = bookmarkFilter.sortTypeList
                                )
                                .getBookmarkWithHeadersListComposable(
                                    starFilterType = bookmarkFilter.starFilterType,
                                    sortTypeList = bookmarkFilter.sortTypeList
                                ).let {
                                    bookmarksLiveData.value = it
                                }
                        }

                        is Response.Error -> it.exception
                    }
                }
        }
    }

    ///whatttt donno
    fun setBookmarkPreviewModal(hasToShown: Boolean) {
        bookmarkPreviewModalMutableState.value = hasToShown
    }

    /**
     * add bookamrk on db
     *
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun deleteBookmark(bookmark: Bookmark) {
        viewModelScope.launch {
            bookmarkListDataRepository.deleteBookmark(bookmark = bookmark)
                .collect {
                    bookmarkDeletionMutableState.value = it
                }

        }
    }

    /**
     * sort
     */
    fun sortBookmarkAscending(bookmarkFilters: BookmarkFilter) {
        bookmarksLiveData.value
            ?.sortListBySortViewTypeComposable(
                bookmarkFilters.sortOrderList,
                bookmarkFilters.sortTypeList
            )
            ?.getBookmarkWithHeadersListComposable(
                starFilterType = bookmarkFilters.starFilterType,
                sortTypeList = bookmarkFilters.sortTypeList
            )
    }

    /**
     *
     */
    fun sortBookmarkByTitle(bookmarkFilters: BookmarkFilter) {
        bookmarksLiveData.value
            ?.sortListByTitleGenericComposable(sortOrderList = bookmarkFilters.sortOrderList)
            ?.getBookmarkWithHeadersListComposable(
                starFilterType = bookmarkFilters.starFilterType,
                sortTypeList = bookmarkFilters.sortTypeList
            )
            ?.also {
                bookmarksLiveData.value = it
            }
    }

    /**
     *
     */
    fun sortBookmarkByDate(bookmarkFilters: BookmarkFilter) {
        bookmarksLiveData.value
            ?.sortListByDateGenericComposable(sortOrderList = bookmarkFilters.sortOrderList)
            ?.getBookmarkWithHeadersListComposable(
                starFilterType = bookmarkFilters.starFilterType,
                sortTypeList = bookmarkFilters.sortTypeList
            )
            ?.also {
                bookmarksLiveData.value = it
            }
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun clearDeleteStatus() {
        bookmarkDeletionMutableState.value = null
    }
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
