package com.application.dev.david.materialbookmarkkot.modules.addBookmark

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.application.dev.david.materialbookmarkkot.OnFragmentInteractionListener
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.ui.hideKeyboard
import com.application.dev.david.materialbookmarkkot.viewModels.AddBookmarkViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.add_bookmark_error_view.*
import kotlinx.android.synthetic.main.add_bookmark_success_view.*
import kotlinx.android.synthetic.main.fragment_add_bookmark.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AddBookmarkFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AddBookmarkFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AddBookmarkFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null
    private val addBookmarkViewModel: AddBookmarkViewModel by lazy {
        ViewModelProviders.of(this).get(AddBookmarkViewModel::class.java)
    }
    private val saveNewSuccessCardviewBottomSheetBehavior: BottomSheetBehavior<MaterialCardView> by lazy {
        from(mbBookmarkSaveNewSuccessCardviewId)
    }

    private val saveNewErrorCardviewBottomSheetBehavior: BottomSheetBehavior<MaterialCardView> by lazy {
        from(mbBookmarkSaveNewErrorCardviewId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_bookmark, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_add_bookmark, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_search_bookmark -> {
                val url = (mbNewBookmarkUrlEditTextId as EditText).text.toString()
                mbNewBookmarkUrlTextId.text = url
                addBookmarkViewModel.updateWebviewByUrl(url)
                addBookmarkViewModel.findBookmarkInfoByUrl(url)

                mbBookmarkSearchedUrlWebViewId.visibility = VISIBLE
                mbBookmarkSaveNewCardviewId.visibility = VISIBLE
                mbNewBookmarkIconLayoutId.visibility = VISIBLE
                mbNewBookmarkUrlCardviewId.visibility = VISIBLE
                mbNewBookmarkUrlEditCardviewId.visibility = GONE
            }
        }
        return super.onOptionsItemSelected(item)

    }
    /**
     * init actionbar
     */
    private fun initActionBar() {
        listener?.showActionBarView(getString(R.string.new_bookmark_string))
    }

    /**
     *
     */
    private fun initView() {
        val sheetBehavior = from(mbBookmarkSaveNewCardviewId)
        mbNewBookmarkUrlCardviewId.setOnClickListener {
            mbNewBookmarkUrlCardviewId.visibility = GONE
            mbNewBookmarkUrlEditCardviewId.visibility = VISIBLE
            sheetBehavior.state = STATE_COLLAPSED
        }

        // loader cbs
        addBookmarkViewModel.loaderLiveStatus.observe(this, Observer { isVisible ->
            when (isVisible) {
                true -> {
                    mbNewBookmarkTitleLoaderId.visibility = VISIBLE
                    mbNewBookmarkTitleTextInputId.visibility = GONE
                    mbNewBookmarkIconImageViewId.visibility = GONE
                }
                false -> {
                    mbNewBookmarkTitleLoaderId.visibility = GONE
                    mbNewBookmarkTitleTextInputId.visibility = VISIBLE
                    mbNewBookmarkIconImageViewId.visibility = VISIBLE
                }
            }
        })
        addBookmarkViewModel.bookmarkSearchedUrlLiveData.observe(this, Observer{ searchedUrl ->
            mbBookmarkSearchedUrlWebViewId.loadUrl(searchedUrl)
            sheetBehavior.state = STATE_EXPANDED
            Log.e(javaClass.name, "blalllala " + searchedUrl)
        })

        addBookmarkViewModel.bookmarkInfoLiveData.observe(this, Observer{ bookmarkInfo ->
            mbNewBookmarkTitleEditTextId.setText(bookmarkInfo.meta.title)

            //setting tag on view
            mbNewBookmarkTitleEditTextId.tag = bookmarkInfo.meta.image

            Glide.with(mbNewBookmarkIconImageViewId.context)
                .load(bookmarkInfo.meta.image)
                .apply(RequestOptions.circleCropTransform())
                .into(mbNewBookmarkIconImageViewId)
        })

        addBookmarkViewModel.saveBookmarkStatus.observe(this, Observer { status ->
            when (status) {
                true -> onSaveWithSuccess()
                false -> onSaveWithError()
            }
        })

        mbBookmarkSaveNewButtonId.setOnClickListener {
            mbNewBookmarkUrlEditTextId.hideKeyboard()
            sheetBehavior.state = STATE_COLLAPSED
            addBookmarkViewModel.saveBookmark(
                mbNewBookmarkTitleEditTextId.text.toString(),
                mbNewBookmarkTitleEditTextId.tag.toString(),
                mbNewBookmarkUrlTextId.text.toString()
            )
        }

    }

    /**
     *
     */
    private fun onSaveWithSuccess() {
        saveNewSuccessCardviewBottomSheetBehavior.state = STATE_EXPANDED
    }

    /**
     *
     */
    private fun onSaveWithError() {
        mbBookmarkSaveNewErrorMessageId.text = "Oh Snap! We got an error:"
        saveNewErrorCardviewBottomSheetBehavior.state = STATE_EXPANDED
        mbNewBookmarkUrlEditCardviewId.strokeColor = ContextCompat.getColor(context!!, R.color.colorError)
        mbNewBookmarkUrlEditCardviewId.setCardBackgroundColor(ContextCompat.getColor(context!!, android.R.color.white))
        mbNewBookmarkUrlEditTitleId.setTextColor(ContextCompat.getColor(context!!, R.color.colorError))
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}