package com.application.dev.david.materialbookmarkkot.modules.addBookmark

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.View.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.application.dev.david.materialbookmarkkot.OnFragmentInteractionListener
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.databinding.FragmentAddBookmarkBinding
import com.application.dev.david.materialbookmarkkot.ui.MbAddBookmarkPreviewView.MbPreviewStatus.SEARCH
import com.application.dev.david.materialbookmarkkot.ui.MbAddBookmarkPreviewView.MbPreviewStatus.UPDATE
import com.application.dev.david.materialbookmarkkot.ui.changeToolbarFont
import com.application.dev.david.materialbookmarkkot.ui.hideKeyboard
import com.application.dev.david.materialbookmarkkot.viewModels.AddBookmarkViewModel
import kotlinx.android.synthetic.main.add_bookmark_preview_view.*
import kotlinx.android.synthetic.main.add_bookmark_preview_view.mbNewBookmarkUrlEditTextId
import kotlinx.android.synthetic.main.add_bookmark_preview_view.mbNewBookmarkUrlTextInputLayoutId
import kotlinx.android.synthetic.main.bookmark_title_icon_layout_view.*
import kotlinx.android.synthetic.main.fragment_add_bookmark.*
import kotlinx.android.synthetic.main.fragment_add_bookmark.mbToolbarId


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

    /**
     * init actionbar
     */
    private fun initActionBar() {
        (activity as AppCompatActivity).setSupportActionBar(mbToolbarId)
        mbToolbarId.changeToolbarFont()
        mbToolbarId.title = "Search"
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
        }

        // loader cbs
        addBookmarkViewModel.loaderLiveStatus.observe(this, Observer { isVisible ->
            when (isVisible) {
                true -> {
                    mbNewBookmarkTitleLoaderId.visibility = VISIBLE
                    mbNewBookmarkTitleTextInputId.visibility = INVISIBLE
                }
                false -> {
                    mbNewBookmarkTitleLoaderId.visibility = GONE
                    mbNewBookmarkTitleTextInputId.visibility = VISIBLE
                }
            }
        })
        addBookmarkViewModel.bookmarkSearchedUrlLiveData.observe(this, Observer{ searchedUrl ->
            mbBookmarkSearchedUrlWebViewId.visibility = VISIBLE
            mbBookmarkSearchedUrlWebViewId.loadUrl(searchedUrl)
        })

        //set placeholder
        mbNewBookmarkIconImageViewId.setImageDrawable(getPlaceholder())

        addBookmarkViewModel.bookmarkInfoLiveData.observe(this, Observer{ bookmarkInfo ->
            mbAddBookmarkPreviewId.setTitleAndIconImage(bookmarkInfo.meta.title, bookmarkInfo.meta.image)
            addBookmarkViewModel.bookmarkIconUrl.set(bookmarkInfo.meta.image)
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
    }

    private fun onUpdateBookmarkWithError() {
        //TODO implement
    }

    private fun onUpdateBookmarkWithSuccess() {
        navigation?.popBackStack()
    }

    /**
     * get placeholder
     */
    private fun getPlaceholder(): Drawable? {
        return context?.let {ctx ->
            ContextCompat.getDrawable(ctx, R.drawable.ic_bookmark)?.let {
                DrawableCompat.setTint(it, ContextCompat.getColor(ctx, R.color.colorPrimary))
                it
            }
        }
    }

    /**
     * update status
     */
    private fun updateBookmarkView(url: String?, title: String?, iconUrl: String?) {
        mbBookmarkUpdateNewButtonId.visibility = VISIBLE
        mbBookmarkSaveNewButtonId.visibility = GONE

        mbNewBookmarkUrlTextId.text = url
        mbNewBookmarkTitleEditTextId.setText(title)
        mbNewBookmarkTitleLoaderId.visibility = GONE
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
        mbBookmarkSaveNewButtonId.visibility = VISIBLE

        mbNewBookmarkUrlTextId.text = url
        addBookmarkViewModel.updateWebviewByUrl(url)
        addBookmarkViewModel.findBookmarkInfoByUrl(url)

        //set placeholder ????? TODO why this?????
        mbNewBookmarkIconImageViewId.setImageDrawable(getPlaceholder())
    }

    /**
     *
     */
    private fun onSaveWithSuccess() {
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
        public final const val SAVE_ACTION_BOOKMARK = "SAVE_ACTION_BOOKMARK"
        public final const val UPDATE_ACTION_BOOKMARK = "UPDATE_ACTION_BOOKMARK"
    }
}