package com.application.material.bookmarkswallet.app.features.bookmarkList.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

/***
 * add inflate function on ViewGroup item
 */
fun View.inflate(
    @LayoutRes layoutRes: Int,
    attachToRoot: Boolean = false
): View {
    return LayoutInflater.from(this.context)
        .inflate(layoutRes, rootView as? ViewGroup, attachToRoot)
}