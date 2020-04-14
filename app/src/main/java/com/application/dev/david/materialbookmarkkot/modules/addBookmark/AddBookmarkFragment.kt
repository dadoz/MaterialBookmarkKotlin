package com.application.dev.david.materialbookmarkkot.modules.addBookmark

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.application.dev.david.materialbookmarkkot.OnFragmentInteractionListener
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.databinding.FragmentAddBookmarkBinding
import com.application.dev.david.materialbookmarkkot.ui.SettingsActivity
import com.application.dev.david.materialbookmarkkot.ui.views.MbAddBookmarkPreviewView.MbPreviewStatus.SEARCH
import com.application.dev.david.materialbookmarkkot.ui.views.MbAddBookmarkPreviewView.MbPreviewStatus.UPDATE
import com.application.dev.david.materialbookmarkkot.ui.changeToolbarFont
import com.application.dev.david.materialbookmarkkot.ui.hideKeyboard
import com.application.dev.david.materialbookmarkkot.viewModels.AddBookmarkViewModel
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.add_bookmark_preview_view.*
import kotlinx.android.synthetic.main.add_bookmark_preview_view.mbNewBookmarkUrlEditTextId
import kotlinx.android.synthetic.main.add_bookmark_preview_view.mbNewBookmarkUrlTextInputLayoutId
import kotlinx.android.synthetic.main.bookmark_title_icon_layout_view.*
import kotlinx.android.synthetic.main.fragment_add_bookmark.*
import kotlinx.android.synthetic.main.fragment_add_bookmark.mbToolbarId
import kotlinx.android.synthetic.main.fragment_search_bookmark.*


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
    private var listener: OnFragmentInteractionListener? = null
    private val navigation: NavController? by lazy {
        view?.let { Navigation.findNavController(it) }
    }
    private val args: AddBookmarkFragmentArgs by navArgs()
    private val addBookmarkViewModel: AddBookmarkViewModel by lazy {
        ViewModelProviders.of(this).get(AddBookmarkViewModel::class.java)
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
        val binding: FragmentAddBookmarkBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_bookmark, container, false)
        binding.addBookmarkViewModel = addBookmarkViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mbAddBookmarkPreviewId.setViewModel(addBookmarkViewModel)
        initActionBar()
        initView()
        when (args.actionType) {
            SAVE_ACTION_BOOKMARK -> args.bookmarkUrl?.let { searchBookmarkAction(it) }
            UPDATE_ACTION_BOOKMARK -> args.bookmark?.let {
                val url = it.getString("bookmark_url")
                val title = it.getString("bookmark_title")
                val iconUrl = it.getString("bookmark_icon_url")
                updateBookmarkView(url, title, iconUrl)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_add_bookmark, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> navigation?.popBackStack()
            R.id.menuSettingsActionId -> startActivity(Intent(context, SettingsActivity::class.java))
        }
        return true
    }

    /**
     * init actionbar
     */
    private fun initActionBar() {
        (activity as AppCompatActivity).setSupportActionBar(mbToolbarId)
        mbToolbarId.changeToolbarFont()
        mbToolbarId.title = getString(R.string.add_actionbar_string)
        mbToolbarId.visibility = VISIBLE
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     *
     */
    @SuppressLint("FragmentLiveDataObserve")
    private fun initView() {
        mbNewBookmarkUrlEditTextId.setText(args.bookmarkUrl)

        mbNewBookmarkUrlCardviewId.setOnClickListener {
            mbAddBookmarkPreviewId.setStatusVisibility(UPDATE)
            mbNewBookmarkTitleTextInputId.visibility = VISIBLE
            mbNewBookmarkTitleTextViewId.visibility = GONE
        }

        // loader cbs
        addBookmarkViewModel.loaderLiveStatus.observe(this, Observer { isVisible ->
            when (isVisible) {
                true -> {
                    mbNewBookmarkTitleLoaderId.visibility = VISIBLE
                    mbNewBookmarkTitleTextInputId.visibility = GONE
                    mbNewBookmarkTitleTextViewId.visibility = GONE
                }
                false -> {
                    mbNewBookmarkTitleLoaderId.visibility = GONE
                    mbNewBookmarkTitleTextInputId.visibility = GONE
                    mbNewBookmarkTitleTextViewId.visibility = VISIBLE
                }
            }
        })
        addBookmarkViewModel.bookmarkSearchedUrlLiveData.observe(this, Observer{ searchedUrl ->
            mbBookmarkSearchedUrlWebViewId.apply {
                visibility = GONE
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        view?.loadUrl(url)
                        return true
                    }

                    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                        super.onReceivedError(view, request, error)
                        view?.visibility = GONE
                        if (mbBookmarkSearchedUrPlaceholderId != null)
                            mbBookmarkSearchedUrPlaceholderId.visibility = VISIBLE
                    }
                }

                loadUrl(searchedUrl)
                settings.javaScriptCanOpenWindowsAutomatically = true
                settings.javaScriptEnabled = true
            }
        })

        addBookmarkViewModel.bookmarkInfoLiveError.observe(this, Observer { error ->
            mbAddBookmarkPreviewId.setEditTitleVisible(true)
        })
        addBookmarkViewModel.bookmarkInfoLiveData.observe(this, Observer{ bookmarkInfo ->
            mbAddBookmarkPreviewId.setTitleAndIconImage(bookmarkInfo.meta.title, bookmarkInfo.meta.image)
       })


        addBookmarkViewModel.saveBookmarkStatus.observe(this, Observer { status ->
            when (status) {
                true -> onSaveWithSuccess()
                false -> onSaveWithError()
            }
        })
        addBookmarkViewModel.updateBookmarkStatus.observe(this, Observer { status ->
            when (status) {
                true -> onUpdateBookmarkWithSuccess()
                false -> onUpdateBookmarkWithError()
            }
        })

        mbBookmarkUpdateNewButtonId.setOnClickListener {
            mbNewBookmarkUrlEditTextId.hideKeyboard()
            addBookmarkViewModel.updateBookmark(
                mbNewBookmarkTitleEditTextId.text.toString(),
                mbNewBookmarkTitleEditTextId.tag.let { it?.toString() ?: "" },
                mbNewBookmarkUrlTextId.text.toString())
        }

        mbBookmarkSaveNewButtonId.setOnClickListener {
            mbNewBookmarkUrlEditTextId.hideKeyboard()
            addBookmarkViewModel.saveBookmark(
                mbNewBookmarkTitleEditTextId.text.toString(),
                mbNewBookmarkTitleEditTextId.tag.let { it?.toString() ?: "" },
                mbNewBookmarkUrlTextId.text.toString()
            )
        }


        mbBookmarkUpdateSearchNewButtonId.setOnClickListener {
            mbAddBookmarkPreviewId.setStatusVisibility(SEARCH)
            searchBookmarkAction(mbNewBookmarkUrlEditTextId.text.toString())
        }

        (mbNewBookmarkUrlEditTextId as AppCompatEditText).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged (text: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(text: Editable?) {
                mbNewBookmarkUrlTextInputLayoutId.error = null
            }
        })

        mbBookmarkSearchedUrlAddPlaceholderId.setOnClickListener {
            context?.let {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(it, this)
            }

        }
        mbNewBookmarkIconImageViewId.setOnClickListener {
            Snackbar.make(it, "show preview url", Snackbar.LENGTH_SHORT).show()
        }

    }

    private fun onUpdateBookmarkWithError() {
        //TODO implement
    }

    private fun onUpdateBookmarkWithSuccess() {
        navigation?.popBackStack()
    }

    /**
     * update status
     */
    private fun updateBookmarkView(url: String?, title: String?, iconUrl: String?) {
        VISIBLE.let {
            mbBookmarkUpdateNewButtonId.visibility = it
            mbNewBookmarkTitleTextInputId.visibility = it
        }
        GONE.let {
            mbBookmarkSaveNewButtonId.visibility = it
            mbNewBookmarkTitleTextViewId.visibility = it
            mbNewBookmarkTitleLoaderId.visibility = it
            mbNewBookmarkAddEditButtonId.visibility = it
        }

        mbNewBookmarkTitleTextInputId.requestFocus()

        mbNewBookmarkUrlTextId.text = url
        mbNewBookmarkTitleTextViewId.text = title
        mbNewBookmarkTitleEditTextId.setText(title)
        iconUrl?.let {
            mbNewBookmarkTitleEditTextId.tag = iconUrl
            addBookmarkViewModel.bookmarkIconUrl.set(iconUrl)
        }
        url?.let { addBookmarkViewModel.updateWebviewByUrl(url) }
        mbNewBookmarkUrlCardviewId.isClickable = false
    }

    /**
     * search
     */
    private fun searchBookmarkAction(url: String) {
        mbBookmarkUpdateNewButtonId.visibility = GONE
        VISIBLE.let {
            mbBookmarkSaveNewButtonId.visibility = it
            mbNewBookmarkAddEditButtonId.visibility = it
        }

        mbNewBookmarkUrlTextId.text = url
        addBookmarkViewModel.updateWebviewByUrl(url)
        addBookmarkViewModel.findBookmarkInfoByUrl(url)
    }

    /**
     *
     */
    private fun onSaveWithSuccess() {
        navigation?.popBackStack()
        navigation?.popBackStack()
    }

    /**
     *
     */
    private fun onSaveWithError() {
        mbNewBookmarkUrlTextInputLayoutId.error = "Oh Snap! We got an error:"
        mbNewBookmarkUrlCardviewId.callOnClick()//trigger click
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        result.uri.toString().let {
                            mbNewBookmarkTitleEditTextId.tag = it
                            addBookmarkViewModel.bookmarkIconUrl.set(it)
                        }
                    }
                    CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> { val error = result.error }
                }
            }
        }
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

    companion object {
        const val SAVE_ACTION_BOOKMARK = "SAVE_ACTION_BOOKMARK"
        const val UPDATE_ACTION_BOOKMARK = "UPDATE_ACTION_BOOKMARK"
    }
}