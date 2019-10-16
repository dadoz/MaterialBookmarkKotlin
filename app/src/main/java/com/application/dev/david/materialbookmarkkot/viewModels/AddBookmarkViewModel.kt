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
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import javax.xml.transform.Transformer
import kotlin.properties.Delegates.observable

class AddBookmarkViewModel(application: Application) : AndroidViewModel(application) {
    val bookmarkInfoLiveData : MutableLiveData<BookmarkInfo> = MutableLiveData()
    private val bookmarkListaDataRepository : BookmarkListDataRepository = BookmarkListDataRepository(getApplication())
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val saveBookmarkStatus: MutableLiveData<Boolean> = MutableLiveData()
    val bookmarkSearchedUrlLiveData: MutableLiveData<String> = MutableLiveData()
    val loaderLiveStatus:  MutableLiveData<Boolean> = MutableLiveData()
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
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnNext{ bookmarkInfo -> bookmarkInfoLiveData.value = bookmarkInfo }
//            .onErrorReturn{ error2 -> BookmarkInfo("", ArrayList(), Og("", "", "", ""), "") }
//            .map{ bookmarkInfo ->
//                var iconUrl : String? = null
//                if (bookmarkInfo.icons.isNotEmpty()) iconUrl = bookmarkInfo.icons[0].href
//                Bookmark(
//                    bookmarkInfo.title,
//                    bookmarkInfo.title,
//                    iconUrl,
//                    Bookmark.getId(url),
//                    url,
//                    Dates.today
//                )}
//            .observeOn(Schedulers.newThread())
//            .map(bookmarkListaDataRepository::addBookmark)
            .doOnNext { bookmarksInfo ->
                if (!bookmarksInfo.meta.image.contains("https")) {
                bookmarksInfo.meta.image = "https://$url/" + bookmarksInfo.meta.image
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .compose(attachLoaderOnView())
            .subscribe(
                { data ->
                    print("INSERT SUCCESS")
                    bookmarkInfoLiveData.value = data
                },
                { error ->
                    Log.e(javaClass.name, error.message)
                })
        compositeDisposable.add(disposable)
    }

    /**
     * please move it
     */
    private fun <T> attachLoaderOnView():  ObservableTransformer< T, T> {
        return ObservableTransformer {
            observable -> observable
            .doOnSubscribe { loaderLiveStatus.value = true }
            .doOnNext { loaderLiveStatus.value = false}
            .doOnError { loaderLiveStatus.value = false}
        }
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