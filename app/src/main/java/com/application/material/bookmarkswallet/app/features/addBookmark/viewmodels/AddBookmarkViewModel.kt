package com.application.material.bookmarkswallet.app.features.addBookmark.viewmodels

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.application.material.bookmarkswallet.app.data.BookmarkListDataRepository
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.BookmarkInfo
import com.application.material.bookmarkswallet.app.utils.EMPTY
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date

@Deprecated("not used anymore")
class AddBookmarkViewModel(application: Application) :
    AndroidViewModel(application = application) {
    val bookmarkInfoLiveData: MutableLiveData<BookmarkInfo> = MutableLiveData()
    val bookmarkInfoLiveError: MutableLiveData<String> = MutableLiveData()
    private lateinit var bookmarkListDataRepository: BookmarkListDataRepository
    val saveBookmarkStatus: MutableLiveData<Boolean> = MutableLiveData()
    val bookmarkSearchedUrlLiveData: MutableLiveData<String> = MutableLiveData()
    val loaderLiveStatus: MutableLiveData<Boolean> = MutableLiveData()
    val updateBookmarkStatus: MutableLiveData<Boolean> = MutableLiveData()
    var bookmarkIconUrl: ObservableField<String> = ObservableField<String>()

    /**
     * add bookamrk on db
     *
     */
    fun updateBookmark(title: String, iconUrl: String?, url: String) {
        Observable.just(url)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .map(bookmarkListDataRepository::findBookmarkById)
            .doOnNext { bookmark ->
                bookmark.title = title
                bookmark.iconUrl = iconUrl
            }
            .map(bookmarkListDataRepository::updateBookmark)
            .subscribe(
                {
                    print("INSERT SUCCESS")
                    updateBookmarkStatus.value = true
                },
                { error ->
                    Timber.e(error.message ?: "ERROR")
                    updateBookmarkStatus.value = false
                })
    }

    /**
     * add bookamrk on db
     *
     */
    fun saveBookmark(title: String, iconUrl: String?, url: String) {
        viewModelScope.launch {
            try {
                Bookmark(
                    appId = Bookmark.getId(url),
                    siteName = EMPTY,
                    title = title,
                    iconUrl = iconUrl,
                    url = url,
                    timestamp = Date(),//Dates.today,
                    isLike = false
                ).also {
                    bookmarkListDataRepository.addBookmark(it)
                    saveBookmarkStatus.value = true
                }
            } catch (e: Exception) {
                // handle error
                Timber.e(e.message ?: "ERROR")
                saveBookmarkStatus.value = false
            }
        }
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
//        Observable.just(url)
//            .map { "https://$it" }
//            .filter { Patterns.WEB_URL.matcher(it).matches() }
//            .doOnNext {
//                Timber.tag(javaClass.name).e("----->$it")
//            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(Schedulers.newThread())
//            .filter { tempUrl -> tempUrl.isNotEmpty() }
//            .observeOn(Schedulers.newThread())
//            .flatMap {
//                bookmarkListDataRepository.findBookmarkInfo(url = it) ?: Observable.empty()
//            }
//            .doOnNext { bookmarksInfo ->
//                //print ciao bookmarksinfo
//                Timber.e(bookmarksInfo.toString())
//
//                if (bookmarksInfo.favicon?.contains("https") != true) {
//                    bookmarksInfo.favicon = "https://$url/" + bookmarksInfo.favicon
//                }
//            }
//            .observeOn(AndroidSchedulers.mainThread())
//            .compose(attachLoaderOnView())
//            .subscribe(
//                { data ->
//                    data.apply {
//                        favicon = favicon.replace("//", "/").replace("https:/", "https://")
//                        bookmarkIconUrl.set(favicon)
//                    }
//                    bookmarkInfoLiveData.value = data
//                },
//                { error ->
//                    bookmarkInfoLiveError.value = error.message
//                })
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
}
