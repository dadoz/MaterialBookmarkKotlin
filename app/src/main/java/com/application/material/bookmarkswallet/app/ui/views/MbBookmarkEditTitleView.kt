package com.application.material.bookmarkswallet.app.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.application.material.bookmarkswallet.app.databinding.BookmarkTitleIconLayoutViewBinding

class MbBookmarkEditTitleView : RelativeLayout {
    val binding: BookmarkTitleIconLayoutViewBinding by lazy {
        BookmarkTitleIconLayoutViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        binding.mbNewBookmarkTitleTextViewId.text = ""
    }

    /**
     * setvisibility - TODO mv all to viewBinding :P
     */
    fun setEditTitleVisible(isVisible: Boolean, isError: Boolean = false) {
        when (isVisible) {
            false -> {
                binding.mbNewBookmarkTitleTextViewId.visibility = View.VISIBLE
                binding.mbNewBookmarkTitleTextInputId.visibility = View.GONE
            }

            true -> {
                binding.mbNewBookmarkTitleTextViewId.visibility = View.GONE
                binding.mbNewBookmarkTitleTextInputId.visibility = View.VISIBLE
                when (isError) {
                    true -> {
                        binding.mbNewBookmarkTitleEditTextId.tag = null
                        binding.mbNewBookmarkTitleEditTextId.setText("")
                    }

                    false -> {
                    }
                }
            }
        }
    }
}
