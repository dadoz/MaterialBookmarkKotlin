package com.application.material.bookmarkswallet.app.features.bookmarkList.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.application.material.bookmarkswallet.app.data.BookmarkRepository
import com.application.material.bookmarkswallet.app.di.models.Response
import com.application.material.bookmarkswallet.app.features.bookmarkList.configurator.filterHpList
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.Bookmark
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkListType
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterHp
import com.application.material.bookmarkswallet.app.features.bookmarkList.state.BookmarkListUIState
import com.application.material.bookmarkswallet.app.storage.DataStoreManager
import com.application.material.bookmarkswallet.app.utils.ONE
import com.application.material.bookmarkswallet.app.utils.ZERO
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

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    application: Application,
    private val bookmarkRepository: BookmarkRepository,
    private val dataStoreManager: DataStoreManager
) : AndroidViewModel(application = application) {
    //delete status
    private val bookmarkDeletionMutableState: MutableStateFlow<Boolean?> =
        MutableStateFlow(null)
    var bookmarkDeletionState: StateFlow<Boolean?> =
        this.bookmarkDeletionMutableState.asStateFlow()

    //state to handle
    private val bookmarkPreviewModalMutableState: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    var bookmarkPreviewModalState: StateFlow<Boolean> =
        this.bookmarkPreviewModalMutableState.asStateFlow()

    private val bookmarkListMutableState = MutableStateFlow(BookmarkListUIState())
    val bookmarkListUIState = bookmarkListMutableState.asStateFlow()

    //filter for grid and list
    val selectedFilterListTypeByStorage by lazy {
        dataStoreManager.selectedFilterListType
    }

    //filter for hp with selection with latest or first
    val selectedFilterHpMap by lazy {
        dataStoreManager.selectedFilterHpMap ?: filterHpList
    }

    /**
     * retrieve bookmark list version new please refer to retrieveBookmarkList
     *
     */
    fun getBookmarkList() {
        Timber.w("[BOOKMARK LIST] - get all bookmark list")
        //loading state
        bookmarkListMutableState.value = BookmarkListUIState(
            itemList = emptyList(),
            isLoading = true
        )

        //this is wrong move on VM TODO in right VM please with a collectAsState
        viewModelScope
            .launch {
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
     * add bookamrk on db
     *
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun deleteBookmark(bookmark: Bookmark) {
        viewModelScope.launch {
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
            context = Dispatchers.Main
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

    //sort and filter and update state
    fun updateListByFilter(filterHpMap: Map<FilterHp, Boolean>) {
        bookmarkListMutableState.update {
            it.copy(
                itemList = it.itemList
                    .let { list ->
                        //composition of all filter
                        //filter sort by DATE
                        when {
                            filterHpMap[FilterHp.SORT_BY_DATE] == true ->
                                list.sortedByDescending { bookmark ->
                                    bookmark.timestamp
                                }

                            else ->
                                list.sortedBy { bookmark ->
                                    bookmark.timestamp
                                }
                        }
                    }
                //TODO make it exclusive
//                    .let { list ->
//                        //filter sort by NAME
//                        when {
//                            filterHpMap[FilterHp.SORT_BY_NAME] == true ->
//                                list.sortedByDescending { bookmark ->
//                                    bookmark.title
//                                }
//
//                            else ->
//                                list.sortedBy { bookmark ->
//                                    bookmark.title
//                                }
//                        }
//                    }
//                    .let { list ->
//                        //filter sort by PINNED
//                        when {
//                            filterHpMap[FilterHp.PINNED] == true ->
//                                list.sortedByDescending { bookmark ->
//                                    bookmark.isLike
//                                }
//
//                            else ->
//                                list.sortedBy { bookmark ->
//                                    bookmark.isLike
//                                }
//                        }
//                    }
            )
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

    fun cleaBookmarkListState() {
        bookmarkListMutableState.value = BookmarkListUIState(
            itemList = emptyList(),
            isLoading = false
        )
    }
}

private fun Int.not() = when (this) {
    ZERO -> ONE
    else -> ZERO
}
