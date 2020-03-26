package com.application.dev.david.materialbookmarkkot.viewModels

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.databinding.ObservableField
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
    val updateBookmarkStatus:  MutableLiveData<Boolean> = MutableLiveData()
    var bookmarkIconUrl: ObservableField<String> = ObservableField<String>()

    /**
     * add bookamrk on db
     *
     */
    fun updateBookmark(title: String, iconUrl: String, url: String) {
        val disposable = Observable.just(url)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .map(bookmarkListaDataRepository::findBookmarkById)
            .doOnNext{ bookmark ->
                bookmark.title = title
                bookmark.image = iconUrl
            }
            .map(bookmarkListaDataRepository::updateBookmark)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { print("INSERT SUCCESS")
                    updateBookmarkStatus.value = true },
                { error -> Log.e(javaClass.name, error.message)
                    updateBookmarkStatus.value = false })
        compositeDisposable.add(disposable)

    }
    /**
     * add bookamrk on db
     *
     */
    fun saveBookmark(title: String, iconUrl: String, url: String) {
        val disposable = Observable.just("")
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .map {
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
                { print("INSERT SUCCESS")
                    saveBookmarkStatus.value = true },
                { error -> Log.e(javaClass.name, error.message)
                    saveBookmarkStatus.value = false })
        compositeDisposable.add(disposable)
    }

    /**
     *
     */
    fun updateWebviewByUrl(url: String) {
        bookmarkSearchedUrlLiveData.value = when {
            url.contains("http") -> url
            else -> "https://$url"
        }
    }

    /**
     * add bookamrk on db
     *
     */
    fun findBookmarkInfoByUrl(url: String) {
        val disposable = Observable.just(url)
            .map{ url -> "https://$url" }
            .filter{ url -> Patterns.WEB_URL.matcher(url).matches() }
            .doOnNext{ url -> Log.e(javaClass.name, "----->" + url)}
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .filter{ tempUrl -> tempUrl.isNotEmpty() }
            .observeOn(Schedulers.newThread())
            .flatMap(bookmarkListaDataRepository::findBookmarkInfo)
            .doOnNext { bookmarksInfo ->
                if (!bookmarksInfo.meta.image.contains("https")) {
                    bookmarksInfo.meta.image = "https://$url/" + bookmarksInfo.meta.image
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .compose(attachLoaderOnView())
            .subscribe(
                { data ->
                    data.meta.apply {
                        image = image.replace("//", "/").replace("https:/", "https://")
                        bookmarkIconUrl.set(image)
                    }
                    bookmarkInfoLiveData.value = data
                },
                { error ->
                    Log.e(javaClass.name, error.message)
                })
        compositeDisposable.add(disposable)
    }

    /**
     * please move it and generalize it
     */
    private fun <T> attachLoaderOnView():  ObservableTransformer< T, T> {
        return ObservableTransformer {
                observable -> observable
            .doOnSubscribe { loaderLiveStatus.value = true }
            .doOnNext { loaderLiveStatus.value = false}
            .doOnError { loaderLiveStatus.value = false}
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}