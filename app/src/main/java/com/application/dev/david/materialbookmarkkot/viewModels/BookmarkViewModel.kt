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
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import khronos.toDate
import okhttp3.internal.notify

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {
    var bookmarksLiveData: MutableLiveData<MutableList<Bookmark>> = MutableLiveData()
    private val bookmarkListaDataRepository: BookmarkListDataRepository = BookmarkListDataRepository(getApplication())
    val bookmarkIconUrl: ObservableField<String> = ObservableField()
    val bookmarkDeletionSuccess: MutableLiveData<Boolean> = MutableLiveData()
    var isEmptyDataList: MutableLiveData<Boolean> = MutableLiveData(false)
    /**
     * retrieve bookamr list
     */
    fun retrieveBookmarkList() {
        val disposable = Observable.just("")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { bookmarkListaDataRepository.getBookmarks() }
            .doOnNext { list -> isEmptyDataList.value = list.isEmpty() }
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
            .map(bookmarkListaDataRepository::deleteBookmark)
            .subscribe({ success -> bookmarkDeletionSuccess.value = true },
                { error -> bookmarkDeletionSuccess.value = false; Log.e("bla", error.message) })
    }

    fun removeBookmarkAt(position: Int) {
        val list = bookmarksLiveData.value
        list?.removeAt(position)
        bookmarksLiveData.value = list
    }
}