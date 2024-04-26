package com.application.material.bookmarkswallet.app.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.application.material.bookmarkswallet.app.databinding.HeaderBookmarkViewBinding
import com.application.material.bookmarkswallet.app.viewModels.BookmarkViewModel
import java.util.Date

class MbBookmarkDetailsHeaderView : RelativeLayout {
    private var viewModel: BookmarkViewModel? = null
    val binding: HeaderBookmarkViewBinding by lazy {
        HeaderBookmarkViewBinding.bind(this)
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setTitle(title: String?) {
        binding.mbBookmarkPreviewHeaderTitleTextViewId.text = title
    }

    fun setDescription(timestamp: Date?) {
        binding.mbBookmarkPreviewHeaderSubtitleTextViewId.text =
            "updated " + timestamp?.toString() + " at " + //"dd MMMM ")
                    timestamp?.toString()//"HH:mm")
    }

    fun setViewModel(bookmarkViewModel: BookmarkViewModel) {
        viewModel = bookmarkViewModel
        binding.bookmarkViewModel = viewModel
    }

    fun setIcon(iconUrl: String?) {
        viewModel?.bookmarkIconUrl?.set(iconUrl)
    }
}
