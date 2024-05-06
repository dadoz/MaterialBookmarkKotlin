package com.application.material.bookmarkswallet.app.features.searchBookmark

import android.annotation.SuppressLint
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipDescription.MIMETYPE_TEXT_URILIST
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.application.material.bookmarkswallet.app.OnFragmentInteractionListener
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.databinding.FragmentSearchBookmarkBinding
import com.application.material.bookmarkswallet.app.extensions.changeToolbarFont
import com.application.material.bookmarkswallet.app.extensions.hideKeyboard
import com.application.material.bookmarkswallet.app.extensions.hideKeyboardIfNeeded
import com.application.material.bookmarkswallet.app.features.addBookmark.AddBookmarkFragment
import com.application.material.bookmarkswallet.app.features.addBookmark.AddBookmarkFragment.Companion.SAVE_ACTION_BOOKMARK
import com.application.material.bookmarkswallet.app.features.settings.SettingsActivity
import com.application.material.bookmarkswallet.app.ui.*
import com.application.material.bookmarkswallet.app.viewModels.SearchBookmarkViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AddBookmarkFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AddBookmarkFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SearchBookmarkFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentSearchBookmarkBinding
    private var listener: OnFragmentInteractionListener? = null
    val clipboard by lazy {
        activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
    private val searchBookmarkViewModel: SearchBookmarkViewModel by lazy {
        ViewModelProvider(this)[SearchBookmarkViewModel::class.java]
    }

    private val navigation: NavController? by lazy {
        view?.let { Navigation.findNavController(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSearchBookmarkBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
        initView()
        startActivity(
            Intent(
                context,
                SettingsActivity::class.java
            )
        )
    }

    /**
     * init actionbar
     */
    private fun initActionBar() {
        requireActivity()
            .apply {
                this.addMenuProvider(this@SearchBookmarkFragment, viewLifecycleOwner)
                binding.mbToolbarId.changeToolbarFont()

//        (activity as AppCompatActivity).setSupportActionBar(binding.mbToolbarId)
//        binding.mbToolbarId.changeToolbarFont()
//        binding.mbToolbarId.title = getString(R.string.search_actionbar_string)
//        binding.mbToolbarId.visibility = View.VISIBLE
//        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_search_bookmark, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> {
                navigation?.popBackStack()
                activity?.hideKeyboardIfNeeded()
            }

            R.id.menuSettingsActionId -> startActivity(
                Intent(
                    context,
                    SettingsActivity::class.java
                )
            )
        }
        return true
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun initView() {
        binding.mbNewBookmarkUrlTextInputLayoutId.requestFocus()

        binding.mbBookmarkSearchPasteClipboardButtonId.setOnClickListener {
            when {
                !clipboard.hasPrimaryClip() || clipboard.primaryClipDescription == null -> {
                    view?.let {
                        Snackbar.make(it, "Nothing pasted on clipboard", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }

                else -> {
                    clipboard.primaryClipDescription?.let {
                        val res: String = when {
                            it.hasMimeType(MIMETYPE_TEXT_PLAIN) -> clipboard.primaryClip?.getItemAt(
                                0
                            )?.text.toString()

                            it.hasMimeType(MIMETYPE_TEXT_URILIST) -> clipboard.primaryClip?.getItemAt(
                                0
                            )?.uri.toString()

                            else -> ""
                        }
                        binding.mbNewBookmarkUrlEditTextId.setText(res)
                    }
                }
            }
        }

        searchBookmarkViewModel.bookmarkInfoLiveData.observe(this, Observer {
            navigation?.navigate(R.id.addBookmarkFragment)
        })

        binding.mbsearchBookmarkButtonViewId.setOnClickListener {
            navigation?.navigate(
                R.id.searchBookmarkFragment, bundleOf(
                    "actionType" to SAVE_ACTION_BOOKMARK,
                    "bookmarkUrl" to binding.mbNewBookmarkUrlEditTextId.text.toString()
                )
            )

            Handler().postDelayed(
                {
                    binding.mbNewBookmarkUrlEditTextId.hideKeyboard()
                }, 200
            )
        }

        binding.mbNewBookmarkUrlEditTextId.apply {
            binding.mbsearchBookmarkButtonViewId.isEnabled = false
            binding.mbsearchBookmarkButtonViewId.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    binding.mbsearchBookmarkButtonViewId.isEnabled = !s.isNullOrEmpty()
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