package com.application.material.bookmarkswallet.app.features.addBookmark

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.databinding.FragmentAddBookmarkBinding
import com.application.material.bookmarkswallet.app.extensions.hideKeyboard
import com.application.material.bookmarkswallet.app.extensions.hideKeyboardIfNeeded
import com.application.material.bookmarkswallet.app.extensions.showKeyboard
import com.application.material.bookmarkswallet.app.features.addBookmark.viewmodels.AddBookmarkViewModel
import com.application.material.bookmarkswallet.app.features.settings.SettingsActivity
import com.application.material.bookmarkswallet.app.ui.*
import com.application.material.bookmarkswallet.app.ui.views.MbAddBookmarkPreviewView.MbPreviewStatus.SEARCH
import com.application.material.bookmarkswallet.app.ui.views.MbAddBookmarkPreviewView.MbPreviewStatus.UPDATE

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AddBookmarkFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AddBookmarkFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
@Deprecated("not used")
class AddBookmarkFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentAddBookmarkBinding
    private val newBookmarkEditTitleViewBinding by lazy {
        (binding.mbAddBookmarkPreviewId).binding.mbNewBookmarkEditTitleViewId.binding
    }
    private val addBookmarkPreviewBinding by lazy {
        (binding.mbAddBookmarkPreviewId).binding
    }
    private val navigation: NavController? by lazy {
        view?.let { Navigation.findNavController(it) }
    }
    private val addBookmarkViewModel: AddBookmarkViewModel by lazy {
        ViewModelProvider(this)[AddBookmarkViewModel::class.java]
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
                ?.let {
                    searchBookmarkAction(it)
                }

            UPDATE_ACTION_BOOKMARK -> arguments?.getBundle("bookmark")
                ?.let {
                    val url = it.getString("bookmark_url")
                    val title = it.getString("bookmark_title")
                    val iconUrl = it.getString("bookmark_icon_url")
                    updateBookmarkView(url, title, iconUrl)
                }
        }
    }

    private fun showEditBookmarkView() {
        binding.mbBookmarkSearchedUrlWebViewId.visibility = GONE
        (binding.mbAddBookmarkPreviewId).binding.mbNewBookmarkEditTitleViewId.visibility = VISIBLE
    }

    /**
     * init actionbar
     */
    private fun initActionBar() {
        requireActivity()
            .apply {
                this.addMenuProvider(this@AddBookmarkFragment, viewLifecycleOwner)
                //todo please clean up
//            (this as? AppCompatActivity)?.also {
//                it.setSupportActionBar(binding.mbToolbarId)
//                it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
//            }
//            binding.mbToolbarId.changeToolbarFont()
//            binding.mbToolbarId.title = getString(R.string.add_actionbar_string)
//            binding.mbToolbarId.visibility = VISIBLE
            }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_add_bookmark, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
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
                    @Deprecated("Deprecated in Java")
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
                bookmarkInfo.sitename,
                bookmarkInfo.favicon
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
                    //save bookmark
                    addBinding.mbNewBookmarkUrlTextId.visibility = VISIBLE
                    newBookmarkEditTitleViewBinding.mbNewBookmarkTitleEditTextId
                        .let {
                            addBookmarkViewModel.updateBookmark(
                                title = it.text.toString(),
                                iconUrl = addBookmarkViewModel.bookmarkIconUrl.get(),
                                url = addBinding.mbNewBookmarkUrlTextId.text.toString()
                            )
                        }
                }
            }

        addBookmarkPreviewBinding
            .also { addBinding ->
                addBinding.mbBookmarkSaveNewButtonId
                    .setOnClickListener {
                        //hide keyboard
                        addBinding.mbNewBookmarkUrlEditTextId.hideKeyboard()
                        addBinding.mbNewBookmarkUrlTextId.visibility = VISIBLE
                        //save bookmark
                        newBookmarkEditTitleViewBinding.mbNewBookmarkTitleEditTextId
                            .let {
                                addBookmarkViewModel.saveBookmark(
                                    title = it.text.toString(),
                                    iconUrl = addBookmarkViewModel.bookmarkIconUrl.get(),
                                    url = addBinding.mbNewBookmarkUrlTextId.text.toString()
                                )
                            }

                    }
            }

        addBookmarkPreviewBinding
            .also { addBinding ->
                addBinding.mbBookmarkUpdateSearchNewButtonId
                    .setOnClickListener {
                        //set search mode
                        binding.mbAddBookmarkPreviewId.setStatusVisibility(SEARCH)
                        //hide keyboard
                        addBinding.mbNewBookmarkUrlEditTextId.hideKeyboard()
                        newBookmarkEditTitleViewBinding.mbNewBookmarkTitleEditTextId.hideKeyboard()
                        //search bookmark
                        searchBookmarkAction(addBinding.mbNewBookmarkUrlEditTextId.text.toString())
                        newBookmarkEditTitleViewBinding.mbNewBookmarkIconImageViewId.visibility =
                            VISIBLE
                    }
            }
        addBookmarkPreviewBinding
            .also { addBinding ->
                addBinding.mbNewBookmarkUrlEditTextId.addTextChangedListener(
                    object :
                        TextWatcher {
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {
                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        override fun afterTextChanged(text: Editable?) {
                            addBinding.mbNewBookmarkUrlTextInputLayoutId.error = null
                        }
                    })
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

                binding.mbNewBookmarkUrlTextId.visibility = VISIBLE
                binding.mbNewBookmarkUrlTextId.text = url
                newBookmarkEditTitleViewBinding.mbNewBookmarkTitleTextViewId.text = title
                newBookmarkEditTitleViewBinding.mbNewBookmarkTitleEditTextId.setText(title)
                iconUrl
                    ?.also {
                        newBookmarkEditTitleViewBinding.mbNewBookmarkTitleEditTextId.tag = it
                        addBookmarkViewModel.bookmarkIconUrl.set(it)
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
                    binding.mbBookmarkSaveNewButtonId.visibility = VISIBLE
                    binding.mbNewBookmarkAddEditButtonId.visibility = VISIBLE
                }

                binding.mbNewBookmarkUrlTextId.text = url
                newBookmarkEditTitleViewBinding.mbNewBookmarkTitleTextViewId.text =
                    "Balallllllallalal"
                newBookmarkEditTitleViewBinding.mbNewBookmarkTitleTextViewId.visibility = VISIBLE
                addBookmarkViewModel.updateWebviewByUrl(url)
                addBookmarkViewModel.findBookmarkInfoByUrl(url)
                addBookmarkViewModel.bookmarkInfoLiveData.observe(viewLifecycleOwner) {
                    newBookmarkEditTitleViewBinding.mbNewBookmarkTitleTextViewId.text = it.sitename
                    newBookmarkEditTitleViewBinding.mbNewBookmarkTitleEditTextId.setText(it.sitename)
                }
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
//    fun onButtonPressed(uri: Uri) {
//        listener?.onFragmentInteraction(uri)
//    }

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

    companion object {
        const val SAVE_ACTION_BOOKMARK = "SAVE_ACTION_BOOKMARK"
        const val UPDATE_ACTION_BOOKMARK = "UPDATE_ACTION_BOOKMARK"
    }
}
