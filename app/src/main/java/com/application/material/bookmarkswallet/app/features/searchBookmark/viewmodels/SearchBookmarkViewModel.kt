package com.application.material.bookmarkswallet.app.features.searchBookmark.viewmodels

import android.app.Application
import android.graphics.BitmapFactory
import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.application.material.bookmarkswallet.app.GenAIManager
import com.application.material.bookmarkswallet.app.data.BookmarkListDataRepository
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.BookmarkInfo
import com.application.material.bookmarkswallet.app.models.BookmarkSimple
import com.application.material.bookmarkswallet.app.network.models.Response
import com.application.material.bookmarkswallet.app.utils.EMPTY_BOOKMARK_LABEL
import com.google.ai.client.generativeai.type.TextPart
import com.google.ai.client.generativeai.type.content
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import java.io.BufferedInputStream
import java.io.IOException
import java.util.Date
import javax.inject.Inject
import kotlin.collections.joinToString
import kotlin.collections.map

@HiltViewModel
class SearchBookmarkViewModel @Inject constructor(
    val app: Application,
    private val bookmarkListDataRepository: BookmarkListDataRepository,
    private val genAIManager: GenAIManager
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
     *
     */
    fun saveBookmark(title: String, description: String?, iconUrl: String?, url: String) {
        viewModelScope.launch {
            try {
                Bookmark(
                    appId = Bookmark.getId(url),
                    siteName = description,
                    title = title,
                    iconUrl = iconUrl,
                    url = url,
                    timestamp = Date(),//Dates.today,
                    isLike = false
                ).also {
                    Timber.e(it.toString())
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

    fun searchTextByImagerGenAI() {
        val url = "https://lh3.googleusercontent.com/QHoiIO_W_jxLrG_EfMLu_A5mptYkU-IOWwDTTkZXCe3IRx6v6sd-w9ZeubjBE_K2qZ9_L1oxkfLB5N2HAPr7qiCqdCuJWc32v99bcYzgoWyEtOV-g8OkA7X-W4JEgRS8Qw=w740"
        CoroutineScope(Dispatchers.IO)
            .launch {

                val client = OkHttpClient.Builder()
                    .followRedirects(false)
                    .build()
                val request = Request.Builder()
                    .url(url)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: okhttp3.Response) {
                        try {
                            //generate bitmap from URL
                            Timber.e(url)
                            val bitmap = response.body?.byteStream()
                                ?.let {
                                    val temp = BitmapFactory.decodeStream(BufferedInputStream(it))
                                    Timber.e(temp.height.toString())
                                    temp
                                }
                                ?.let { bitmap ->
                                    //generative AI ------>
                                    val prompt = content {
                                        image(bitmap)
                                        text("retrieve text from image to JSON format")
                                    }
                                    CoroutineScope(Dispatchers.IO)
                                        .launch {
                                            val response =
                                                genAIManager.generativeModel
                                                    .generateContent(prompt)
                                            Timber.e(response.text)
                                        }
                                } ?: {
                                Timber.e("EMPTY IMAGE $url")
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })
            }
    }

    fun searchUrlInfoByUrlGenAI(url: String, onCompletion: () -> Unit = {}) {
        CoroutineScope(Dispatchers.IO)
            .launch {
                val response = genAIManager.generativeModel
                    .generateContent("get title, public icon, url, description $url in JSON format")
                Timber.e(response.candidates.map { it.content.parts.map { (it as TextPart).text }.joinToString()}.joinToString())

                try {
                    (response.candidates.first().content.parts.first() as TextPart).text.replace("json", "").replace("```", "")
                        .let {
                            val  jsonAdapter: JsonAdapter<BookmarkSimple> = Moshi.Builder().build().adapter(BookmarkSimple::class.java).lenient()
                            jsonAdapter.fromJson(it)
                        }
                        ?.also { bookmark ->
                            Timber.e(bookmark.toString())
                            saveBookmark(
                                title = bookmark.title ?: EMPTY_BOOKMARK_LABEL,
                                iconUrl = bookmark.icon,
                                description = bookmark.description,
                                url = bookmark.url
                            )
                            //main thread
                            viewModelScope.launch {
                                onCompletion.invoke()
                            }
                        }

                } catch (e: Exception) {
                    e.printStackTrace()
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