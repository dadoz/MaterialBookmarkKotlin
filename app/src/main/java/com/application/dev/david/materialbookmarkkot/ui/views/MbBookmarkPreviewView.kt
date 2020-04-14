package com.application.dev.david.materialbookmarkkot.ui.views

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.StarFilterTypeEnum.IS_STAR_VIEW
import com.application.dev.david.materialbookmarkkot.modules.bookmarkList.BookmarkListAdapter.Companion.EMPTY_BOOKMARK_LABEL
import com.application.dev.david.materialbookmarkkot.ui.setStrokeColorByColorRes
import com.application.dev.david.materialbookmarkkot.ui.toggleIcon
import com.application.dev.david.materialbookmarkkot.ui.views.behaviors.BookmarkAnimator
import com.application.dev.david.materialbookmarkkot.viewModels.BookmarkViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_bookmark_list.view.*
import kotlinx.android.synthetic.main.header_bookmark_view.view.*
import kotlinx.android.synthetic.main.preview_bookmark_view.view.*

class MbBookmarkPreviewView : FrameLayout {
    var bookmark: Bookmark? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflate(context, R.layout.preview_bookmark_view, this)
    }
    private var previewBehaviourView: BottomSheetBehavior<View>? = null

    fun setStarColor(starFilterType: BookmarkFilter.StarFilterTypeEnum) {
        when (starFilterType) {
            IS_STAR_VIEW -> mbBookmarkPreviewHeaderCardViewId.setStrokeColorByColorRes(R.color.colorAccent)
            else -> mbBookmarkPreviewHeaderCardViewId.setStrokeColorByColorRes(R.color.colorPrimary)
        }
    }
    fun initView(previewBehaviourView: BottomSheetBehavior<View>, fab: FloatingActionButton) {
        this.previewBehaviourView = previewBehaviourView
        previewBehaviourView.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        fab.animate().scaleX(0F).scaleY(0F).setDuration(300).start()
                        mbBookmarkPreviewCardviewId.resetDownIconByTag()
//                        mbBookmarkPreviewCardviewId.setPreviewVisible(true)
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        fab.animate().scaleX(1F).scaleY(1F).setDuration(300).start()
                        mbBookmarkPreviewCardviewId.setPreviewVisible(true)
                    }
                    else -> {}
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
        mbBookmarkPreviewHeaderViewId.setTitle(bookmark.title.let { if (it.isNullOrEmpty()) EMPTY_BOOKMARK_LABEL else it })
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

    fun resetDownIconByTag() {
        mbBookmarkPreviewMoreButtonId.apply {
            tag = "down"
            toggleIcon(tag == "down", R.drawable.ic_arrow_down, R.drawable.ic_arrow_up)
        }
    }

    fun setMoreButtonAction(callbackAction: () -> Unit) {
        mbBookmarkPreviewMoreButtonId.apply {
            toggleIcon(tag == "down", R.drawable.ic_arrow_down, R.drawable.ic_arrow_up)
            setOnClickListener {
                tag = when (tag) {
                    "down" -> "up"
                    else -> "down"
                }
                setPreviewVisible(tag == "down")
                callbackAction.invoke()
                toggleIcon(tag == "down", R.drawable.ic_arrow_down, R.drawable.ic_arrow_up)
            }
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
        val animatorList: List<Animator> = BookmarkAnimator().let {
            when (isPreviewVisible) {
                true -> listOf(it.collapseAnimator(mbBookmarkPreviewDeleteLayoutId),
                        it.expandAnimator(mbBookmarkPreviewHeaderEditUrlLayoutId))
                else -> listOf(it.collapseAnimator(mbBookmarkPreviewHeaderEditUrlLayoutId),
                        it.expandAnimator(mbBookmarkPreviewDeleteLayoutId))
            }
        }

        AnimatorSet().apply {
            playSequentially(animatorList)
            start()
        }
    }

}
