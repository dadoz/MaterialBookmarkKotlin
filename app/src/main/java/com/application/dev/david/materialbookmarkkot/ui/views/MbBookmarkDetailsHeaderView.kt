package com.application.dev.david.materialbookmarkkot.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.databinding.HeaderBookmarkViewBinding
import com.application.dev.david.materialbookmarkkot.viewModels.BookmarkViewModel
import khronos.toString
import kotlinx.android.synthetic.main.header_bookmark_view.view.*
import java.util.*

class MbBookmarkDetailsHeaderView : RelativeLayout {
    private var viewModel: BookmarkViewModel? = null
    private var binding: HeaderBookmarkViewBinding

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
         binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.header_bookmark_view, this, true)
    }

    fun setTitle(title: String?) {
        mbBookmarkPreviewHeaderTitleTextViewId.text = title
    }

    fun setDescription(timestamp: Date?) {
        mbBookmarkPreviewHeaderSubtitleTextViewId.text = "updated " + timestamp?.toString("dd MMMM ") + " at " +
                timestamp?.toString("HH:mm")
    }

    fun setViewModel(bookmarkViewModel: BookmarkViewModel) {
        viewModel = bookmarkViewModel
        binding.bookmarkViewModel = viewModel
    }

    fun setIcon(iconUrl: String?) {
        viewModel?.bookmarkIconUrl?.set(iconUrl)
    }
}
