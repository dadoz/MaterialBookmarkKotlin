package com.application.material.bookmarkswallet.app.features.searchBookmark

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.databinding.FragmentSearchBookmarkComposeBinding
import com.application.material.bookmarkswallet.app.features.addBookmark.AddBookmarkFragment
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.BookmarkPreviewCard
import com.application.material.bookmarkswallet.app.features.searchBookmark.components.BookmarkModalBottomSheetView
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.ui.*
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.application.material.bookmarkswallet.app.ui.style.expandedBottomSheetState
import com.application.material.bookmarkswallet.app.ui.style.mbButtonTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor2
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleBoldTextStyle
import com.application.material.bookmarkswallet.app.utils.EMPTY
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AddBookmarkFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AddBookmarkFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
@AndroidEntryPoint
class SearchBookmarkFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentSearchBookmarkComposeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSearchBookmarkComposeBinding.inflate(inflater, container, false)
        .also {
            binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
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
                            onDeleteAction = null,
                            onOpenAction = null,
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

    private fun initView() {
        //compose view
        binding.mbNewBookmarkUrlComposeView
            .apply {
                // is destroyed
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                //set content
                setContent {
                    // In Compose world
                    MaterialBookmarkMaterialTheme {
                        SearchBookmarkView(modifier = Modifier)
                    }
                }
            }
    }

    @Composable
    fun SearchBookmarkView(modifier: Modifier) {
        Column(
            modifier = modifier
                .padding(Dimen.sizeMedium16dp)
        ) {
            Text(
                modifier = Modifier,
                style = mbTitleBoldTextStyle(),
                text = stringResource(id = R.string.search_nbookmark)
            )
            Text(
                modifier = Modifier,
                style = mbSubtitleTextStyle(),
                text = stringResource(id = R.string.search_your_bookmark)
            )
            MbCardView(
                modifier = Modifier
                    .padding(vertical = Dimen.paddingMedium16dp)
            ) {

                //search text field
                MbBookmarkTextFieldView(
                    modifier = Modifier
                )

                //clipboard
                Text(
                    modifier = Modifier
                        .padding(vertical = Dimen.paddingMedium16dp),
                    style = mbSubtitleTextStyle(),
                    text = stringResource(id = R.string.paste_clipboard)
                )

                //open cta
                ExtendedFloatingActionButton(
                    modifier = Modifier
                        .padding(bottom = Dimen.paddingMedium16dp),
                    containerColor = MbColor.Yellow,
                    text = {
                        Text(
                            modifier = Modifier,
                            style = mbButtonTextStyle(),
                            text = stringResource(id = R.string.search_nbookmark)
                        )
                    },
                    icon = {
                        Icon(
                            modifier = Modifier
                                .width(Dimen.sizeMedium16dp)
                                .height(Dimen.sizeMedium16dp),
                            painter = painterResource(R.drawable.ic_star),
                            contentDescription = EMPTY
                        )
                    },
                    onClick = {
//                        localUriHandler.openUri("$HTTPS_SCHEMA${bookmark.url}")
                    }
                )
            }

            val searchedBookmark by remember {
                mutableStateOf(
                    Bookmark(
                        "Google",
                        "Google",
                        "www.google.it",
                        "",
                        "http://www.google.it",
                        Date(),
                        false
                    )
                )
            }
            BookmarkPreviewCard(
                modifier = Modifier,
                bookmark = searchedBookmark
            )
            Image(
                modifier = Modifier
                    .padding(vertical = Dimen.paddingMedium16dp)
                    .align(alignment = Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.ic_bear_illustration),
                contentDescription = EMPTY
            )
        }
    }

    @Composable
    fun MbBookmarkTextFieldView(modifier: Modifier) {
        var searchUrlText by remember { mutableStateOf(EMPTY) }
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth(),
            textStyle = mbSubtitleTextStyle(),
            value = searchUrlText,
            label = {
                Text(
                    modifier = Modifier,
                    style = mbSubtitleTextStyle(),
                    text = stringResource(id = R.string.bookmark_url)
                )
            },
            onValueChange = {
                searchUrlText = it
            })
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BookmarkPreview() {
        BookmarkModalBottomSheetView(
            modifier = Modifier,
            bottomSheetState = expandedBottomSheetState(),
            url = "https://www.ecosia.com",
            onCloseCallback = {

            }
        )
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

    @Preview
    @Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
    @Composable
    fun SearchBookmarkViewPreview() {
        MaterialBookmarkMaterialTheme {
            Box(modifier = Modifier.background(mbGrayLightColor2())) {

                SearchBookmarkView(modifier = Modifier)
            }
        }
    }
}
