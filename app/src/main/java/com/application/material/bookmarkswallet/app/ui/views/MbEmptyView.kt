package com.application.material.bookmarkswallet.app.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.databinding.EmptyViewBinding
import com.application.material.bookmarkswallet.app.models.BookmarkFilter
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_DEFAULT_VIEW
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_STAR_VIEW
import com.application.material.bookmarkswallet.app.extensions.setColorByRes
import com.application.material.bookmarkswallet.app.extensions.setImageDrawableByRes
import com.application.material.bookmarkswallet.app.extensions.toggleVisibilty
import com.application.material.bookmarkswallet.app.features.bookmarkList.viewmodels.BookmarkViewModel

class MbEmptyView : FrameLayout {
    private lateinit var viewModel: BookmarkViewModel
    val binding by lazy {
        EmptyViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        binding.root
    }

    fun setViewModel(bookmarkViewModel: BookmarkViewModel) {
        viewModel = bookmarkViewModel
    }

    fun init(
        owner: LifecycleOwner,
        bookmarkFilter: BookmarkFilter,
        recyclerView: RecyclerView
    ) {
        viewModel.sizeEmptyDataPair.observe(owner) {
            //search mode
            visibility = when (bookmarkFilter.isSearchViewType) {
                true -> {
                    binding.mbBookmarkEmptyImageViewId.visibility = View.VISIBLE
                    binding.mbBookmarkEmptyImageViewId.setImageDrawableByRes(R.drawable.ic_rabbit_and_fox_illustration)
                    binding.mbBookmarkEmptyLabelTextViewId.setColorByRes(R.color.colorPrimary)
                    binding.mbBookmarkEmptyLabelTextViewId.setText(R.string.no_search_result_string)
                    when (it.first) {
                        0 -> VISIBLE
                        else -> GONE
                    }
                }

                else -> when (bookmarkFilter.starFilterType) {
                    IS_DEFAULT_VIEW -> {
                        binding.mbBookmarkEmptyImageViewId.visibility = View.VISIBLE
                        binding.mbBookmarkEmptyImageViewId.setImageDrawableByRes(R.drawable.ic_fox_sleep_illustration)
                        binding.mbBookmarkEmptyLabelTextViewId.setColorByRes(R.color.colorPrimary)
                        binding.mbBookmarkEmptyLabelTextViewId.setText(R.string.no_bookmark_string)
                        when (it.first) {
                            0 -> VISIBLE
                            else -> GONE
                        }
                    }

                    IS_STAR_VIEW -> {
                        binding.mbBookmarkEmptyImageViewId.visibility = View.GONE
//                        mbBookmarkEmptyImageViewId.setImageDrawableByRes(R.drawable.ic_lamp_illustration)
                        binding.mbBookmarkEmptyLabelTextViewId.setColorByRes(R.color.colorAccent)
                        binding.mbBookmarkEmptyLabelTextViewId.setText(R.string.no_bookmark_star_string)
//                        views[1].visibility = View.VISIBLE
//                        views[2].visibility = View.VISIBLE
                        when (it.second) {
                            0 -> VISIBLE
                            else -> GONE
                        }
                    }
                }
            }

            //handled custom logic to hide views on main view
            if (bookmarkFilter.starFilterType == IS_DEFAULT_VIEW &&
                !bookmarkFilter.isSearchViewType
            ) {
//                views.forEach{
//                    it.toggleVisibilty(this.visibility)
//                }
            }

            recyclerView.toggleVisibilty(this.visibility)
        }
    }
}


