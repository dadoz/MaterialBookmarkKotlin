package com.application.dev.david.materialbookmarkkot.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.*
import android.widget.RelativeLayout
import com.application.dev.david.materialbookmarkkot.R
import kotlinx.android.synthetic.main.bookmark_title_icon_layout_view.view.*

class MbBookmarkEditTitleView : RelativeLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflate(context, R.layout.bookmark_title_icon_layout_view, this)
    }


    /**
     * setvisibility - TODO mv all to viewBinding :P
     */
    fun setEditTitleVisible(isVisible: Boolean) {
        when (isVisible) {
            false -> {
                mbNewBookmarkTitleTextViewId.visibility = View.VISIBLE
                mbNewBookmarkTitleTextInputId.visibility = View.GONE
            }
            true -> {
                mbNewBookmarkTitleTextViewId.visibility = View.GONE
                mbNewBookmarkTitleTextInputId.visibility = View.VISIBLE
            }
        }
    }
}
