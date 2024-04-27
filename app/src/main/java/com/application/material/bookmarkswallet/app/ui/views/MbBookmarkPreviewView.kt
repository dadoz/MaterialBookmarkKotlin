package com.application.material.bookmarkswallet.app.ui.views

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.databinding.PreviewBookmarkViewBinding
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.BookmarkFilter
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_STAR_VIEW
import com.application.material.bookmarkswallet.app.modules.bookmarkList.BookmarkListAdapter.Companion.EMPTY_BOOKMARK_LABEL
import com.application.material.bookmarkswallet.app.ui.setColor
import com.application.material.bookmarkswallet.app.ui.setColorByRes
import com.application.material.bookmarkswallet.app.ui.setStrokeColorByColorRes
import com.application.material.bookmarkswallet.app.ui.views.behaviors.BookmarkAnimator
import com.application.material.bookmarkswallet.app.viewModels.BookmarkViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MbBookmarkPreviewView : FrameLayout {
    var bookmark: Bookmark? = null
    val binding: PreviewBookmarkViewBinding by lazy {
        PreviewBookmarkViewBinding.inflate(LayoutInflater.from(context), this, true)
    }
    private val bookmarkPreviewHeaderBinding by lazy {
        binding.mbBookmarkPreviewHeaderViewId.binding
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var previewBehaviourView: BottomSheetBehavior<View>? = null

    fun setStarColor(starFilterType: BookmarkFilter.StarFilterTypeEnum) {
        when (starFilterType) {
            IS_STAR_VIEW -> binding.mbBookmarkPreviewHeaderCardViewId.setStrokeColorByColorRes(R.color.colorAccent)
            else -> binding.mbBookmarkPreviewHeaderCardViewId.setStrokeColorByColorRes(R.color.colorPrimary)
        }
    }

    fun initView(
        previewBehaviourView: BottomSheetBehavior<View>,
        fab: FloatingActionButton? = null
    ) {
        this.previewBehaviourView = previewBehaviourView
        fab?.let {
            previewBehaviourView.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            AnimatorSet().apply {
                                playTogether(BookmarkAnimator().scaleXYAnimator(fab, 0F))
                                doOnEnd {
                                    fab.visibility = GONE
                                }
                                start()
                            }
                        }

                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            AnimatorSet().apply {
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
        }

        binding.mbBookmarkPreviewBackButtonId.setOnClickListener {
            setPreviewVisible(true)
        }

    }

    fun initData(bookmark: Bookmark, bookmarkViewModel: BookmarkViewModel) {
        this.bookmark = bookmark
        //set title and icon
        binding.mbBookmarkPreviewHeaderViewId.setViewModel(bookmarkViewModel)
        binding.mbBookmarkPreviewHeaderViewId.setTitle(bookmark.title.let { if (it.isNullOrEmpty()) EMPTY_BOOKMARK_LABEL else it })
        binding.mbBookmarkPreviewHeaderViewId.setDescription(bookmark.timestamp)
        binding.mbBookmarkPreviewHeaderViewId.setIcon(bookmark.image)
        binding.mbBookmarkPreviewUrlTextId.text = "https://${bookmark.url}"
        //after calculation of data - otw cannot retrieve size
        previewBehaviourView?.state = BottomSheetBehavior.STATE_EXPANDED

        when (bookmark.isStar) {
            true -> bookmarkPreviewHeaderBinding.mbBookmarkPreviewStarButtonId.setColor(R.color.colorAccent)
            false -> bookmarkPreviewHeaderBinding.mbBookmarkPreviewStarButtonId.setColor(R.color.colorPrimary)
        }
    }

    fun actionShareBookmark(callbackAction: (intent: Intent) -> Unit) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, bookmark.toString())
            type = "text/plain"
        }

        bookmarkPreviewHeaderBinding.mbShareBookmarkActionId.setOnClickListener {
            callbackAction.invoke(sendIntent)
        }
    }

    fun actionOpenPreviewBookmark(callbackAction: (intent: Intent) -> Unit) {
        binding.mbBookmarkPreviewOpenInBrowserButtonId.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse("https://${bookmark?.url}")
            }
            callbackAction.invoke(sendIntent)
        }
    }

    fun actionEditBookmark(callbackAction: () -> Unit) {
        binding.mbBookmarkPreviewMainLayoutId.setOnClickListener {
            callbackAction.invoke()
        }
    }

    fun setMoreButtonAction(callbackAction: () -> Unit) {
        binding.mbBookmarkPreviewMoreButtonId.apply {
            setOnClickListener {
                setPreviewVisible(false)
                callbackAction.invoke()
            }
        }
    }

    fun setDeleteButtonAction(callbackAction: () -> Unit) {
        binding.mbBookmarkPreviewDeleteButtonId.setOnClickListener {
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
                        true -> listOf(it.expandAnimator(binding.mbBookmarkPreviewOpenLayoutId))
                        else -> listOf(it.expandAnimator(binding.mbBookmarkPreviewDeleteLayoutId))
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
                    binding.mbBookmarkPreviewOpenLayoutId.visibility = it
                    binding.mbBookmarkPreviewEditButtonId.visibility = it
                }
                GONE.let {
                    binding.mbBookmarkPreviewDeleteLayoutId.visibility = it
                    binding.mbBookmarkPreviewDeleteLabelTextId.visibility = it
                }

                binding.mbBookmarkPreviewMainLayoutId.isClickable = true
                binding.mbBookmarkPreviewUrlTextId.setColorByRes(R.color.colorPrimary)

            }

            false -> {

                GONE.let {
                    binding.mbBookmarkPreviewEditButtonId.visibility = it
                    binding.mbBookmarkPreviewOpenLayoutId.visibility = it
                }
                VISIBLE.let {
                    binding.mbBookmarkPreviewDeleteLayoutId.visibility = it
                    binding.mbBookmarkPreviewDeleteLabelTextId.visibility = it
                }
                binding.mbBookmarkPreviewMainLayoutId.isClickable = false
            }
        }
    }
}