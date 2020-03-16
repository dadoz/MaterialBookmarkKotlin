package com.application.dev.david.materialbookmarkkot.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import io.reactivex.functions.Action
import kotlinx.android.synthetic.main.action_bookmark_view.view.*

class MbBookmarkActionView : RelativeLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflate(context, R.layout.action_bookmark_view, this)
    }
}
