package com.application.dev.david.materialbookmarkkot.modules.searchBookmark

import android.annotation.SuppressLint
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipDescription.MIMETYPE_TEXT_URILIST
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.adapters.TextViewBindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.application.dev.david.materialbookmarkkot.OnFragmentInteractionListener
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.modules.addBookmark.AddBookmarkFragment
import com.application.dev.david.materialbookmarkkot.modules.addBookmark.AddBookmarkFragment.Companion.SAVE_ACTION_BOOKMARK
import com.application.dev.david.materialbookmarkkot.ui.SettingsActivity
import com.application.dev.david.materialbookmarkkot.ui.changeToolbarFont
import com.application.dev.david.materialbookmarkkot.ui.hideKeyboard
import com.application.dev.david.materialbookmarkkot.viewModels.SearchBookmarkViewModel
import com.google.android.material.snackbar.Snackbar
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
class SearchBookmarkFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    val clipboard by lazy {
        activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
    private val searchBookmarkViewModel: SearchBookmarkViewModel by lazy {
        ViewModelProviders.of(this).get(SearchBookmarkViewModel::class.java)
    }

    private val navigation: NavController? by lazy {
        view?.let { Navigation.findNavController(it) }
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
        return inflater.inflate(R.layout.fragment_search_bookmark, container, false)
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
        mbToolbarId.title = getString(R.string.search_actionbar_string)
        mbToolbarId.visibility = View.VISIBLE
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    @SuppressLint("FragmentLiveDataObserve")
    private fun initView() {
        mbBookmarkSearchPasteClipboardButtonId.setOnClickListener {
            when {
                !clipboard.hasPrimaryClip() || clipboard.primaryClipDescription == null -> {
                    view?.let {
                        Snackbar.make(it, "Nothing pasted on clipboard", Snackbar.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    clipboard.primaryClipDescription?.let {
                        val res: String = when  {
                            it.hasMimeType(MIMETYPE_TEXT_PLAIN) -> clipboard.primaryClip?.getItemAt(0)?.text.toString()
                            it.hasMimeType(MIMETYPE_TEXT_URILIST) -> clipboard.primaryClip?.getItemAt(0)?.uri.toString()
                            else -> ""
                        }
                        mbNewBookmarkUrlEditTextId.setText(res)
                    }
                }
            }
        }

        searchBookmarkViewModel.bookmarkInfoLiveData.observe(this, Observer{
            navigation?.navigate(R.id.addBookmarkFragment)
        })

        mbsearchBookmarkButtonViewId.setOnClickListener {
            mbNewBookmarkUrlEditTextId.hideKeyboard()
            Handler().postDelayed(
                {
                    val action = SearchBookmarkFragmentDirections
                        .actionSearchBookmarkFragmentToAddBookmarkFragment(
                            actionType = SAVE_ACTION_BOOKMARK, bookmarkUrl = mbNewBookmarkUrlEditTextId.text.toString())
                    navigation?.navigate(action)

                }, 200)
        }

        mbNewBookmarkUrlEditTextId.apply {
            mbsearchBookmarkButtonViewId.isEnabled = false
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    mbsearchBookmarkButtonViewId.isEnabled = !s.isNullOrEmpty()
                }
            })
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
}