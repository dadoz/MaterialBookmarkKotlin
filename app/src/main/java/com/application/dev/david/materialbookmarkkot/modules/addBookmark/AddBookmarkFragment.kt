package com.application.dev.david.materialbookmarkkot.modules.addBookmark

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.application.dev.david.materialbookmarkkot.OnFragmentInteractionListener
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.ui.changeToolbarFont
import com.application.dev.david.materialbookmarkkot.ui.hideKeyboard
import com.application.dev.david.materialbookmarkkot.viewModels.AddBookmarkViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.add_bookmark_error_view.*
import kotlinx.android.synthetic.main.add_bookmark_success_view.*
import kotlinx.android.synthetic.main.bookmark_title_icon_layout_view.*
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

    /**
     * init actionbar
     */
    private fun initActionBar() {
        (activity as AppCompatActivity).setSupportActionBar(mbToolbarId)
        mbToolbarId.changeToolbarFont()
        mbToolbarId.title = ""
        mbToolbarId.visibility = VISIBLE
    }

    /**
     *
     */
    private fun initView() {
        mbNewBookmarkUrlCardviewId.setOnClickListener {
            mbNewBookmarkUrlCardviewId.visibility = GONE
            mbNewBookmarkUrlEditCardviewId.visibility = VISIBLE
        }

        // loader cbs
        addBookmarkViewModel.loaderLiveStatus.observe(this, Observer { isVisible ->
            when (isVisible) {
                true -> {
                    mbNewBookmarkTitleLoaderId.visibility = VISIBLE
                    mbNewBookmarkTitleTextInputId.visibility = GONE
                }
                false -> {
                    mbNewBookmarkTitleLoaderId.visibility = GONE
                    mbNewBookmarkTitleTextInputId.visibility = VISIBLE
                }
            }
        })
        addBookmarkViewModel.bookmarkSearchedUrlLiveData.observe(this, Observer{ searchedUrl ->
            mbBookmarkSearchedUrlWebViewId.loadUrl(searchedUrl)
            Log.e(javaClass.name, "blalllala " + searchedUrl)
        })

        //set placeholder
        mbNewBookmarkIconImageViewId.setImageDrawable(getPlaceholder())

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
            addBookmarkViewModel.saveBookmark(
                mbNewBookmarkTitleEditTextId.text.toString(),
                mbNewBookmarkTitleEditTextId.tag.let { it?.toString() ?: "" },
                mbNewBookmarkUrlTextId.text.toString()
            )
        }


        mbsearchBookmarkButtonViewId.setOnClickListener {
            val url = (mbNewBookmarkUrlEditTextId as AppCompatEditText).text.toString()
            mbNewBookmarkUrlTextId.text = url
            addBookmarkViewModel.updateWebviewByUrl(url)
            addBookmarkViewModel.findBookmarkInfoByUrl(url)

            mbBookmarkSearchedUrlWebViewId.visibility = VISIBLE
            mbNewBookmarkIconLayoutId.visibility = VISIBLE
            mbNewBookmarkSearchIntroViewId.visibility = GONE
            mbNewBookmarkUrlCardviewId.visibility = VISIBLE
            mbNewBookmarkUrlEditCardviewId.visibility = GONE
        }


        (mbNewBookmarkUrlEditTextId as AppCompatEditText).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged (text: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(text: Editable?) {
                mbsearchBookmarkButtonViewId.visibility = if (text.isNullOrBlank()) GONE else VISIBLE
            }
        })
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