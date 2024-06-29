package com.application.material.bookmarkswallet.app.features.searchBookmark.viewmodels

import android.app.Application
import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.application.material.bookmarkswallet.app.data.BookmarkListDataRepository
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.BookmarkInfo
import com.application.material.bookmarkswallet.app.network.models.Response
import com.application.material.bookmarkswallet.app.utils.EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class SearchBookmarkViewModel @Inject constructor(
    val app: Application,
    private val bookmarkListDataRepository: BookmarkListDataRepository
) : AndroidViewModel(app) {
    val bookmarkInfoLiveData: MutableLiveData<BookmarkInfo> = MutableLiveData()
    val bookmarkInfoLiveError: MutableLiveData<String> = MutableLiveData()
    val saveBookmarkStatus: MutableLiveData<Boolean> = MutableLiveData()
    val bookmarkSearchedUrlLiveData: MutableLiveData<String> = MutableLiveData()
    val updateBookmarkStatus: MutableLiveData<Boolean> = MutableLiveData()

    //first state :)
    private val bookmarkIconUrl: MutableState<String> = mutableStateOf("bla")
    private val searchedBookmarkMutableState: MutableStateFlow<Bookmark?> = MutableStateFlow(null)
    val searchedBookmarkState: StateFlow<Bookmark?> = searchedBookmarkMutableState.asStateFlow()

    // TODO please !!!!!!!!!!!!!!!!!!!!!!
    override fun onCleared() {
        super.onCleared()
    }

    /**
     *
     */
    fun findBookmarkInfoByUrl(url: String) {
        viewModelScope.launch {
            url
                .let { "https://$it" }
                .takeIf { Patterns.WEB_URL.matcher(it).matches() && it.isNotEmpty() }
                ?.also {
                    bookmarkListDataRepository.findBookmarkInfo(url = it)
                        .catch {
                            bookmarkInfoLiveError.value = it.message
                        }
                        .collect { response ->
                            when (response) {
                                is Response.Success -> {
                                    response.data.let { bookmarksInfo ->
                                        //old logic todo please
                                        if (!bookmarksInfo.favicon.contains("https")) {
                                            bookmarksInfo.favicon =
                                                "https://$url/" + bookmarksInfo.favicon
                                        }
                                        bookmarksInfo.apply {
                                            favicon = favicon
                                                .replace("//", "/")
                                                .replace("https:/", "https://")
                                            //set state
                                            bookmarkIconUrl.value = favicon
                                        }
                                        bookmarkInfoLiveData.value = bookmarksInfo
                                    }
                                }

                                else -> {
                                    bookmarkInfoLiveError.value = "Error Generic"
                                }
                            }
                        }
                }
        }
    }

    /**
     * add bookamrk on db
     *
     */
    fun updateBookmark(title: String, iconUrl: String?, url: String) {
        viewModelScope.launch {
            bookmarkListDataRepository.findBookmarkById(id = url)
                .let {
                    it.title = title
                    it.iconUrl = iconUrl
                    it
                }.let {
                    bookmarkListDataRepository.updateBookmark(bookmark = it)
                        .let {
                            updateBookmarkStatus.value = true
                        }
                }
        }
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
    fun updateWebViewByUrl(url: String) {
        bookmarkSearchedUrlLiveData.value = when {
            url.contains("http") -> url
            else -> "https://$url"
        }
    }
}