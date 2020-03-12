package com.application.dev.david.materialbookmarkkot.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.application.dev.david.materialbookmarkkot.R
import khronos.Dates
import khronos.toString
import kotlinx.android.synthetic.main.add_bookmark_preview_view.view.*

class MbBookmarkPreviewView : RelativeLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflate(context, R.layout.add_bookmark_preview_view, this)
        initView()
    }

    private fun initView() {
        mbNewBookmarkAddUpdatedTimestampId.text = Dates.today.toString("dd MMM hh:mm")
    }
}
