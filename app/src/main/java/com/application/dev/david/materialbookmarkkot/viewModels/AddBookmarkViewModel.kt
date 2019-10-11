package com.application.dev.david.materialbookmarkkot.viewModels

import android.app.Application
import android.util.Log
import android.util.Patterns
import android.util.Size
import android.webkit.URLUtil
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.dev.david.materialbookmarkkot.data.BookmarkListDataRepository
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import com.application.dev.david.materialbookmarkkot.models.BookmarkInfo
import com.application.dev.david.materialbookmarkkot.models.Icon
import com.application.dev.david.materialbookmarkkot.models.Og
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import khronos.Dates
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class AddBookmarkViewModel(application: Application) : AndroidViewModel(application) {
    val bookmarkInfoLiveData : MutableLiveData<BookmarkInfo> = MutableLiveData()
    private val bookmarkListaDataRepository : BookmarkListDataRepository = BookmarkListDataRepository(getApplication())
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val saveBookmarkStatus: MutableLiveData<Boolean> = MutableLiveData()
    val bookmarkSearchedUrlLiveData: MutableLiveData<String> = MutableLiveData()
    /**
     *
     */
    fun updateWebviewByUrl(url: String) {
        bookmarkSearchedUrlLiveData.value = "https://$url"
    }

    /**
     * add bookamrk on db
     *
     */
    fun findBookmarkInfoByUrlAndSave(url: String) {
        val disposable = Observable.just(url)
            .map{ url -> "https://$url" }
            .filter{ url -> Patterns.WEB_URL.matcher(url).matches() }
            .doOnNext{ url -> Log.e(javaClass.name, "----->" + url)}
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .filter{ tempUrl -> tempUrl.isNotEmpty() }
            .observeOn(Schedulers.newThread())
            .flatMap(bookmarkListaDataRepository::findBookmarkInfo)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext{ bookmarkInfo -> bookmarkInfoLiveData.value = bookmarkInfo }
            .onErrorReturn{ error2 -> BookmarkInfo("", ArrayList(), Og("", "", "", ""), "") }
            .map{ bookmarkInfo ->
                var iconUrl : String? = null
                if (bookmarkInfo.icons.isNotEmpty()) iconUrl = bookmarkInfo.icons[0].href
                Bookmark(
                    bookmarkInfo.title,
                    bookmarkInfo.title,
                    iconUrl,
                    Bookmark.getId(url),
                    url,
                    Dates.today
                )}
            .observeOn(Schedulers.newThread())
            .map(bookmarkListaDataRepository::addBookmark)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { success -> print("INSERT SUCCESS")
                    saveBookmarkStatus.value = true },
                { error -> Log.e(javaClass.name, error.message)
                    saveBookmarkStatus.value = false })
        compositeDisposable.add(disposable)
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