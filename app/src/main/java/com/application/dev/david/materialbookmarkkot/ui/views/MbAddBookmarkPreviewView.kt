package com.application.dev.david.materialbookmarkkot.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.databinding.AddBookmarkPreviewViewBinding
import com.application.dev.david.materialbookmarkkot.models.setImageViewResource
import com.application.dev.david.materialbookmarkkot.viewModels.AddBookmarkViewModel
import khronos.Dates
import khronos.toString
import kotlinx.android.synthetic.main.add_bookmark_preview_view.view.*
import kotlinx.android.synthetic.main.bookmark_title_icon_layout_view.view.*
import kotlinx.android.synthetic.main.fragment_add_bookmark.view.*

class MbAddBookmarkPreviewView : RelativeLayout {
    private val binding: AddBookmarkPreviewViewBinding
    enum class MbPreviewStatus { UPDATE, SEARCH }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
         binding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.add_bookmark_preview_view, this, true)
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
    fun setEditTitleVisible(isVisible: Boolean) {
        mbNewBookmarkEditTitleViewId.setEditTitleVisible(isVisible)
    }
    /**
     * setvisibility - TODO mv all to viewBinding :P
     */
    fun setStatusVisibility(status: MbPreviewStatus) {
        when(status) {
            MbPreviewStatus.SEARCH -> {
                mbAddNewBookmarkUrlEditLayoutId.visibility = View.GONE
                mbBookmarkUpdateNewLayoutId.visibility = View.GONE
                mbBookmarkSaveNewLayoutId.visibility = View.VISIBLE
                mbBookmarkSaveNewButtonId.visibility = View.VISIBLE
                mbNewBookmarkUrlCardviewId.visibility = View.VISIBLE
            }
            MbPreviewStatus.UPDATE -> {
                mbAddNewBookmarkUrlEditLayoutId.visibility = View.VISIBLE
                mbBookmarkUpdateNewLayoutId.visibility = View.VISIBLE
                mbNewBookmarkUrlCardviewId.visibility = View.GONE
                mbBookmarkSaveNewLayoutId.visibility = View.GONE
                mbBookmarkSaveNewButtonId.visibility = View.GONE
            }
        }

    }

    /**
     * set icon and text - TODO mv all to viewBinding :P
     */
    fun setTitleAndIconImage(title: String, iconUrl: String) {
        mbNewBookmarkTitleEditTextId.setText(title)
        mbNewBookmarkTitleEditTextId.tag = iconUrl
        mbNewBookmarkTitleTextViewId.text = title
    }

    /**
     *
     */
    private fun initView() {
        mbNewBookmarkAddUpdatedTimestampId.text = Dates.today.toString("dd MMM hh:mm")
    }
}