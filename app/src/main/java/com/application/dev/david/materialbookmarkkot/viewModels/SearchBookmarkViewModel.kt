package com.application.dev.david.materialbookmarkkot.viewModels

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.application.dev.david.materialbookmarkkot.data.BookmarkListDataRepository
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import com.application.dev.david.materialbookmarkkot.models.BookmarkInfo
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import khronos.Dates

class SearchBookmarkViewModel(application: Application) : AndroidViewModel(application) {
    val bookmarkInfoLiveData : MutableLiveData<BookmarkInfo> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
    }
}