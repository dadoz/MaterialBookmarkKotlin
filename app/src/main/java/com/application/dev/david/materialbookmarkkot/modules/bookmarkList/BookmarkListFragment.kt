package com.application.dev.david.materialbookmarkkot.modules.bookmarkList

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.application.dev.david.materialbookmarkkot.OnFragmentInteractionListener
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import com.application.dev.david.materialbookmarkkot.modules.addBookmark.AddBookmarkFragment.Companion.UPDATE_ACTION_BOOKMARK
import com.application.dev.david.materialbookmarkkot.modules.bookmarkList.BookmarkListAdapter.BookmarkViewItemType.BOOKMARK_HEADER_TYPE
import com.application.dev.david.materialbookmarkkot.modules.bookmarkList.BookmarkListFragment.BookmarkListViewTpeEnum.MB_CARD_VIEW_TYPE
import com.application.dev.david.materialbookmarkkot.modules.bookmarkList.BookmarkListFragment.BookmarkListViewTpeEnum.MB_LIST_VIEW_TYPE
import com.application.dev.david.materialbookmarkkot.preferences.booleanPreference
import com.application.dev.david.materialbookmarkkot.viewModels.BookmarkViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.library.davidelmn.materailbookmarksearchviewkt.MaterialSearchView
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.fragment_bookmark_list.*
import okhttp3.internal.notifyAll


class BookmarkListFragment : Fragment()  {
    private var listener: OnFragmentInteractionListener? = null
    var isBookmarkCardViewType: Boolean by booleanPreference("MB_IS_CARD_VIEw_TYPE", true)
    enum class BookmarkListViewTpeEnum { MB_LIST_VIEW_TYPE, MB_CARD_VIEW_TYPE }

    private val bookmarkViewModel by lazy {
        ViewModelProviders.of(this).get(BookmarkViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookmark_list, container, false)
    }

    /**
     * init view app
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInitSearchView()
        onInitAppBarMenu()
        initView()
    }

    /**
     *
     */
    private fun onInitSearchView() {
        mbMaterialSearchVIewId.addListener(MaterialSearchView.SearchViewSearchListener {
            Log.e("tag", it)
            bookmarkViewModel.searchBookmarkByTitle(it)
        })

    }
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
    }

    private fun onInitAppBarMenu() {
        mbBookmarkBottomBarLayoutId.apply {
            when (isBookmarkCardViewType) {
                true -> replaceMenu(R.menu.menu_bookmark_list)
                false -> replaceMenu(R.menu.menu_bookmark_list_with_cards)
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.mbBookmarkHeaderListFilterIconId ->  {
                        mbBookmarkRecyclerViewId.setGridOrListLayout(MB_LIST_VIEW_TYPE.ordinal, 1)
                        //update isBookmarkCardViewType
                        isBookmarkCardViewType = false
                        replaceMenu(R.menu.menu_bookmark_list_with_cards)
                    }
                    R.id.mbBookmarkHeaderCardFilterIconId -> {
                        mbBookmarkRecyclerViewId.setGridOrListLayout(MB_CARD_VIEW_TYPE.ordinal, 2)
                        //update isBookmarkCardViewType
                        isBookmarkCardViewType = true
                        replaceMenu(R.menu.menu_bookmark_list)
                    }
                    R.id.mbBookmarkHeaderStarFilterIconId -> {
                        val isStarFilterView = true
                        bookmarkViewModel.retrieveBookmarkList(isStarFilterView)
                    }
                    R.id.mbBookmarkHeaderHomeFilterIconId -> {
                        bookmarkViewModel.retrieveBookmarkList()
                    }
                }
                true
            }
        }
    }
    /**
     *
     */
    @SuppressLint("FragmentLiveDataObserve")
    private fun initView() {
        bookmarkViewModel.retrieveBookmarkList()

        //event update list
        bookmarkViewModel.bookmarksRemovedBookmarkPairData.observe(this, Observer { pairData ->
            val position = pairData.first
            pairData.second?.let { list ->
                mbBookmarkRecyclerViewId.apply {
                    adapter?.let {
                        (it as BookmarkListAdapter).setItems(list as MutableList<Any>)
                        it.notifyItemRemoved(position)
                        it.notifyItemRangeChanged(position, list.size - position)
                    }
                }
            }
        })

        //event retrieve list
        bookmarkViewModel.bookmarksLiveData.observe(this, Observer { list ->
            mbBookmarkRecyclerViewId.apply {
                layoutManager = GridLayoutManager(context, 2)
                when (isBookmarkCardViewType) {
                    true -> setGridOrListLayout(MB_CARD_VIEW_TYPE.ordinal, 2)
                    false -> setGridOrListLayout(MB_LIST_VIEW_TYPE.ordinal, 1)
                }

                adapter = BookmarkListAdapter(list as MutableList<Any>,
                    isBookmarkCardViewType,
                    { position, bookmark ->  openPreviewView(position, bookmark) },
                    { position, bookmark ->
                        bookmark.isStar = !bookmark.isStar //toggling status
                        bookmarkViewModel.setStarBookmark(bookmark)
                        adapter?.notifyDataSetChanged()
                    }
                )
            }

            //please replace with viewModel isEmptyDataList
            when (list.isNotEmpty()) {
                true -> {
                    mbBookmarkEmptyViewId.visibility =  GONE
                }
                false -> {
                    mbBookmarkEmptyViewId.visibility =  VISIBLE
                }
            }
        })

        bookmarkViewModel.bookmarkListSize.observe(this, Observer {
            //update label tot
            mbBookmarkHeaderTotBookmarkLabelId.apply {
                text = it
            }

        })


        mbBookmarkEmptyAddNewButtonId.setOnClickListener {
            findNavController().navigate(R.id.searchBookmarkFragment)
        }

        mbBookmarkAddNewButtonId.setOnClickListener {
            findNavController().navigate(R.id.searchBookmarkFragment)
        }

        mbBookmarkHeaderSortFilterIconId.setOnClickListener {
            Toast.makeText(context, "hey you clicked Bla", Toast.LENGTH_LONG).show()
        }
    }

    /**
     *
     */
    private fun openPreviewView(position: Int, bookmark: Bookmark) =
        mbBookmarkPreviewCardviewId.apply {
            initView(BottomSheetBehavior.from(mbBookmarkPreviewCardviewId), fab = mbBookmarkAddNewButtonId)
            initData(bookmark, bookmarkViewModel)
            setMoreButtonAction { }

            setDeleteButtonAction {
                bookmarkViewModel.deleteBookmark(bookmark)
                bookmarkViewModel.deleteBookmarkFromList(position)
            }

            actionEditBookmark {
                val action = BookmarkListFragmentDirections
                    .actionBookmarkListFragmentToAddBookmarkFragment(actionType = UPDATE_ACTION_BOOKMARK, bookmark =
                    bundleOf(
                        "bookmark_url" to bookmark.url,
                        "bookmark_title" to bookmark.title,
                        "bookmark_icon_url" to bookmark.image
                    ))
                findNavController().navigate(action)
            }

            actionShareBookmark { intent -> startActivity(intent)}

            actionOpenPreviewBookmark { intent -> startActivity(intent)}
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

/***
 * extension class :P
 */
fun RecyclerView.setGridOrListLayout(viewType: Int, newSpanCount: Int) {
    this.apply {
        (layoutManager as GridLayoutManager).apply {
            this.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when ((adapter as BookmarkListAdapter).getSpanSizeByPosition(position)) {
                        BOOKMARK_HEADER_TYPE.ordinal -> when(viewType)  {
                            0 -> 1
                            else -> 2
                        }
                        else -> 1
                    }
                }
            }
            //set span
            this.spanCount = newSpanCount
        }
        adapter?.notifyDataSetChanged()
    }
}