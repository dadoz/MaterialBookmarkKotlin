package com.application.dev.david.materialbookmarkkot.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import com.application.dev.david.materialbookmarkkot.viewModels.BookmarkViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.add_bookmark_preview_view.view.*
import kotlinx.android.synthetic.main.fragment_bookmark_list.*
import kotlinx.android.synthetic.main.fragment_bookmark_list.view.*
import kotlinx.android.synthetic.main.header_bookmark_view.view.*
import kotlinx.android.synthetic.main.preview_bookmark_view.view.*

class MbBookmarkPreviewView : RelativeLayout {
    var bookmark: Bookmark? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflate(context, R.layout.preview_bookmark_view, this)
    }
    private var previewBehaviourView: BottomSheetBehavior<View>? = null

    fun initView(previewBehaviourView: BottomSheetBehavior<View>) {
        this.previewBehaviourView = previewBehaviourView
        previewBehaviourView.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> mbBookmarkPreviewCardviewId.setPreviewVisible(true)
                    else -> mbBookmarkPreviewCardviewId.setPreviewVisible(true)
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }


    fun initData(bookmark: Bookmark, bookmarkViewModel: BookmarkViewModel ) {
        this.bookmark = bookmark
        //set title and icon
        mbBookmarkPreviewHeaderViewId.setViewModel(bookmarkViewModel)
        mbBookmarkPreviewHeaderViewId.setTitle(bookmark.title)
        mbBookmarkPreviewHeaderViewId.setDescription(bookmark.timestamp)
        mbBookmarkPreviewHeaderViewId.setIcon(bookmark.image)
        mbBookmarkPreviewUrlTextId.text = "https://${bookmark.url}"
        //after calculation of data - otw cannot retrieve size
        previewBehaviourView?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun actionShareBookmark(callbackAction: (intent: Intent) -> Unit) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, bookmark.toString())
            type = "text/plain"
        }
        mbBookmarkPreviewHeaderCardViewId.setOnClickListener {
            callbackAction.invoke(sendIntent)
        }
        mbShareBookmarkActionId.setOnClickListener {
            callbackAction.invoke(sendIntent)
        }
    }

    fun actionOpenPreviewBookmark(callbackAction: (intent: Intent) -> Unit) {
        mbBookmarkPreviewOpenInBrowserButtonId.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse("https://${bookmark?.url}")
            }
            callbackAction.invoke(sendIntent)
        }
    }

    fun actionEditBookmark(callbackAction: () -> Unit) {
        mbBookmarkPreviewActionLayoutId.setOnClickListener {
            callbackAction.invoke()
        }
    }

    fun setMoreButtonAction(callbackAction: () -> Unit) {
        mbBookmarkPreviewMoreButtonId.setOnClickListener {
            setPreviewVisible(false)
            callbackAction.invoke()
        }
    }

    fun setDeleteButtonAction(callbackAction: () -> Unit) {
        mbBookmarkPreviewDeleteButtonId.setOnClickListener {
            previewBehaviourView?.state = BottomSheetBehavior.STATE_COLLAPSED
            setPreviewVisible(false)
            callbackAction.invoke()
        }
    }

    fun setPreviewVisible(isPreviewVisible: Boolean) {
        when (isPreviewVisible) {
            true -> {
                mbBookmarkPreviewDeleteLayoutId.visibility = View.GONE
                mbBookmarkPreviewHeaderEditUrlLayoutId.visibility = View.VISIBLE
            }
            else -> {
                mbBookmarkPreviewDeleteLayoutId.visibility = View.VISIBLE
                mbBookmarkPreviewHeaderEditUrlLayoutId.visibility = View.GONE
            }

        }
    }

}
