package com.application.material.bookmarkswallet.app.features.searchBookmark.viewmodels

import android.app.Application
import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.application.material.bookmarkswallet.app.GenAIManager
import com.application.material.bookmarkswallet.app.data.BookmarkListDataRepository
import com.application.material.bookmarkswallet.app.di.models.Response
import com.application.material.bookmarkswallet.app.features.searchBookmark.SearchResultUIState
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.BookmarkIconInfo
import com.application.material.bookmarkswallet.app.models.BookmarkSimple
import com.application.material.bookmarkswallet.app.models.getBookmarkId
import com.application.material.bookmarkswallet.app.utils.EMPTY_BOOKMARK_LABEL
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.TextPart
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class SearchBookmarkViewModel @Inject constructor(
    val app: Application,
    private val bookmarkListDataRepository: BookmarkListDataRepository,
    private val genAIManager: GenAIManager
) : AndroidViewModel(app) {
    //old value live date
    val bookmarkInfoLiveData: MutableLiveData<BookmarkIconInfo> = MutableLiveData()
    val bookmarkInfoLiveError: MutableLiveData<String> = MutableLiveData()
    val bookmarkSearchedUrlLiveData: MutableLiveData<String> = MutableLiveData()
    val updateBookmarkStatus: MutableLiveData<Boolean> = MutableLiveData()

    //first state :)
    private val bookmarkIconUrl: MutableState<String> = mutableStateOf("bla")
    private val searchResultMutableState: MutableStateFlow<SearchResultUIState> =
        MutableStateFlow(value = SearchResultUIState())
    val searchResultUIState: StateFlow<SearchResultUIState> =
        searchResultMutableState.asStateFlow()

    //val bookmarkIconInfo
    val bookmarkIconInfo = mutableStateOf<BookmarkIconInfo?>(
        value = null
    )

    // clear state
    fun clearSearchResultUIState() {
        searchResultMutableState.value = SearchResultUIState(
            isLoading = false,
            error = null,
            bookmark = null
        )
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
                                        if (!bookmarksInfo.first().favicon.contains("https")) {
                                            bookmarksInfo.first().favicon =
                                                "https://$url/" + bookmarksInfo.first().favicon
                                        }
                                        bookmarksInfo.onEach { item ->
                                            item.favicon = item.favicon
                                                .replace("//", "/")
                                                .replace("https:/", "https://")
                                            //set state
                                            bookmarkIconUrl.value = item.favicon
                                        }
                                        bookmarkInfoLiveData.value = bookmarksInfo.first()
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
                .map {
                    it.title = title
                    it.iconUrl = iconUrl
                    it
                }.map {
                    bookmarkListDataRepository.updateBookmark(bookmark = it)
                        .let {
                            updateBookmarkStatus.value = true
                        }
                }
        }
    }


    /**
     * add bookamrk on db
     * handle with state instead of cbs (legacy mode but still like it)
     */
    fun saveBookmark(
        title: String,
        description: String?,
        iconUrl: String?,
        url: String,
        onSuccessCallback: (bookmark: Bookmark) -> Unit = { bookmark -> },
        onErrorCallback: (e: Throwable) -> Unit = { e -> }
    ) {
        viewModelScope.launch {
            try {
                Bookmark(
                    appId = getBookmarkId(url),
                    siteName = description,
                    title = title,
                    iconUrl = iconUrl,
                    url = url,
                    timestamp = Date(),//Dates.today,
                    isLike = false
                )
                    .also {
                        Timber.e(it.toString())
                        bookmarkListDataRepository.addBookmark(it)
                        onSuccessCallback.invoke(it)
                    }
            } catch (e: Exception) {
                // handle error
                Timber.e(e.message ?: "ERROR")
                onErrorCallback.invoke(e)
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


    /**
     * IMPORTANT -------->> the one used by search
     *
     */
    fun searchUrlInfoByUrlGenAI(
        url: String,
        coroutineContext: CoroutineContext = Dispatchers.IO
    ) {
        //loading state
        searchResultMutableState.update {
            it.copy(
                isLoading = true
            )
        }
//        val prompt0 = "get title, public icon, url, description $url in JSON format"
        val prompts = listOf(
            "generate site_name, title, image, url, description for $url ONLY in JSON format",
        )
            .toContentPrompt()

        CoroutineScope(context = coroutineContext)
            .launch {
                val response = genAIManager.generativeModel
                    .generateContent(
                        prompts
                    )
                Timber.e(response.candidates.joinToString {
                    it.content.parts.joinToString { (it as TextPart).text }
                })

                try {
                    (response.candidates.first().content.parts.first() as TextPart).text.replace(
                        "json",
                        ""
                    )
                        .replace("```", "")
                        .let {
                            val jsonAdapter: JsonAdapter<BookmarkSimple> =
                                Moshi.Builder().build().adapter(BookmarkSimple::class.java)
                                    .lenient()
                            jsonAdapter.fromJson(it)
                        }
                        ?.also { bookmark ->

                            //@TODO temp - test to handle temporary pic
                            bookmark.siteName
                                ?.let { siteName ->
                                    bookmarkListDataRepository.findIconInfoByUrl(
                                        url = siteName
                                    )
                                        .first()
                                        .let {
                                            when {
                                                it is Response.Success -> {
                                                    bookmark.icon = it.data.first().favicon
                                                }

                                                else -> {}
                                            }
                                        }
                                }
                            //@TODO

                            bookmark
                        }
                        ?.also { bookmark ->
                            saveBookmark(
                                title = bookmark.title ?: EMPTY_BOOKMARK_LABEL,
                                iconUrl = bookmark.icon,
                                description = bookmark.description,
                                url = bookmark.url,
                                onErrorCallback = { error ->
                                    viewModelScope.launch { //as ui thread
                                        searchResultMutableState.update {
                                            it.copy(
                                                isLoading = false,
                                                error = error
                                            )
                                        }
                                    }
                                },
                                onSuccessCallback = { res ->
                                    viewModelScope.launch { //as ui thread
                                        searchResultMutableState.update {
                                            it.copy(
                                                isLoading = false,
                                                error = null,
                                                bookmark = res
                                            )
                                        }
                                    }
                                }
                            )
                            //main thread
                        }

                } catch (e: Exception) {
                    e.printStackTrace()
                    viewModelScope.launch { //as ui thread
                        searchResultMutableState.update {
                            it.copy(
                                isLoading = false,
                                error = e
                            )
                        }
                    }
                }
            }
    }

    /**
     *
     */
    fun findIconInfoByUrl(url: String) {
        viewModelScope.launch {
            bookmarkListDataRepository.findIconInfoByUrl(
                url = url
            )
                .catch {
                    Timber.e(it)
                }
                .collect {
                    Timber.e(it.toString())
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}

internal fun List<String>.toContentPrompt() = Content.Builder()
    .also { content ->
        this.onEach { item ->
            content.text(text = item)
        }
    }
    .build()
