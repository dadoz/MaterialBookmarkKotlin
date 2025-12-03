package com.application.material.bookmarkswallet.app.features.bookmarkList.viewmodels

import android.app.Application
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.application.material.bookmarkswallet.app.data.BookmarkRepository
import com.application.material.bookmarkswallet.app.di.models.Response
import com.application.material.bookmarkswallet.app.features.bookmarkList.extension.sortByTimestampDefault
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.Bookmark
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkListType
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterHp
import com.application.material.bookmarkswallet.app.features.bookmarkList.state.BookmarkListUIState
import com.application.material.bookmarkswallet.app.storage.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    application: Application,
    private val bookmarkRepository: BookmarkRepository,
    private val dataStoreManager: DataStoreManager
) : AndroidViewModel(application = application) {
    //delete status
    private val bookmarkDeletionMutableState: MutableStateFlow<Boolean?> =
        MutableStateFlow(
            value = null
        )
    var bookmarkDeletionState: StateFlow<Boolean?> =
        this.bookmarkDeletionMutableState.asStateFlow()

    //list mutable state UI
    private val bookmarkListMutableState = MutableStateFlow(
        value = BookmarkListUIState()
    )
    val bookmarkListUIState = bookmarkListMutableState.asStateFlow()

    //filter for grid and list
    val selectedFilterListTypeStored by lazy {
        dataStoreManager.selectedFilterListType
    }

    /**
     * retrieve bookmark list version new please refer to retrieveBookmarkList
     *
     */
    fun getBookmarkList(
        coroutineContext: CoroutineContext = Dispatchers.IO
    ) {
        Timber.w("[BOOKMARK LIST] - get all bookmark list")
        //loading state
        bookmarkListMutableState.value = BookmarkListUIState(
            itemList = emptyList(),
            isLoading = true
        )

        //this is wrong move on VM TODO in right VM please with a collectAsState
        viewModelScope
            .launch(
                context = coroutineContext
            ) {
                //retrieve items
                bookmarkRepository.getBookmarks()
                    .collect { result ->
                        when (result) {
                            is Response.Success -> {
                                Timber.d("item ${result.data.joinToString()}")
                                //set new state
                                bookmarkListMutableState
                                    .update {
                                        it.copy(
                                            itemList = result.data
                                        )
                                    }
                            }

                            is Response.Error -> {
                                Timber.d("ERROR - retrieve list ")
                                bookmarkListMutableState
                                    .update {
                                        it.copy(
                                            error = result.exception
                                        )
                                    }
                            }
                        }
                    }
            }

    }

    /**
     * add bookmark on db
     *
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun deleteBookmark(
        coroutineContext: CoroutineContext = Dispatchers.Main,
        bookmark: Bookmark
    ) {
        viewModelScope.launch(
            context = coroutineContext,
        ) {
            bookmarkRepository.deleteBookmark(bookmark = bookmark)
                .collect {
                    bookmarkDeletionMutableState.value = it
                }

        }
    }

    /**
     * add bookamrk on db
     * handle with state instead of cbs (legacy mode but still like it)
     */
    private fun updateBookmarkByShallowCopy(
        coroutineContext: CoroutineContext = Dispatchers.Main,
        bookmarkShallowCopy: Bookmark
    ) {
        //launch update state
        bookmarkListMutableState.update { state ->
            state.copy(
                isLoading = true,
                itemList = state.itemList
                    .toMutableList()
                    .also {
                        it
                            .replaceAll {
                                when {
                                    it.url == bookmarkShallowCopy.url -> bookmarkShallowCopy

                                    else -> it
                                }
                            }
                    }
            )
        }

        //launch update on state
        viewModelScope.launch(
            context = coroutineContext
        ) {
            bookmarkRepository.updateBookmark(
                bookmark = bookmarkShallowCopy
            )
                .first()
                .also {
                    when {
                        it -> {
                            Timber.e("updateBookmark -> successs")
                        }

                        else -> {
                            Timber.e("updateBookmark -> error")
                        }
                    }
                }
        }
    }

    /**
     * add bookamrk on db
     * handle with state instead of cbs (legacy mode but still like it)
     */
    fun updateBookmark(
        bookmark: Bookmark
    ) {
        //update bookmark
        val bookmarkShallowCopy = bookmark.copy(
            timestamp = Date() //new timestamp in my opinion
        )
        //update by shallow
        updateBookmarkByShallowCopy(
            bookmarkShallowCopy = bookmarkShallowCopy
        )
    }

    /**
     * add bookamrk on db
     * handle with state instead of cbs (legacy mode but still like it)
     */
    fun updateBookmarkByPinning(
        bookmark: Bookmark
    ) {
        //update bookmark -- TODO THIS is not working
        //bookmark.isPinned = bookmark.isPinned.not()
        val bookmarkShallowCopy = bookmark.copy(
            isPinned = bookmark.isPinned.not()
        )

        updateBookmarkByShallowCopy(
            bookmarkShallowCopy = bookmarkShallowCopy
        )
    }

    /**
     * mutual exclusion filter - now we have filter by default and also filter by
     * DATE
     * NAME
     * PINNED
     */
    //composition of all filter
    //sort and filter and update state
    fun updateListByFilter(
        singleFilterHpMap: Map<FilterHp, Boolean>
    ) {
        bookmarkListMutableState
            .update {
                it.copy(
                    itemList = it.itemList
                        .let { list ->
                            //filter sort by DATE
                            when {
                                singleFilterHpMap[FilterHp.SORT_BY_DATE] == true ->
                                    list.sortedByDescending { bookmark ->
                                        bookmark.timestamp
                                    }

                                //filter sort by NAME
                                singleFilterHpMap[FilterHp.SORT_BY_NAME] == true ->
                                    list.sortedBy { bookmark ->
                                        bookmark.title
                                    }

                                //filter sort by PINNED
                                singleFilterHpMap[FilterHp.PINNED] == true ->
                                    list.sortedByDescending { bookmark ->
                                        bookmark.isPinned
                                    }

                                else -> list.sortByTimestampDefault()
                            }
                        }
                )
            }
    }

    /**
     * filter by search query
     */
    fun filterListBySearchQuery(
        query: String
    ) {
        //update state on ui
        bookmarkListMutableState
            .update {
                it.copy(
                    itemList = it.preFilteredList
                        .filter {
                            it.title?.contains(
                                other = query,
                                ignoreCase = true
                            ) == true
                                    || it.url.contains(
                                other = query,
                                ignoreCase = true
                            )
                        }
                )
            }
    }

    /**
     * set filter by search query
     * clear filter by search query
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun setFilterListBySearchState(
        searchBarState: SearchBarValue
    ) {
        bookmarkListMutableState
            .update {
                when (searchBarState) {
                    //store a copy items for the search list
                    SearchBarValue.Expanded ->
                        it.copy(
                            preFilteredList = it.itemList.toMutableList(),
                        )

                    //clear filter with prev list
                    //todo THERES A BUG - filter has not been applicable if reset from filter
                    SearchBarValue.Collapsed ->
                        it.copy(
                            itemList = it.preFilteredList.toMutableList(),
                        )
                }
            }
    }

    fun setSelectedFilterListType(value: BookmarkListType) =
        dataStoreManager.setSelectedFilterListType(
            value = value.name
        )

    override fun onCleared() {
        super.onCleared()
    }

    fun clearDeleteStatus() {
        bookmarkDeletionMutableState.value = null
    }

    //todo why not using anymore
    fun cleaBookmarkListState() {
        bookmarkListMutableState.value = BookmarkListUIState(
            itemList = emptyList(),
            isLoading = false
        )
    }
}
