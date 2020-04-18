package com.application.dev.david.materialbookmarkkot.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.StarFilterTypeEnum.IS_DEFAULT_VIEW
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.StarFilterTypeEnum.IS_STAR_VIEW
import com.application.dev.david.materialbookmarkkot.ui.setColorByRes
import com.application.dev.david.materialbookmarkkot.ui.setImageDrawableByRes
import com.application.dev.david.materialbookmarkkot.ui.toggleVisibilty
import com.application.dev.david.materialbookmarkkot.viewModels.BookmarkViewModel
import kotlinx.android.synthetic.main.empty_view.view.*

class MbEmptyView: FrameLayout {
    private lateinit var viewModel: BookmarkViewModel

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflate(context, R.layout.empty_view, this)
    }

    fun setViewModel(bookmarkViewModel: BookmarkViewModel) {
        viewModel = bookmarkViewModel
    }

    fun init(owner: LifecycleOwner, bookmarkFilter: BookmarkFilter, views: List<View>) {
        val recyclerView = views[0]
        viewModel.sizeEmptyDataPair.observe(owner, Observer {
            visibility = when (bookmarkFilter.starFilterType) {
                IS_DEFAULT_VIEW -> {
                    mbBookmarkEmptyImageViewId.setImageDrawableByRes(R.drawable.ic_tortoise_illustration)
                    mbBookmarkEmptyLabelTextViewId.setColorByRes(R.color.colorPrimary)
                    mbBookmarkEmptyLabelTextViewId.setText(R.string.no_bookmark_string)
                    when (it.first) {
                        0 -> VISIBLE
                        else -> GONE
                    }
                }
                IS_STAR_VIEW -> {
                    mbBookmarkEmptyImageViewId.setImageDrawableByRes(R.drawable.ic_lamp_illustration)
                    mbBookmarkEmptyLabelTextViewId.setColorByRes(R.color.colorAccent)
                    mbBookmarkEmptyLabelTextViewId.setText(R.string.no_bookmark_star_string)
                    views[1].visibility = View.VISIBLE
                    views[2].visibility = View.VISIBLE
                    when (it.second) {
                        0 -> VISIBLE
                        else -> GONE
                    }
                }
            }

            if (bookmarkFilter.starFilterType == IS_DEFAULT_VIEW) {
                views.forEach{
                    it.toggleVisibilty(this.visibility)
                }
            }
            recyclerView.toggleVisibilty(this.visibility)
        })
    }
}


