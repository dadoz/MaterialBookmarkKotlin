package com.application.material.bookmarkswallet.app.features.searchBookmark.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.material.bookmarkswallet.app.models.BookmarkInfo
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchBookmarkViewModel(application: Application) : AndroidViewModel(application) {
    val bookmarkInfoLiveData : MutableLiveData<BookmarkInfo> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
    }

    fun onSearchBookmarkByUrl(url: String) {
        viewModelScope.launch {
            Timber.i("hey bla bla")
        }
    }
}