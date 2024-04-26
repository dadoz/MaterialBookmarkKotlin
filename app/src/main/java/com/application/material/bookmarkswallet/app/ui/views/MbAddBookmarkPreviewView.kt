package com.application.material.bookmarkswallet.app.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.application.material.bookmarkswallet.app.databinding.AddBookmarkPreviewViewBinding
import com.application.material.bookmarkswallet.app.ui.views.MbAddBookmarkPreviewView.MbPreviewStatus.SEARCH
import com.application.material.bookmarkswallet.app.ui.views.MbAddBookmarkPreviewView.MbPreviewStatus.UPDATE
import com.application.material.bookmarkswallet.app.viewModels.AddBookmarkViewModel

class MbAddBookmarkPreviewView : RelativeLayout {
    val binding: AddBookmarkPreviewViewBinding by lazy {
        AddBookmarkPreviewViewBinding.bind(this)
    }

    enum class MbPreviewStatus { UPDATE, SEARCH }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    /**
     * set ViewModel to let work ViewBinding
     */
    fun setViewModel(viewModel: AddBookmarkViewModel) {
        binding.addBookmarkViewModel = viewModel
    }

    /**
     * setvisibility - TODO mv all to viewBinding :P
     */
    fun setEditTitleVisible(isVisible: Boolean, isError: Boolean = false) {
        binding.mbNewBookmarkEditTitleViewId.setEditTitleVisible(isVisible, isError)
    }

    /**
     * setvisibility - TODO mv all to viewBinding :P
     */
    fun setStatusVisibility(status: MbPreviewStatus) {
        binding.also {
            when (status) {
                SEARCH -> {
                    it.mbAddNewBookmarkUrlEditLayoutId.visibility = View.GONE
                    it.mbBookmarkUpdateNewLayoutId.visibility = View.GONE
                    it.mbBookmarkSaveNewLayoutId.visibility = View.VISIBLE
                    it.mbBookmarkSaveNewButtonId.visibility = View.VISIBLE
                    it.mbNewBookmarkUrlCardviewId.visibility = View.VISIBLE
                }

                UPDATE -> {
                    it.mbAddNewBookmarkUrlEditLayoutId.visibility = View.VISIBLE
                    it.mbBookmarkUpdateNewLayoutId.visibility = View.VISIBLE
                    it.mbNewBookmarkUrlCardviewId.visibility = View.GONE
                    it.mbBookmarkSaveNewLayoutId.visibility = View.GONE
                    it.mbBookmarkSaveNewButtonId.visibility = View.GONE
                }
            }
        }
    }

    /**
     * set icon and text - TODO mv all to viewBinding :P
     */
    fun setTitleAndIconImage(title: String, iconUrl: String) {
        binding
            .also {
//                it.mbNewBookmarkTitleEditTextId.setText(title)
//                it.mbNewBookmarkTitleEditTextId.tag = iconUrl
//                it.mbNewBookmarkTitleTextViewId.text = title
            }
    }

    /**
     *
     */
    private fun initView() {
        binding.mbNewBookmarkAddUpdatedTimestampId.text =
            "EMPTY"//Dates.today.toString("dd MMM hh:mm")
    }
}