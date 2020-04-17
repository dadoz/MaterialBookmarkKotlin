package com.application.dev.david.materialbookmarkkot.ui.views

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.StarFilterTypeEnum.IS_STAR_VIEW
import com.application.dev.david.materialbookmarkkot.modules.bookmarkList.BookmarkListAdapter.Companion.EMPTY_BOOKMARK_LABEL
import com.application.dev.david.materialbookmarkkot.ui.setColor
import com.application.dev.david.materialbookmarkkot.ui.setColorByRes
import com.application.dev.david.materialbookmarkkot.ui.setStrokeColorByColorRes
import com.application.dev.david.materialbookmarkkot.ui.views.behaviors.BookmarkAnimator
import com.application.dev.david.materialbookmarkkot.viewModels.BookmarkViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
                        AnimatorSet(). apply {
                            playTogether(BookmarkAnimator().scaleXYAnimator(fab, 0F))
                            doOnEnd {
                                fab.visibility = GONE
                            }
                            start()
                        }
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        AnimatorSet(). apply {
                            playTogether(BookmarkAnimator().scaleXYAnimator(fab, 1F))
                            doOnStart {
                                fab.visibility = View.VISIBLE
                            }
                            start()
                        }
                        setPreviewVisible(true, false)
                    }
                    else -> {}
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        mbBookmarkPreviewBackButtonId.setOnClickListener {
            setPreviewVisible(true)
        }

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

        when (bookmark.isStar) {
            true -> mbBookmarkPreviewStarButtonId.setColor(R.color.colorAccent)
            false -> mbBookmarkPreviewStarButtonId.setColor(R.color.colorPrimary)
        }
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
        mbBookmarkPreviewMainLayoutId.setOnClickListener {
            callbackAction.invoke()
        }
    }

    fun setMoreButtonAction(callbackAction: () -> Unit) {
        mbBookmarkPreviewMoreButtonId.apply {
            setOnClickListener {
                setPreviewVisible(false)
                callbackAction.invoke()
            }
        }
    }

    fun setDeleteButtonAction(callbackAction: () -> Unit) {
        mbBookmarkPreviewDeleteButtonId.setOnClickListener {
            previewBehaviourView?.state = BottomSheetBehavior.STATE_COLLAPSED
            callbackAction.invoke()
        }
    }

    /**
     * logic to handle preview visibility
     */
    private fun setPreviewVisible(isPreviewVisible: Boolean, isAnimated: Boolean = true) {
        when (isAnimated) {
            false -> {
                setVisibilityOnViews(isPreviewVisible)
            }
            true -> {
                val animatorList: List<Animator> = BookmarkAnimator().let {
                    when (isPreviewVisible) {
                        true -> listOf(it.expandAnimator(mbBookmarkPreviewOpenLayoutId))
                        else -> listOf(it.expandAnimator(mbBookmarkPreviewDeleteLayoutId))
                    }
                }

                AnimatorSet().apply {
                    playSequentially(animatorList)
                    doOnStart {
                        setVisibilityOnViews(isPreviewVisible)
                    }
                    start()
                }
            }
        }
    }

    private fun setVisibilityOnViews(isPreviewVisible: Boolean) {
        when (isPreviewVisible) {
            true -> {
                VISIBLE.let {
                    mbBookmarkPreviewOpenLayoutId.visibility = it
                    mbBookmarkPreviewEditButtonId.visibility = it
                }
                GONE.let {
                    mbBookmarkPreviewDeleteLayoutId.visibility = it
                    mbBookmarkPreviewDeleteLabelTextId.visibility = it
                }

                mbBookmarkPreviewMainLayoutId.isClickable = true
                mbBookmarkPreviewUrlTextId.setColorByRes(R.color.colorPrimary)

            }
            false -> {

                GONE.let {
                    mbBookmarkPreviewEditButtonId.visibility = it
                    mbBookmarkPreviewOpenLayoutId.visibility = it
                }
                VISIBLE.let {
                    mbBookmarkPreviewDeleteLayoutId.visibility = it
                    mbBookmarkPreviewDeleteLabelTextId.visibility = it
                }
                mbBookmarkPreviewMainLayoutId.isClickable = false

            }
        }
    }

}