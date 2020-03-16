package com.application.dev.david.materialbookmarkkot.ui

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import io.reactivex.functions.Action
import khronos.Dates
import khronos.toString
import kotlinx.android.synthetic.main.action_bookmark_view.view.*
import kotlinx.android.synthetic.main.add_bookmark_preview_view.view.*
import kotlinx.android.synthetic.main.bookmark_title_icon_layout_view.view.*

class MbAddBookmarkPreviewView : RelativeLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflate(context, R.layout.add_bookmark_preview_view, this)
        initView()
    }
    enum class MbPreviewStatus { UPDATE, SEARCH }

    fun setStatusVisibility(status: MbPreviewStatus) {
        when(status) {
            MbAddBookmarkPreviewView.MbPreviewStatus.SEARCH -> {
                mbNewBookmarkUrlEditLayoutId.visibility = View.GONE
                mbBookmarkUpdateNewLayoutId.visibility = View.GONE
                mbBookmarkSaveNewLayoutId.visibility = View.VISIBLE
                mbBookmarkSaveNewButtonId.visibility = View.VISIBLE
                mbNewBookmarkUrlCardviewId.visibility = View.VISIBLE
            }
            MbAddBookmarkPreviewView.MbPreviewStatus.UPDATE -> {
                mbNewBookmarkUrlEditLayoutId.visibility = View.VISIBLE
                mbBookmarkUpdateNewLayoutId.visibility = View.VISIBLE
                mbNewBookmarkUrlCardviewId.visibility = View.GONE
                mbBookmarkSaveNewLayoutId.visibility = View.GONE
                mbBookmarkSaveNewButtonId.visibility = View.GONE
            }
        }

    }

    fun setTitleAndIconImage(title: String, iconUrl: String) {
        mbNewBookmarkTitleEditTextId.setText(title)
        mbNewBookmarkTitleEditTextId.tag = iconUrl
    }

    private fun initView() {
        mbNewBookmarkAddUpdatedTimestampId.text = Dates.today.toString("dd MMM hh:mm")
    }
}