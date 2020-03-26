package com.application.dev.david.materialbookmarkkot.viewModels

import android.app.Application
import android.content.Context
import android.nfc.Tag
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.dev.david.materialbookmarkkot.data.BookmarkListDataRepository
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import com.application.dev.david.materialbookmarkkot.models.BookmarkHeader
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import khronos.toDate
import okhttp3.internal.notify

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {
    var bookmarksLiveData: MutableLiveData<MutableList<Any>> = MutableLiveData()
    var bookmarksRemovedBookmarkPairData: MutableLiveData<Pair<Int, MutableList<Any>?>> = MutableLiveData()
    val bookmarkIconUrl: ObservableField<String> = ObservableField()
    var isEmptyDataList: MutableLiveData<Boolean> = MutableLiveData(false)
    private val bookmarkListDataRepository: BookmarkListDataRepository = BookmarkListDataRepository(getApplication())
    private val bookmarkDeletionSuccess: MutableLiveData<Boolean> = MutableLiveData()
    /**
     * retrieve bookamr list
     */
    fun retrieveBookmarkList() {
        val disposable = Observable.just("")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { bookmarkListDataRepository.getBookmarks() }
            .doOnNext { list -> isEmptyDataList.value = list.isEmpty() }
            .compose(sortListByTitleFirstCharComposable())
            .compose(getBookmarkWithHeadersListComposable())
            .subscribe(
                {result -> bookmarksLiveData.value = result },
                {error -> print(error.message)}
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

    /***
     * compsable with filter on first char title
     */
    private fun sortListByTitleFirstCharComposable() = ObservableTransformer<MutableList<Bookmark>, MutableList<Bookmark>> {
        it
            .flatMap { list -> Observable.fromIterable(list) }
            .sorted { bookmark1, bookmark2 ->
                if (bookmark1.title != null && bookmark2.title != null) {
                    bookmark1.title!!.toLowerCase()[0].minus(bookmark2.title!!.toLowerCase()[0])
                } else 0
            }
            .toList()
            .toObservable()
    }

    /**
     * composable to add Header on bookmark
     */
    private fun getBookmarkWithHeadersListComposable() = ObservableTransformer<MutableList<Bookmark>, MutableList<Any>> {
        it.flatMap { bookmarkList -> Observable.just(ArrayList<Any>())
            .flatMap { list -> Observable.fromIterable(bookmarkList)
                .doOnNext{ bookmark -> list.add(bookmark) }
                .map { bookmark -> bookmark.title?.let { it[0].toLowerCase() } }
                .distinct()
                .map { charRes -> charRes.toString().toUpperCase() }
                .map { label -> BookmarkHeader(label) }
                .doOnNext{ bookmarkHeader -> list.add(list.size -1, bookmarkHeader) }
                .toList().toObservable()
                .map{ list }
            }
        }
    }
}