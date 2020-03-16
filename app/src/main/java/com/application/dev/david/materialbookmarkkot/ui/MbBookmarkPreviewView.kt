package com.application.dev.david.materialbookmarkkot.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import io.reactivex.functions.Action
import kotlinx.android.synthetic.main.action_bookmark_view.view.*
import kotlinx.android.synthetic.main.preview_bookmark_view.view.*

class MbBookmarkPreviewView : RelativeLayout {
    var bookmark: Bookmark? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflate(context, R.layout.preview_bookmark_view, this)
    }

    fun initData(bookmark: Bookmark) {
        this.bookmark = bookmark
        //set title and icon
        mbBookmarkPreviewHeaderViewId.setTitle(bookmark.title)
        mbBookmarkPreviewHeaderViewId.setDescription(bookmark.timestamp)
        mbBookmarkPreviewHeaderViewId.setIcon(bookmark.image)
    }

    fun actionShareBookmark(callbackAction: (intent: Intent) -> Unit) {
        mbShareBookmarkActionId.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, bookmark.toString())
                type = "text/plain"
            }

            callbackAction.invoke(sendIntent)
        }
    }

    fun actionOpenPreviewBookmark(callbackAction: (intent: Intent) -> Unit) {
        mbOpenPreviewBookmarkActionId.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(bookmark?.url)
            }
            callbackAction.invoke(sendIntent)
        }
    }

    fun actionEditBookmark(callbackAction: () -> Unit) {
        mbEditBookmarkActionId.setOnClickListener {
            callbackAction.invoke()
        }
    }
}
