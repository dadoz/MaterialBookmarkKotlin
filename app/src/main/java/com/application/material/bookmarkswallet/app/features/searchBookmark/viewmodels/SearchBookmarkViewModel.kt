package com.application.material.bookmarkswallet.app.features.searchBookmark.viewmodels

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.application.material.bookmarkswallet.app.GenAIManager
import com.application.material.bookmarkswallet.app.data.BookmarkListDataRepository
import com.application.material.bookmarkswallet.app.di.models.Response
import com.application.material.bookmarkswallet.app.features.searchBookmark.SearchResultUIState
import com.application.material.bookmarkswallet.app.models.Bookmark
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
    //first state :)
    private val bookmarkIconUrl: MutableState<String> = mutableStateOf("bla")
    private val searchResultMutableState: MutableStateFlow<SearchResultUIState> =
        MutableStateFlow(value = SearchResultUIState())
    val searchResultUIState: StateFlow<SearchResultUIState> =
        searchResultMutableState.asStateFlow()


    // clear state
    fun clearSearchResultUIState() {
        searchResultMutableState.value = SearchResultUIState(
            isLoading = false,
            error = null,
            bookmark = null
        )
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
                                                    bookmark.icon = it.data.first().logoUrl
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
