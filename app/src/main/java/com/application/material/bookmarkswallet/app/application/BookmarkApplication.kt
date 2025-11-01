package com.application.material.bookmarkswallet.app.application

import android.app.Application
import com.application.material.bookmarkswallet.app.BuildConfig.DEBUG
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BookmarkApplication : Application() {
    init {
        if (DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}
