package com.application.material.bookmarkswallet.app.features.searchBookmark

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.application.material.bookmarkswallet.app.databinding.FragmentSearchBookmarkComposeBinding
import com.application.material.bookmarkswallet.app.features.addBookmark.AddBookmarkFragment
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.BookmarkPreviewCard
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.ui.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.findNavController

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
@AndroidEntryPoint
class SearchBookmarkFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentSearchBookmarkComposeBinding
    private val navigation: NavController? by lazy {
        view?.findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSearchBookmarkComposeBinding.inflate(inflater, container, false)
        .also {
            binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        TODO("Not yet implemented")
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        TODO("Not yet implemented")
    }

    private fun openPreviewView(position: Int, bookmark: Bookmark) {
        //compose view
        binding.mbNewBookmarkUrlComposeView
            .apply {
                // is destroyed
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                //set content
                setContent {
                    // In Compose world
                    MaterialBookmarkMaterialTheme {
                        BookmarkPreviewCard(
                            modifier = Modifier,
                            bookmark = bookmark,
                        )
                    }
                }
            }
            .also {
                Handler(Looper.getMainLooper()).postDelayed({
                    //set callbacks
                    BottomSheetBehavior.from(it)
                        .apply {
                            //set state and add
                            this.state = BottomSheetBehavior.STATE_EXPANDED
                            //set callback
//                            this.addBottomSheetCallback(openBottomSheetCallback)
                        }
                }, 100)
            }
    }
}

//    private val searchBookmarkViewModel: SearchBookmarkViewModel by viewModels()
//
//    //clipboard service
//    private val clipboard by lazy {
//        activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//    }
//
//    private val navigation: NavController? by lazy {
//        view?.let { Navigation.findNavController(it) }
//    }
//    /**
//     * init actionbar
//     */
//    private fun initActionBar() {
//        requireActivity()
//            .apply {
//                this.addMenuProvider(this@SearchBookmarkFragment, viewLifecycleOwner)
//                binding.mbToolbarId.changeToolbarFont()
//
////        (activity as AppCompatActivity).setSupportActionBar(binding.mbToolbarId)
////        binding.mbToolbarId.changeToolbarFont()
////        binding.mbToolbarId.title = getString(R.string.search_actionbar_string)
////        binding.mbToolbarId.visibility = View.VISIBLE
////        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
//            }
//    }
//
//    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//        menuInflater.inflate(R.menu.menu_search_bookmark, menu)
//    }
//
//    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//        when (menuItem.itemId) {
//            android.R.id.home -> {
//                navigation?.popBackStack()
//                activity?.hideKeyboardIfNeeded()
//            }
//
//            R.id.menuSettingsActionId -> startActivity(
//                Intent(
//                    context,
//                    SettingsActivity::class.java
//                )
//            )
//        }
//        return true
//    }
//
//    @SuppressLint("FragmentLiveDataObserve")
//    private fun initView() {
//        binding.mbNewBookmarkUrlTextInputLayoutId.requestFocus()
//
//        binding.mbBookmarkSearchPasteClipboardButtonId.setOnClickListener {
//            when {
//                !clipboard.hasPrimaryClip() || clipboard.primaryClipDescription == null -> {
//                    view?.let {
//                        Snackbar.make(it, "Nothing pasted on clipboard", Snackbar.LENGTH_SHORT)
//                            .show()
//                    }
//                }
//
//                else -> {
//                    clipboard.primaryClipDescription?.let {
//                        val res: String = when {
//                            it.hasMimeType(MIMETYPE_TEXT_PLAIN) -> clipboard.primaryClip?.getItemAt(
//                                0
//                            )?.text.toString()
//
//                            it.hasMimeType(MIMETYPE_TEXT_URILIST) -> clipboard.primaryClip?.getItemAt(
//                                0
//                            )?.uri.toString()
//
//                            else -> ""
//                        }
//                        binding.mbNewBookmarkUrlEditTextId.setText(res)
//                    }
//                }
//            }
//        }
//
//        searchBookmarkViewModel.bookmarkInfoLiveData.observe(viewLifecycleOwner) {
////            navigation?.navigate(R.id.addBookmarkFragment)
//        }
//
//        binding.mbsearchBookmarkButtonViewId.setOnClickListener {
//            binding.mbNewBookmarkUrlEditTextId.text
//                ?.let {
//                    //search and update view
//                    Toast.makeText(context, "hey searching $it", Toast.LENGTH_SHORT).show()
//                }
//            //hide keyboard
//            binding.mbNewBookmarkUrlEditTextId.hideKeyboard()
//        }
//
//        binding.mbNewBookmarkUrlEditTextId.apply {
//            binding.mbsearchBookmarkButtonViewId.isEnabled = false
//
//            binding.mbNewBookmarkUrlEditTextId.addTextChangedListener(textViewChangedCallback)
//        }
//
//        //set content modal view
////        setContentModalView()
//
////        openPreviewView(
////            0,
////            bookmark = Bookmark("balal", "bllala", "blaalla", "blalala", "blalblbal", Date())
////        )
//    }
//
//    private val textViewChangedCallback = object : TextWatcher {
//        override fun afterTextChanged(s: Editable?) {
//        }
//
//        override fun beforeTextChanged(
//            s: CharSequence?,
//            start: Int,
//            count: Int,
//            after: Int
//        ) {
//        }
//
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            binding.mbsearchBookmarkButtonViewId.isEnabled = !s.isNullOrEmpty()
//        }
//    }
