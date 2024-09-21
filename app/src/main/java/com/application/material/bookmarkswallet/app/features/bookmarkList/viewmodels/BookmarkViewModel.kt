package com.application.material.bookmarkswallet.app.features.bookmarkList.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.application.material.bookmarkswallet.app.data.BookmarkListDataRepository
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.BookmarkFilter
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortOrderListEnum.IS_ASCENDING
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortOrderListEnum.IS_DESCENDING
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortTypeListEnum.IS_BY_DATE
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortTypeListEnum.IS_BY_TITLE
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_DEFAULT_VIEW
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_STAR_VIEW
import com.application.material.bookmarkswallet.app.network.models.Response
import com.application.material.bookmarkswallet.app.utils.EMPTY
import com.application.material.bookmarkswallet.app.utils.ONE
import com.application.material.bookmarkswallet.app.utils.ZERO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
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
    private val bookmarkDeletionSuccess: MutableLiveData<Boolean> = MutableLiveData()

    //empty view //total and star
    var sizeEmptyDataPair:
            LiveData<Pair<Int, Int>> = bookmarksLiveData
        .map { Pair(ONE, ZERO) }

    //state to handle
    private val bookmarkPreviewModalMutableState: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    var bookmarkPreviewModalState: StateFlow<Boolean> =
        this.bookmarkPreviewModalMutableState.asStateFlow()

    fun setBookmarkPreviewModal(hasToShown: Boolean) {
        bookmarkPreviewModalMutableState.value = hasToShown
    }

    /**
     * retrieve bookmark list
     */
    fun retrieveBookmarkList(bookmarkFilter: BookmarkFilter) {
        bookmarkFilter.isSearchViewType = false
        viewModelScope.launch {
            bookmarkListDataRepository.getBookmarks()
                .catch {
                    //todo handle error
                    Timber.e(it.message)
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

    /**
     * add bookamrk on db
     *
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun deleteBookmark(bookmark: Bookmark) {
        viewModelScope.launch {
            bookmarkListDataRepository.deleteBookmark(bookmark = bookmark)
                .also {
                    bookmarkListSize.value =
                        bookmarkListSize.value?.toInt()?.minus(1).toString()
                }
                .flatMapLatest { bookmarkListDataRepository.getBookmarks() }.collect {
                    //todo handle response
                    bookmarkDeletionSuccess.value = when (it) {
                        is Response.Success -> true
                        is Response.Error -> false
                    }
                }

        }
    }

    /**
     * composable to add Header on bookmark
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun setStarBookmark(bookmark: Bookmark) {
        viewModelScope.launch {
            bookmarkListDataRepository.updateBookmark(bookmark = bookmark)
                .flatMapLatest {
                    bookmarkListDataRepository.getBookmarks()
                }
                .catch { error ->
                    bookmarkDeletionSuccess.value = false;
                }.collect { success ->
                    bookmarkDeletionSuccess.value = true
                }
        }
    }

    /**
     * composable to add Header on bookmark
     */
    @OptIn(FlowPreview::class)
    fun searchBookmarkByTitle(bookmarkFilter: BookmarkFilter, query: String) {
        bookmarkFilter.isSearchViewType = true
        viewModelScope.launch {
            bookmarkListDataRepository.getBookmarks()
                .debounce(500)
                .catch {
                    //todo handle error
                    Timber.e(it.message)
                }
                .collect {
                    when (it) {
                        is Response.Success -> {
                            it.data
                                .filterByStarTypeComposable(starFilterType = bookmarkFilter.starFilterType)
                                .filterSearchByTitleComposable(query)
                                .sortListByTitleOrDateComposable(
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

    /**
     * delete bookmark
     */
    fun deleteBookmarkFromList(position: Int) {
        var labelPos = -1
        bookmarksLiveData.value
            ?.toMutableList()
            ?.let {
                ///TODO PLEASE I DONT KNOW WHAT THIS IS :( :( :( :( :(
                it.removeAt(position)
                if (position > 0 && position < it.size
//                    it[position - 1] is BookmarkHeader &&
//                    it[position] is BookmarkHeader
                ) {
                    it.removeAt(position - 1)
                    labelPos = position - 1
                }
                if (position == it.size
//                    it[position - 1] is BookmarkHeader
                ) {
                    it.removeAt(position - 1)
                    labelPos = position - 1
                }

                //set data
                bookmarksRemovedBookmarkPairData.value =
                    Pair(listOf(position, labelPos), it)
            }
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

/**
 * composable to add Header on bookmark
 */
fun List<Bookmark>.filterSearchByTitleComposable(query: String) =
    this
        .filter { bookmark ->
            (bookmark.title ?: EMPTY).contains(
                query,
                true
            )
        }
        .toList()