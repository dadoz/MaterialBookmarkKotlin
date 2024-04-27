package com.application.material.bookmarkswallet.app.features.addBookmark

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.*
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.application.material.bookmarkswallet.app.OnFragmentInteractionListener
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.databinding.FragmentAddBookmarkBinding
import com.application.material.bookmarkswallet.app.hideKeyboard
import com.application.material.bookmarkswallet.app.hideKeyboardIfNeeded
import com.application.material.bookmarkswallet.app.showKeyboard
import com.application.material.bookmarkswallet.app.ui.*
import com.application.material.bookmarkswallet.app.ui.views.MbAddBookmarkPreviewView.MbPreviewStatus.SEARCH
import com.application.material.bookmarkswallet.app.ui.views.MbAddBookmarkPreviewView.MbPreviewStatus.UPDATE
import com.application.material.bookmarkswallet.app.viewModels.AddBookmarkViewModel

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
    private lateinit var binding: FragmentAddBookmarkBinding
    private val newBookmarkEditTitleViewBinding by lazy {
        (binding.mbAddBookmarkPreviewId).binding.mbNewBookmarkEditTitleViewId.binding
    }
    private val addBookmarkPreviewBinding by lazy {
        (binding.mbAddBookmarkPreviewId).binding
    }
    private var listener: OnFragmentInteractionListener? = null
    private val navigation: NavController? by lazy {
        view?.let { Navigation.findNavController(it) }
    }
    private val addBookmarkViewModel: AddBookmarkViewModel by lazy {
        ViewModelProvider(this)[AddBookmarkViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAddBookmarkBinding.inflate(inflater)
        .also {
            binding = it
        }
        .also {
            it.addBookmarkViewModel = addBookmarkViewModel
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mbAddBookmarkPreviewId.setViewModel(addBookmarkViewModel)
        initActionBar()
        initView()
        when (arguments?.getString("actionType")) {
            SAVE_ACTION_BOOKMARK -> arguments?.getString("bookmarkUrl")
                ?.let { searchBookmarkAction(it) }

            UPDATE_ACTION_BOOKMARK -> arguments?.getBundle("bookmark")
                ?.let {
                    val url = it.getString("bookmark_url")
                    val title = it.getString("bookmark_title")
                    val iconUrl = it.getString("bookmark_icon_url")
                    updateBookmarkView(url, title, iconUrl)
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_add_bookmark, menu)
//        menu.findItem(R.id.menuSettingsActionId).applyFontToMenuItem(requireContext())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                navigation?.popBackStack()
                activity?.hideKeyboardIfNeeded()
            }

            R.id.menuShowEditActionId -> showEditBookmarkView()
            R.id.menuSettingsActionId -> startActivity(
                Intent(
                    context,
                    SettingsActivity::class.java
                )
            )
        }
        return true
    }

    private fun showEditBookmarkView() {
        binding.mbBookmarkSearchedUrlWebViewId.visibility = GONE
        (binding.mbAddBookmarkPreviewId).binding.mbNewBookmarkEditTitleViewId.visibility = VISIBLE
    }

    /**
     * init actionbar
     */
    private fun initActionBar() {
//        (activity as AppCompatActivity).setSupportActionBar(binding.mbToolbarId)
//        binding.mbToolbarId.changeToolbarFont()
//        binding.mbToolbarId.title = getString(R.string.add_actionbar_string)
//        binding.mbToolbarId.visibility = VISIBLE
//        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     *
     */
    @SuppressLint("FragmentLiveDataObserve")
    private fun initView() {
        addBookmarkPreviewBinding
            .also { addBookmarkPreviewBinding ->
                addBookmarkPreviewBinding.mbNewBookmarkUrlEditTextId.setText(
                    arguments?.getString("bookmarkUrl") ?: "EMPTY"
                )

                addBookmarkPreviewBinding.mbNewBookmarkUrlCardviewId.setOnClickListener {
                    binding.mbAddBookmarkPreviewId.setStatusVisibility(UPDATE)
                    addBookmarkPreviewBinding.mbNewBookmarkUrlEditTextId.requestFocus()
                    addBookmarkPreviewBinding.mbNewBookmarkUrlEditTextId.showKeyboard()
                    newBookmarkEditTitleViewBinding
                        .also {
                            it.mbNewBookmarkTitleTextInputId.visibility = VISIBLE
                            it.mbNewBookmarkTitleTextViewId.visibility = GONE
                            it.mbNewBookmarkIconImageViewId.visibility = GONE
                        }
                    showEditBookmarkView()
                }

                // loader cbs
                newBookmarkEditTitleViewBinding
                    .also {
                        addBookmarkViewModel.loaderLiveStatus.observe(this) { isVisible ->
                            when (isVisible) {
                                true -> {
                                    it.mbNewBookmarkTitleLoaderId.visibility = VISIBLE
                                    it.mbNewBookmarkTitleTextInputId.visibility = GONE
                                    it.mbNewBookmarkTitleTextViewId.visibility = GONE
                                }

                                false -> {
                                    it.mbNewBookmarkTitleLoaderId.visibility = GONE
                                    it.mbNewBookmarkTitleTextInputId.visibility = GONE
                                    it.mbNewBookmarkTitleTextViewId.visibility = VISIBLE
                                }
                            }
                        }
                    }
            }

        addBookmarkViewModel.bookmarkSearchedUrlLiveData.observe(this, Observer { searchedUrl ->
            binding.mbBookmarkSearchedUrlWebViewId.apply {
                visibility = GONE
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        url?.let {
                            view?.loadUrl(it)
                        }
                        return true
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        super.onReceivedError(view, request, error)
                        view?.visibility = GONE
//                            if (binding.mbBookmarkSearchedUrPlaceholderId != null) {
//                                binding.mbBookmarkSearchedUrPlaceholderId.visibility = VISIBLE
//                            }
                    }
                }

                loadUrl(searchedUrl)
                settings.javaScriptCanOpenWindowsAutomatically = true
                settings.javaScriptEnabled = true
            }
        })

        addBookmarkViewModel.bookmarkInfoLiveError.observe(this, Observer { error ->
            binding.mbAddBookmarkPreviewId.setEditTitleVisible(
                isVisible = true,
                isError = error.isNotEmpty()
            )
        })

        addBookmarkViewModel.bookmarkInfoLiveData.observe(this, Observer { bookmarkInfo ->
            binding.mbAddBookmarkPreviewId.setTitleAndIconImage(
                bookmarkInfo.meta.title,
                bookmarkInfo.meta.image
            )
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

        addBookmarkPreviewBinding
            .also { addBinding ->
                addBinding.mbBookmarkUpdateNewButtonId.setOnClickListener {
                    addBinding.mbNewBookmarkUrlEditTextId.hideKeyboard()
                    addBookmarkViewModel.updateBookmark(
                        newBookmarkEditTitleViewBinding.mbNewBookmarkTitleEditTextId.text.toString(),
                        newBookmarkEditTitleViewBinding.mbNewBookmarkTitleEditTextId.tag.let {
                            it?.toString() ?: ""
                        },
                        addBinding.mbNewBookmarkUrlTextId.text.toString()
                    )
                }
            }

        addBookmarkPreviewBinding
            .also { addBinding ->
                addBinding.mbBookmarkSaveNewButtonId.setOnClickListener {
                    addBinding.mbNewBookmarkUrlEditTextId.hideKeyboard()
                    addBookmarkViewModel.saveBookmark(
                        newBookmarkEditTitleViewBinding.mbNewBookmarkTitleEditTextId.text.toString(),
                        newBookmarkEditTitleViewBinding.mbNewBookmarkTitleEditTextId.tag.let {
                            it?.toString() ?: ""
                        },
                        addBinding.mbNewBookmarkUrlTextId.text.toString()
                    )
                }
            }
        addBookmarkPreviewBinding
            .also { addBinding ->
                addBinding.mbBookmarkUpdateSearchNewButtonId
                    .setOnClickListener {
                        binding.mbAddBookmarkPreviewId.setStatusVisibility(SEARCH)
                        searchBookmarkAction(addBinding.mbNewBookmarkUrlEditTextId.text.toString())
                        addBinding.mbNewBookmarkUrlEditTextId.hideKeyboard()
                        newBookmarkEditTitleViewBinding.mbNewBookmarkTitleEditTextId.hideKeyboard()
                        newBookmarkEditTitleViewBinding.mbNewBookmarkIconImageViewId.visibility =
                            VISIBLE
                    }
            }
        addBookmarkPreviewBinding
            .also { addBinding ->
                (addBinding.mbNewBookmarkUrlEditTextId as AppCompatEditText).addTextChangedListener(
                    object :
                        TextWatcher {
                        override fun beforeTextChanged(
                            text: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            text: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun afterTextChanged(text: Editable?) {
                            addBinding.mbNewBookmarkUrlTextInputLayoutId.error = null
                        }
                    })
            }

        binding.mbBookmarkSearchedUrlAddPlaceholderId.setOnClickListener {
            context?.let {
//                CropImage.activity()
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .start(it, this)
            }

        }
        addBookmarkPreviewBinding
            .also { addBinding ->
                newBookmarkEditTitleViewBinding.mbNewBookmarkIconImageViewId.setOnClickListener {
                    binding.mbBookmarkSearchedUrlWebViewId.visibility = VISIBLE
                    addBinding.mbNewBookmarkEditTitleViewId.visibility = GONE
                }
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
        addBookmarkPreviewBinding
            .also { binding ->

                VISIBLE
                    .also {
                        binding.mbBookmarkUpdateNewButtonId.visibility = it
                        newBookmarkEditTitleViewBinding.mbNewBookmarkTitleTextInputId.visibility =
                            it
                    }
                GONE
                    .also {
                        binding.mbBookmarkSaveNewButtonId.visibility = it
                        newBookmarkEditTitleViewBinding.mbNewBookmarkTitleTextViewId.visibility = it
                        newBookmarkEditTitleViewBinding.mbNewBookmarkTitleLoaderId.visibility = it
                        binding.mbNewBookmarkAddEditButtonId.visibility = it
                    }

                newBookmarkEditTitleViewBinding.mbNewBookmarkTitleTextInputId.requestFocus()

                binding.mbNewBookmarkUrlTextId.text = url
                newBookmarkEditTitleViewBinding.mbNewBookmarkTitleTextViewId.text = title
                newBookmarkEditTitleViewBinding.mbNewBookmarkTitleEditTextId.setText(title)
                iconUrl?.also {
                    newBookmarkEditTitleViewBinding.mbNewBookmarkTitleEditTextId.tag = iconUrl
                    addBookmarkViewModel.bookmarkIconUrl.set(iconUrl)
                }
                url?.let { addBookmarkViewModel.updateWebviewByUrl(url) }
                binding.mbNewBookmarkUrlCardviewId.isClickable = false
            }
    }

    /**
     * search
     */
    private fun searchBookmarkAction(url: String) {
        addBookmarkPreviewBinding
            .also { binding ->
                binding.mbBookmarkUpdateNewButtonId.visibility = GONE
                VISIBLE.let {
                    binding.mbBookmarkSaveNewButtonId.visibility = it
                    binding.mbNewBookmarkAddEditButtonId.visibility = it
                }

                binding.mbNewBookmarkUrlTextId.text = url
                addBookmarkViewModel.updateWebviewByUrl(url)
                addBookmarkViewModel.findBookmarkInfoByUrl(url)
            }
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
        addBookmarkPreviewBinding
            .also { addBinding ->
                addBinding.mbNewBookmarkUrlTextInputLayoutId.error =
                    getString(R.string.oh_snap_error_string)
                addBinding.mbNewBookmarkUrlCardviewId.callOnClick()//trigger click
            }
        //TODO handle error box (change colors even on Title and other stuff
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
//                val result = CropImage.getActivityResult(data)
//                when (resultCode) {
//                    Activity.RESULT_OK -> {
//                        result.uri.toString().let {
//                            binding.mbNewBookmarkTitleEditTextId.tag = it
//                            addBookmarkViewModel.bookmarkIconUrl.set(it)
//                        }
//                    }
//
//                    CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
//                        val error = result.error
//                    }
//                }
//            }
//        }
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
