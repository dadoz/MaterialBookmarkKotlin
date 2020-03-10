package com.application.dev.david.materialbookmarkkot.viewModels

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.application.dev.david.materialbookmarkkot.data.BookmarkListDataRepository
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import com.application.dev.david.materialbookmarkkot.models.BookmarkInfo
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import khronos.Dates

class AddBookmarkViewModel(application: Application) : AndroidViewModel(application) {
    val bookmarkInfoLiveData : MutableLiveData<BookmarkInfo> = MutableLiveData()
    private val bookmarkListaDataRepository : BookmarkListDataRepository = BookmarkListDataRepository(getApplication())
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val saveBookmarkStatus: MutableLiveData<Boolean> = MutableLiveData()
    val bookmarkSearchedUrlLiveData: MutableLiveData<String> = MutableLiveData()
    val loaderLiveStatus:  MutableLiveData<Boolean> = MutableLiveData()

    /**
     * add bookamrk on db
     *
     */
    fun saveBookmark(title: String, iconUrl: String, url: String) {
        val disposable = Observable.just("")
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .map { res ->
                Bookmark(
                    title,
                    title,
                    iconUrl,
                    Bookmark.getId(url),
                    url,
                    Dates.today
            )}
            .map(bookmarkListaDataRepository::addBookmark)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { success -> print("INSERT SUCCESS")
                    saveBookmarkStatus.value = true },
                { error -> Log.e(javaClass.name, error.message)
                    saveBookmarkStatus.value = false })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}