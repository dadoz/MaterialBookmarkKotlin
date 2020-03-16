package com.application.dev.david.materialbookmarkkot.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.application.dev.david.materialbookmarkkot.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import khronos.isFriday
import khronos.toString
import kotlinx.android.synthetic.main.header_bookmark_view.view.*
import java.sql.Timestamp
import java.util.*

class MbBookmarkDetailsHeaderView : RelativeLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflate(context, R.layout.header_bookmark_view, this)
    }

    fun setTitle(title: String?) {
        mbBookmarkPreviewHeaderTitleTextViewId.text = title
    }

    fun setDescription(timestamp: Date?) {
        mbBookmarkPreviewHeaderSubtitleTextViewId.text = "on " + timestamp?.toString("dd MMMM   ") + " at " +
                timestamp?.toString("HH:mm")
    }

    fun setIcon(iconUrl: String?) {
        Glide.with(mbBookmarkPreviewHeaderIconImageViewId.context)
            .load(iconUrl)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(32)))
            .placeholder(R.drawable.ic_bookmark)
            .into(mbBookmarkPreviewHeaderIconImageViewId)
    }
}
