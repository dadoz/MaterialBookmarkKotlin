package com.application.material.bookmarkswallet.app.utils

import android.content.Intent

fun shareContentIntentBuilder(url: String): Intent? {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "")
        putExtra(Intent.EXTRA_TEXT, url)
    }
    return Intent.createChooser(sendIntent, null)

}