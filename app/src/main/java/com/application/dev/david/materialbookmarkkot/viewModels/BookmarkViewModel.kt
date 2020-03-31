package com.application.dev.david.materialbookmarkkot.viewModels

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.application.dev.david.materialbookmarkkot.data.BookmarkListDataRepository
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import com.application.dev.david.materialbookmarkkot.models.BookmarkHeader
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {
    var bookmarksLiveData: MutableLiveData<MutableList<Any>> = MutableLiveData()
    var bookmarksRemovedBookmarkPairData: MutableLiveData<Pair<Int, MutableList<Any>?>> = MutableLiveData()
    val bookmarkIconUrl: ObservableField<String> = ObservableField()
    var isEmptyDataList: MutableLiveData<Boolean> = MutableLiveData(false)
    var bookmarkListSize: MutableLiveData<String> = MutableLiveData()
    private val bookmarkListDataRepository: BookmarkListDataRepository = BookmarkListDataRepository(getApplication())
    private val bookmarkDeletionSuccess: MutableLiveData<Boolean> = MutableLiveData()
    /**
     * retrieve bookamr list
     */
    fun retrieveBookmarkList(isStarFilterView: Boolean = false) {
        val disposable = Observable.just("")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { bookmarkListDataRepository.getBookmarks() }
            .doOnNext { list -> isEmptyDataList.value = list.isEmpty() }
            .compose(filterByStarTypeComposable(isStarFilterView))
            .doOnNext { bookList -> bookmarkListSize.value = bookList.size.toString() }
            .compose(sortListByTitleFirstCharComposable())
            .compose(getBookmarkWithHeadersListComposable(isStarFilterView))
            .subscribe(
                {result -> bookmarksLiveData.value = result },
                {error ->
                    Timber.e(error)
                }
            )
    }



    /**
     * add bookamrk on db
     *
     */
    fun deleteBookmark(bookmark : Bookmark) {
        val disposable = Observable.just(bookmark)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(bookmarkListDataRepository::deleteBookmark)
            .doOnNext { _ -> bookmarkListSize.value =
                bookmarkListSize.value?.toInt()?.minus(1).toString()
            }
            .subscribe({ success -> bookmarkDeletionSuccess.value = true },
                { error -> bookmarkDeletionSuccess.value = false; })
    }

    /**
     * delete bookmark
     */
    fun deleteBookmarkFromList(position: Int) {
        bookmarksLiveData.value?.removeAt(position)
        bookmarksRemovedBookmarkPairData.value = Pair(position, bookmarksLiveData.value)
    }

    /**
     * composable to add Header on bookmark
     */
    fun setStarBookmark(bookmark: Bookmark) {
        val disposable = Observable.just(bookmark)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(bookmarkListDataRepository::updateBookmark)
            .subscribe({ success -> bookmarkDeletionSuccess.value = true },
                { error -> bookmarkDeletionSuccess.value = false; })
    }

    /**
     * composable to add Header on bookmark
     */
    fun searchBookmarkByTitle(query: String) {
        val disposable = Observable.just(query)
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { bookmarkListDataRepository.getBookmarks() }
            .doOnNext { list -> isEmptyDataList.value = list.isEmpty() }
            .compose(filterByTitleComposable(query))
            .compose(sortListByTitleFirstCharComposable())
            .compose(getBookmarkWithHeadersListComposable(false))
            .subscribe(
                {result -> bookmarksLiveData.value = result },
                {error ->
                    Timber.e(error)
                }
            )
    }

    /**
     * composable to add Header on bookmark
     */
    fun sortBookmarkAscending(isSortAscending: Boolean) {
        val disposable = Observable.just(bookmarksLiveData.value)
            .map { res -> res }
            .compose(sortListByTitleGenericComposable(isSortAscending))
            .subscribe(
                {result -> bookmarksLiveData.value = result },
                {error ->
                    Timber.e(error)
                }
            )

    }


    /***
     * compsable with filter on first char title
     */
    private fun sortListByTitleFirstCharComposable() = ObservableTransformer<MutableList<Bookmark>, MutableList<Bookmark>> {
        it
            .flatMap { list -> Observable.fromIterable(list) }
            .sorted { bookmark1, bookmark2 ->
                (bookmark1.title?: "").compareTo(bookmark2.title?: "")
            }
            .toList()
            .toObservable()
    }

    /***
     * compsable with filter on first char title
     */
    private fun sortListByTitleGenericComposable(isAscending: Boolean) = ObservableTransformer<MutableList<Any>, MutableList<Any>> {
        it
            .flatMap { list -> Observable.fromIterable(list) }
            .filter { item -> item is Bookmark }
            .map { item -> item as Bookmark }
            .sorted { item1, item2 ->
                when (isAscending) {
                    true -> (item1.title ?: "").compareTo(item2.title ?: "")
                    false -> (item2.title ?: "").compareTo(item1.title ?: "")
                }
            }
            .toList()
            .toObservable()
            .compose(getBookmarkWithHeadersListComposable(false))
    }

    /**
     * composable to add Header on bookmark
     */
    private fun getBookmarkWithHeadersListComposable(isStarFilterView: Boolean) = ObservableTransformer<MutableList<Bookmark>, MutableList<Any>> {
        it
            .flatMap { bookmarkList ->
                if (!isStarFilterView) {
                    Observable.just(ArrayList<Any>())
                        .flatMap { list ->
                            Observable.fromIterable(bookmarkList)
                                .doOnNext { bookmark -> list.add(bookmark) }
                                .map { bookmark ->
                                    bookmark.title?.let { if (it.isNullOrBlank()) "" else it.toLowerCase()[0] }
                                        ?: ""
                                }
                                .distinct()
                                .map { charRes -> charRes.toString().toUpperCase() }
                                .map { label -> BookmarkHeader(label) }
                                .doOnNext { bookmarkHeader -> list.add(list.size - 1, bookmarkHeader) }
                                .toList().toObservable()
                                .map { list }
                        }
                } else
                    Observable.just(bookmarkList as MutableList<Any>)
            }
    }

    /**
     * composable to add Header on bookmark
     */
    private fun filterByStarTypeComposable(isStarFilterView: Boolean) = ObservableTransformer<MutableList<Bookmark>, MutableList<Bookmark>> {
        it
            .flatMap { bookmarks -> Observable.fromIterable(bookmarks) }
            .filter { bookmark -> if (isStarFilterView) bookmark.isStar else true }
            .toList().toObservable()
    }

    /**
     * composable to add Header on bookmark
     */
    private fun filterByTitleComposable(query: String) = ObservableTransformer<MutableList<Bookmark>, MutableList<Bookmark>> {
        it
            .flatMap { bookmarks -> Observable.fromIterable(bookmarks) }
            .filter { bookmark -> (bookmark.title?: "").contains(query, true) }
            .toList().toObservable()
    }
}