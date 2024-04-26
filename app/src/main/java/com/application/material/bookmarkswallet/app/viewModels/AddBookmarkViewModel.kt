package com.application.material.bookmarkswallet.app.viewModels

import android.app.Application
import android.util.Patterns
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.application.material.bookmarkswallet.app.data.BookmarkListDataRepository
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.BookmarkInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.Date

class AddBookmarkViewModel(application: Application) : AndroidViewModel(application) {
    val bookmarkInfoLiveData: MutableLiveData<BookmarkInfo> = MutableLiveData()
    val bookmarkInfoLiveError: MutableLiveData<String> = MutableLiveData()
    private val bookmarkListaDataRepository: BookmarkListDataRepository =
        BookmarkListDataRepository(getApplication())
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val saveBookmarkStatus: MutableLiveData<Boolean> = MutableLiveData()
    val bookmarkSearchedUrlLiveData: MutableLiveData<String> = MutableLiveData()
    val loaderLiveStatus: MutableLiveData<Boolean> = MutableLiveData()
    val updateBookmarkStatus: MutableLiveData<Boolean> = MutableLiveData()
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
            .doOnNext { bookmark ->
                bookmark.title = title
                bookmark.image = iconUrl
            }
            .map(bookmarkListaDataRepository::updateBookmark)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    print("INSERT SUCCESS")
                    updateBookmarkStatus.value = true
                },
                { error ->
                    Timber.e(error.message ?: "ERROR")
                    updateBookmarkStatus.value = false
                })

        //add to disposable
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
                    Date(),//Dates.today,
                    false
                )
            }
            .map(bookmarkListaDataRepository::addBookmark)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    print("INSERT SUCCESS")
                    saveBookmarkStatus.value = true
                },
                { error ->
                    Timber.e(error.message ?: "ERROR")
                    saveBookmarkStatus.value = false
                })

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
            .map { "https://$it" }
            .filter { Patterns.WEB_URL.matcher(it).matches() }
            .doOnNext {
                Timber.tag(javaClass.name).e("----->$it")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .filter { tempUrl -> tempUrl.isNotEmpty() }
            .observeOn(Schedulers.newThread())
            .flatMap {
                bookmarkListaDataRepository.findBookmarkInfo(url = it) ?: Observable.empty()
            }
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
                    bookmarkInfoLiveError.value = error.message
                })

        compositeDisposable.add(disposable)
    }

    /**
     * please move it and generalize it
     */
    private fun <T : Any> attachLoaderOnView(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable
                .doOnSubscribe { loaderLiveStatus.value = true }
                .doOnNext { loaderLiveStatus.value = false }
                .doOnError { loaderLiveStatus.value = false }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}