package com.application.material.bookmarkswallet.app.features.bookmarkList.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.application.material.bookmarkswallet.app.data.BookmarkListDataRepository
import com.application.material.bookmarkswallet.app.di.models.Response
import com.application.material.bookmarkswallet.app.features.bookmarkList.state.BookmarkListUIState
import com.application.material.bookmarkswallet.app.models.Bookmark
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    application: Application,
    private val bookmarkListDataRepository: BookmarkListDataRepository
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
                bookmarkListDataRepository.getBookmarks()
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
            bookmarkListDataRepository.deleteBookmark(bookmark = bookmark)
                .collect {
                    bookmarkDeletionMutableState.value = it
                }

        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun clearDeleteStatus() {
        bookmarkDeletionMutableState.value = null
    }
}
