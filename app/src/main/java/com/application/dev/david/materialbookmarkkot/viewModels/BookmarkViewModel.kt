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

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {
    var bookmarksLiveData = MutableLiveData<List<Bookmark>>()
    private val bookmarkListaDataRepository : BookmarkListDataRepository = BookmarkListDataRepository(getApplication())
    val bookmarkIconUrl: ObservableField<String> = ObservableField()

    /**
     * retrieve bookamr list
     */
    fun retrieveBookmarkList() {
        val disposable = Observable.just("")//fromArray(list)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { obs -> bookmarkListaDataRepository.getBookmarks()}
//                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
            .subscribe(
                {result -> bookmarksLiveData.value = result },
                {error -> print(error.message)}
            )
    }

    /**
     * add bookamrk on db
     *
     */
    fun saveBookmark(bookmark : Bookmark) {
        val disposable = Observable.just(bookmark)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .map(bookmarkListaDataRepository::addBookmark)
            .subscribe({success -> print("INSERT SUCCESS")}, {error -> Log.e("bla", error.message)})

    }
}