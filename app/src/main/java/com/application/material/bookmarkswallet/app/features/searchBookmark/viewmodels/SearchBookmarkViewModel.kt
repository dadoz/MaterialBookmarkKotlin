package com.application.material.bookmarkswallet.app.features.searchBookmark.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.application.material.bookmarkswallet.app.models.BookmarkInfo

class SearchBookmarkViewModel(application: Application) : AndroidViewModel(application) {
    val bookmarkInfoLiveData : MutableLiveData<BookmarkInfo> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
    }
}