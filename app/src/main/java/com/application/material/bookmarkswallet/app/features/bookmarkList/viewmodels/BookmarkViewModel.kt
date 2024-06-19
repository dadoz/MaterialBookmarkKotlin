package com.application.material.bookmarkswallet.app.features.bookmarkList.viewmodels

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
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
import com.application.material.bookmarkswallet.app.models.BookmarkHeader
import com.application.material.bookmarkswallet.app.utils.EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    application: Application,
    private val bookmarkListDataRepository: BookmarkListDataRepository
) : AndroidViewModel(application = application) {
    var bookmarksLiveData: MutableLiveData<List<Bookmark>> = MutableLiveData()
    var bookmarksRemovedBookmarkPairData: MutableLiveData<Pair<List<Int>, List<Bookmark>?>> =
        MutableLiveData()
    val bookmarkIconUrl: ObservableField<String> = ObservableField()
    var bookmarkListSize: MutableLiveData<String> = MutableLiveData()

    //delete status
    private val bookmarkDeletionSuccess: MutableLiveData<Boolean> = MutableLiveData()

    //empty view
    var sizeEmptyDataPair:
            LiveData<Pair<Int, Int>> = bookmarksLiveData
        .map { Pair(it.size, 0) }//total and star

    //state to handle
    private val bookmarkPreviewModalMutableState: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    var bookmarkPreviewModalState: StateFlow<Boolean> =
        this.bookmarkPreviewModalMutableState.asStateFlow()

    fun setBookmarkPreviewModal(hasToShown: Boolean) {
        bookmarkPreviewModalMutableState.value = hasToShown
    }
    /**
     * retrieve bookamr list
     */
    fun retrieveBookmarkList(bookmarkFilter: BookmarkFilter) {
        Observable.just(EMPTY)
            .doOnNext { _ -> bookmarkFilter.isSearchViewType = false }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { bookmarkListDataRepository.getBookmarks() }
            .compose(filterByStarTypeComposable(bookmarkFilter.starFilterType))
            .compose(updatedBookmarkListSizeComposable())
            .compose(
                sortListByTitleOrDateComposable(
                    sortOrderList = bookmarkFilter.sortOrderList,
                    sortTypeList = bookmarkFilter.sortTypeList
                )
            )
            .compose(
                sortListBySortViewTypeComposable(
                    sortOrderList = bookmarkFilter.sortOrderList,
                    sortTypeList = bookmarkFilter.sortTypeList
                )
            )
            .compose(
                getBookmarkWithHeadersListComposable(
                    starFilterType = bookmarkFilter.starFilterType,
                    sortTypeList = bookmarkFilter.sortTypeList
                )
            )
            .subscribe(
                { result -> bookmarksLiveData.value = result },
                { error ->
                    Timber.e(error)
                })
    }

    /**
     * add bookamrk on db
     *
     */
    fun deleteBookmark(bookmark: Bookmark) {
        Observable.just(bookmark)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(bookmarkListDataRepository::deleteBookmark)
            .doOnNext { _ ->
                bookmarkListSize.value =
                    bookmarkListSize.value?.toInt()?.minus(1).toString()
            }
            .flatMap { bookmarkListDataRepository.getBookmarks() }
            .subscribe({ success -> bookmarkDeletionSuccess.value = true },
                { error -> bookmarkDeletionSuccess.value = false; })
    }

    /**
     * composable to add Header on bookmark
     */
    fun setStarBookmark(bookmark: Bookmark) {
        Observable.just(bookmark)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(bookmarkListDataRepository::updateBookmark)
            .flatMap { bookmarkListDataRepository.getBookmarks() }
            .subscribe({ success -> bookmarkDeletionSuccess.value = true },
                { error -> bookmarkDeletionSuccess.value = false; })
    }

    /**
     * composable to add Header on bookmark
     */
    fun searchBookmarkByTitle(bookmarkFilter: BookmarkFilter, query: String) {
        Observable.just(query)
            .doOnNext { _ -> bookmarkFilter.isSearchViewType = true }
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { bookmarkListDataRepository.getBookmarks() }
            .compose(filterByStarTypeComposable(starFilterType = bookmarkFilter.starFilterType))
            .compose(filterSearchByTitleComposable(query))
            .compose(
                sortListByTitleOrDateComposable(
                    sortOrderList = bookmarkFilter.sortOrderList,
                    sortTypeList = bookmarkFilter.sortTypeList
                )
            )
            .compose(
                getBookmarkWithHeadersListComposable(
                    starFilterType = bookmarkFilter.starFilterType,
                    sortTypeList = bookmarkFilter.sortTypeList
                )
            )
            .subscribe(
                { result -> bookmarksLiveData.value = result },
                { error -> Timber.e(error) }
            )
    }

    /**
     * sort
     */
    fun sortBookmarkAscending(bookmarkFilters: BookmarkFilter) {
        Observable.just(bookmarksLiveData.value ?: listOf())
            .flatMap { list -> Observable.fromIterable(list) }
            .map { item -> item as Bookmark }
            .toList().toObservable()
            .compose(
                sortListBySortViewTypeComposable(
                    bookmarkFilters.sortOrderList,
                    bookmarkFilters.sortTypeList
                )
            )
            .compose(
                getBookmarkWithHeadersListComposable(
                    starFilterType = bookmarkFilters.starFilterType,
                    sortTypeList = bookmarkFilters.sortTypeList
                )
            )
            .subscribe(
                { result -> bookmarksLiveData.value = result },
                { error -> Timber.e(error) }
            )
    }

    /**
     *
     */
    fun sortBookmarkByTitle(bookmarkFilters: BookmarkFilter) {
        Observable.just(bookmarksLiveData.value ?: listOf())
            .flatMap { list -> Observable.fromIterable(list) }
//            .filter { item -> item is Bookmark }
            .map { item -> item as Bookmark }
            .toList().toObservable()
            .compose(sortListByTitleGenericComposable(sortOrderList = bookmarkFilters.sortOrderList))
            .compose(
                getBookmarkWithHeadersListComposable(
                    starFilterType = bookmarkFilters.starFilterType,
                    sortTypeList = bookmarkFilters.sortTypeList
                )
            )
            .subscribe(
                { result -> bookmarksLiveData.value = result },
                { error -> Timber.e(error) }
            )
    }

    /**
     *
     */
    fun sortBookmarkByDate(bookmarkFilters: BookmarkFilter) {
        Observable.just(bookmarksLiveData.value ?: listOf())
            .flatMap { list -> Observable.fromIterable(list) }
            .filter { item -> item is Bookmark }
            .map { item -> item as Bookmark }
            .toList().toObservable()
            .compose(sortListByDateGenericComposable(sortOrderList = bookmarkFilters.sortOrderList))
            .compose(
                getBookmarkWithHeadersListComposable(
                    starFilterType = bookmarkFilters.starFilterType,
                    sortTypeList = bookmarkFilters.sortTypeList
                )
            )
            .subscribe(
                { result -> bookmarksLiveData.value = result },
                { error ->
                    Timber.e(error)
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
    }

    /***
     * compsable with filter on first char title
     */
    private fun sortListBySortViewTypeComposable(sortOrderList: Int, sortTypeList: Int) =
        ObservableTransformer<MutableList<Bookmark>, MutableList<Bookmark>> {
            it
                .flatMap {
                    when (sortTypeList) {
                        IS_BY_TITLE.ordinal -> Observable.just(it)
                            .compose(sortListByTitleGenericComposable(sortOrderList))

                        IS_BY_DATE.ordinal -> Observable.just(it)
                            .compose(sortListByDateGenericComposable(sortOrderList))

                        else -> Observable.just(it)
                    }
                }
        }

    /***
     * compsable with filter on first char title
     */
    private fun sortListByTitleFirstCharComposable(sortOrderList: Int) =
        ObservableTransformer<MutableList<Bookmark>, MutableList<Bookmark>> {
            it
                .flatMap { list -> Observable.fromIterable(list) }
                .sorted { bookmark1, bookmark2 ->
                    when (sortOrderList) {
                        IS_ASCENDING.ordinal -> (bookmark1.title ?: EMPTY).compareTo(
                            bookmark2.title ?: EMPTY
                        )

                        IS_DESCENDING.ordinal -> (bookmark2.title ?: EMPTY).compareTo(
                            bookmark1.title ?: EMPTY
                        )

                        else -> 0
                    }
                }
                .toList()
                .toObservable()
        }

    /***
     * compsable with filter on first char title
     */
    private fun sortListByTitleOrDateComposable(sortOrderList: Int, sortTypeList: Int) =
        ObservableTransformer<MutableList<Bookmark>, MutableList<Bookmark>> {
            it
                .flatMap { list ->
                    when (sortTypeList) {
                        IS_BY_TITLE.ordinal -> Observable.just(list)
                            .compose(sortListByTitleFirstCharComposable(sortOrderList = sortOrderList))

                        IS_BY_DATE.ordinal -> Observable.just(list)
                            .compose(sortListByDateGenericComposable(sortOrderList = sortOrderList))

                        else -> Observable.just(list)
                            .compose(sortListByTitleFirstCharComposable(sortOrderList = sortOrderList))
                    }
                }
        }

    /***
     * compsable with filter on first char title
     */
    private fun sortListByTitleGenericComposable(sortOrderList: Int) =
        ObservableTransformer<MutableList<Bookmark>, MutableList<Bookmark>> {
            it
                .flatMap { list -> Observable.fromIterable(list) }
                .sorted { item1, item2 ->
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
                .toList()
                .toObservable()
        }

    /***
     * compsable with filter on first char title
     */
    private fun sortListByDateGenericComposable(sortOrderList: Int) =
        ObservableTransformer<MutableList<Bookmark>, MutableList<Bookmark>> {
            it
                .flatMap { list -> Observable.fromIterable(list) }
                .sorted { item1, item2 ->
                    when (sortOrderList) {
                        IS_ASCENDING.ordinal -> (item1.timestamp?.time
                            ?: 0).compareTo(item2.timestamp?.time ?: 0)

                        IS_DESCENDING.ordinal -> (item2.timestamp?.time
                            ?: 0).compareTo(item1.timestamp?.time ?: 0)

                        else -> 0
                    }
                }
                .toList()
                .toObservable()
        }

    /**
     * composable to add Header on bookmark
     */
    private fun getBookmarkWithHeadersListComposable(
        starFilterType: StarFilterTypeEnum,
        sortTypeList: Int
    ) = ObservableTransformer<MutableList<Bookmark>, List<Bookmark>> {
        it
            .flatMap { bookmarkList ->
                when (starFilterType) {
                    IS_DEFAULT_VIEW -> {
                        Observable.just(ArrayList<Bookmark>())
                            .flatMap { list ->
                                Observable.fromIterable(bookmarkList)
                                    .doOnNext { bookmark -> list.add(bookmark) }
                                    .compose(setHeaderLabelBySortType(sortTypeList))
                                    .map { charRes -> charRes.uppercase(locale = Locale.getDefault()) }
                                    .map { label -> BookmarkHeader(label) }
//                                    .doOnNext { bookmarkHeader ->
//                                        list.add(
//                                            list.size - 1,
//                                            bookmarkHeader
//                                        )
//                                    }
                                    .toList().toObservable()
                                    .map { list }
                            }
                    }

                    IS_STAR_VIEW -> Observable.just(bookmarkList)
                }
            }
    }

    /**
     *
     */
    private fun setHeaderLabelBySortType(sortTypeList: Int) =
        ObservableTransformer<Bookmark, String> {
            it
                .map { bookmark ->
                    when (sortTypeList) {
                        IS_BY_TITLE.ordinal -> {
                            bookmark.title?.let {
                                if (it.isBlank()) "..." else it.lowercase(Locale.getDefault())[0].toString()
                            } ?: "..."
                        }

                        IS_BY_DATE.ordinal -> bookmark.timestamp?.toString() ?: EMPTY
                        else -> EMPTY
                    }
                }
                .distinct()
        }

    /**
     * composable to add Header on bookmark
     */
    private fun filterByStarTypeComposable(starFilterType: StarFilterTypeEnum) =
        ObservableTransformer<MutableList<Bookmark>, MutableList<Bookmark>> {
            it
                .flatMap { bookmarks -> Observable.fromIterable(bookmarks) }
                .filter { bookmark ->
                    when (starFilterType) {
                        IS_STAR_VIEW -> bookmark.isLike
                        else -> true
                    }
                }
                .toList().toObservable()
        }

    /**
     * composable to add Header on bookmark
     */
    private fun updatedBookmarkListSizeComposable() =
        ObservableTransformer<MutableList<Bookmark>, MutableList<Bookmark>> {
            it
                .doOnNext { bookList -> bookmarkListSize.value = bookList.size.toString() }
        }

    /**
     * composable to add Header on bookmark
     */
    private fun filterSearchByTitleComposable(query: String) =
        ObservableTransformer<MutableList<Bookmark>, MutableList<Bookmark>> {
            it
                .flatMap { bookmarks -> Observable.fromIterable(bookmarks) }
                .filter { bookmark -> (bookmark.title ?: EMPTY).contains(query, true) }
                .toList().toObservable()
        }

    /**
     * delete bookmark
     */
    fun deleteBookmarkFromList(position: Int) {
        var labelPos = -1
        bookmarksLiveData.value?.toMutableList()
            ?.let {
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
            }
        bookmarksRemovedBookmarkPairData.value =
            Pair(listOf(position, labelPos), bookmarksLiveData.value)
    }
}
