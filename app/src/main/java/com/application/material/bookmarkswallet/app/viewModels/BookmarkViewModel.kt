package com.application.material.bookmarkswallet.app.viewModels

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.Locale
import java.util.concurrent.TimeUnit

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {
    var bookmarksLiveData: MutableLiveData<List<Bookmark>> = MutableLiveData()
    var bookmarksRemovedBookmarkPairData: MutableLiveData<Pair<List<Int>, List<Bookmark>?>> =
        MutableLiveData()
    val bookmarkIconUrl: ObservableField<String> = ObservableField()
    var sizeEmptyDataPair: MutableLiveData<Pair<Int, Int>> =
        MutableLiveData(Pair(0, 0)) //total and star
    var bookmarkListSize: MutableLiveData<String> = MutableLiveData()
    private val bookmarkListDataRepository: BookmarkListDataRepository =
        BookmarkListDataRepository(getApplication())
    private val bookmarkDeletionSuccess: MutableLiveData<Boolean> = MutableLiveData()
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    /**
     * retrieve bookamr list
     */
    fun retrieveBookmarkList(bookmarkFilter: BookmarkFilter) {
        val disposable = Observable.just("")
            .doOnNext { _ -> bookmarkFilter.isSearchViewType = false }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { bookmarkListDataRepository.getBookmarks() }
            .compose(setListSizeComposable())
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

        compositeDisposable.add(disposable)
    }

    /**
     * add bookamrk on db
     *
     */
    fun deleteBookmark(bookmark: Bookmark) {
        val disposable = Observable.just(bookmark)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(bookmarkListDataRepository::deleteBookmark)
            .doOnNext { _ ->
                bookmarkListSize.value =
                    bookmarkListSize.value?.toInt()?.minus(1).toString()
            }
            .flatMap { bookmarkListDataRepository.getBookmarks() }
            .compose(setListSizeComposable())
            .subscribe({ success -> bookmarkDeletionSuccess.value = true },
                { error -> bookmarkDeletionSuccess.value = false; })
        compositeDisposable.add(disposable)

    }

    /**
     * composable to add Header on bookmark
     */
    fun setStarBookmark(bookmark: Bookmark) {
        val disposable = Observable.just(bookmark)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(bookmarkListDataRepository::updateBookmark)
            .flatMap { bookmarkListDataRepository.getBookmarks() }
            .compose(setListSizeComposable())
            .subscribe({ success -> bookmarkDeletionSuccess.value = true },
                { error -> bookmarkDeletionSuccess.value = false; })

        compositeDisposable.add(disposable)
    }

    /**
     * composable to add Header on bookmark
     */
    fun searchBookmarkByTitle(bookmarkFilter: BookmarkFilter, query: String) {
        val disposable = Observable.just(query)
            .doOnNext { _ -> bookmarkFilter.isSearchViewType = true }
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { bookmarkListDataRepository.getBookmarks() }
            .compose(filterByStarTypeComposable(starFilterType = bookmarkFilter.starFilterType))
            .compose(filterSearchByTitleComposable(query))
            .compose(setListSizeComposable())
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

        compositeDisposable.add(disposable)
    }

    /**
     * sort
     */
    fun sortBookmarkAscending(bookmarkFilters: BookmarkFilter) {
        val disposable = Observable.just(bookmarksLiveData.value ?: listOf())
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

        compositeDisposable.add(disposable)
    }

    /**
     *
     */
    fun sortBookmarkByTitle(bookmarkFilters: BookmarkFilter) {
        val disposable = Observable.just(bookmarksLiveData.value ?: listOf())
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

        compositeDisposable.add(disposable)
    }

    /**
     *
     */
    fun sortBookmarkByDate(bookmarkFilters: BookmarkFilter) {
        val disposable = Observable.just(bookmarksLiveData.value ?: listOf())
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

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
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
                        IS_ASCENDING.ordinal -> (bookmark1.title ?: "").compareTo(
                            bookmark2.title ?: ""
                        )

                        IS_DESCENDING.ordinal -> (bookmark2.title ?: "").compareTo(
                            bookmark1.title ?: ""
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
                        IS_ASCENDING.ordinal -> (item1.title?.toLowerCase()
                            ?: "").compareTo(item2.title?.toLowerCase() ?: "")

                        IS_DESCENDING.ordinal -> (item2.title?.toLowerCase()
                            ?: "").compareTo(item1.title?.toLowerCase() ?: "")

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
                                    .map { charRes -> charRes.uppercase(Locale.ROOT) }
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
                                if (it.isBlank()) "..." else it.toLowerCase(Locale.ROOT)[0].toString()
                            } ?: "..."
                        }

                        IS_BY_DATE.ordinal -> bookmark.timestamp?.let { it.toString() } ?: ""
                        else -> ""
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
                        IS_STAR_VIEW -> bookmark.isStar
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
                .filter { bookmark -> (bookmark.title ?: "").contains(query, true) }
                .toList().toObservable()
        }

    /**
     * set size of star bookmarks and full items
     *
     */
    private fun setListSizeComposable() =
        ObservableTransformer<MutableList<Bookmark>, MutableList<Bookmark>> {
            var first = 0
            var second = 0
            it
                .doOnNext { list -> first = list.size }
                .flatMap { list -> Observable.fromIterable(list) }
                .filter { item -> item.isStar }
                .toList().toObservable()
                .doOnNext { list -> second = list.size }
                .doOnNext { sizeEmptyDataPair.value = Pair(first, second) }
                .flatMap { _ -> it }
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