package com.application.dev.david.materialbookmarkkot.modules.bookmarkList

import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.core.animation.doOnStart
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
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.Companion.GRID_SPAN_COUNT
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.Companion.LIST_SPAN_COUNT
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.ListViewTypeEnum
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.ListViewTypeEnum.IS_GRID
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.ListViewTypeEnum.IS_LIST
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.SortOrderListEnum.IS_ASCENDING
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.SortTypeListEnum.IS_BY_DATE
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.SortTypeListEnum.IS_BY_TITLE
import com.application.dev.david.materialbookmarkkot.modules.addBookmark.AddBookmarkFragment.Companion.UPDATE_ACTION_BOOKMARK
import com.application.dev.david.materialbookmarkkot.modules.bookmarkList.BookmarkListAdapter.BookmarkViewItemType.BOOKMARK_HEADER_TYPE
import com.application.dev.david.materialbookmarkkot.ui.views.behaviors.addOnScrollListenerWithViews
import com.application.dev.david.materialbookmarkkot.ui.views.behaviors.setGridOrListLayout
import com.application.dev.david.materialbookmarkkot.viewModels.BookmarkViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.library.davidelmn.materailbookmarksearchviewkt.MaterialSearchView
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.fragment_bookmark_list.*
import java.util.Arrays.asList


class BookmarkListFragment : Fragment()  {
    private var listener: OnFragmentInteractionListener? = null
    private val bookmarkFilters: BookmarkFilter by lazy {
        BookmarkFilter(IS_GRID, IS_ASCENDING, IS_BY_TITLE, context)
    }
    private val bookmarkViewModel by lazy {
        ViewModelProviders.of(this).get(BookmarkViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
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
            bookmarkViewModel.searchBookmarkByTitle(bookmarkFilter= bookmarkFilters, query= it)
        })

    }
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
    }

    /**
     *
     */
    private fun onInitAppBarMenu() {
        mbBookmarkBottomBarLayoutId.apply {
            when (bookmarkFilters.listViewType) {
                IS_GRID.ordinal -> replaceMenu(R.menu.menu_bookmark_list)
                IS_LIST.ordinal -> replaceMenu(R.menu.menu_bookmark_list_with_cards)
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.mbBookmarkHeaderStarFilterIconId -> {
                        bookmarkFilters.starFilterType = BookmarkFilter.StarFilterTypeEnum.IS_STAR_VIEW
                        bookmarkViewModel.retrieveBookmarkList(bookmarkFilter = bookmarkFilters)
                    }
                    R.id.mbBookmarkHeaderHomeFilterIconId -> {
                        bookmarkFilters.starFilterType = BookmarkFilter.StarFilterTypeEnum.IS_DEFAULT_VIEW
                        bookmarkViewModel.retrieveBookmarkList(bookmarkFilter = bookmarkFilters)
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
        bookmarkViewModel.retrieveBookmarkList(bookmarkFilter = bookmarkFilters)

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
                setGridOrListLayout(ListViewTypeEnum.values()[bookmarkFilters.listViewType])

                adapter = BookmarkListAdapter(list as MutableList<Any>,
                    bookmarkFilters,
                    { position, bookmark -> openPreviewView(position, bookmark) },
                    { _, bookmark ->
                        bookmark.isStar = !bookmark.isStar
                        //toggling status
                        bookmarkViewModel.setStarBookmark(bookmark)
                        adapter?.notifyDataSetChanged()
                    }
                )
            }

            //please replace with viewModel isEmptyDataList
            when (list.isNotEmpty()) {
                true -> mbBookmarkEmptyViewId.visibility = GONE
                false -> mbBookmarkEmptyViewId.visibility = VISIBLE
            }
        })

        bookmarkViewModel.bookmarkListSize.observe(this, Observer {
            mbBookmarkHeaderTotBookmarkLabelId.apply { text = it }
        })

        mbBookmarkEmptyAddNewButtonId.setOnClickListener {
            findNavController().navigate(R.id.searchBookmarkFragment)
        }

        mbBookmarkAddNewButtonId.setOnClickListener {
            findNavController().navigate(R.id.searchBookmarkFragment)
        }

        mbBookmarkHeaderSortFilterIconId.apply {
            setIconDependingOnSortAscending(bookmarkFilters.isSortAscending())
            setOnClickListener {
                bookmarkFilters.toggleSortAscending()
                bookmarkViewModel.sortBookmarkAscending(bookmarkFilters = bookmarkFilters)
                this.setIconDependingOnSortAscending(bookmarkFilters.isSortAscending())
            }
        }

        mbBookmarkHeaderSortFilterByTitleChipId.apply {
            visibility = bookmarkFilters.getVisibilityBySortType(IS_BY_TITLE)
            setOnClickListener {
                bookmarkFilters.setSortByTitle()
                bookmarkViewModel.sortBookmarkByTitle(bookmarkFilters = bookmarkFilters)
                it.togglieVisibiltyWithView(mbBookmarkHeaderSortFilterByDateChipId)
            }
        }

        mbBookmarkHeaderSortFilterByDateChipId.apply {
            visibility = bookmarkFilters.getVisibilityBySortType(IS_BY_DATE)
            setOnClickListener {
                bookmarkFilters.setSortByDate()
                bookmarkViewModel.sortBookmarkByDate(bookmarkFilters = bookmarkFilters)
                it.togglieVisibiltyWithView(mbBookmarkHeaderSortFilterByTitleChipId)
            }
        }

        mbBookmarkHeaderListFilterIconId.apply  {
            visibility = bookmarkFilters.getVisibilityByViewType(IS_LIST)
            setOnClickListener {
                bookmarkFilters.setListViewType()
                mbBookmarkRecyclerViewId.setGridOrListLayout(IS_LIST)
                it.togglieVisibiltyWithView(mbBookmarkHeaderCardFilterIconId)
            }

        }
        mbBookmarkHeaderCardFilterIconId.apply {
            visibility = bookmarkFilters.getVisibilityByViewType(IS_GRID)
            setOnClickListener {
                bookmarkFilters.setGridViewType()
                mbBookmarkRecyclerViewId.setGridOrListLayout(IS_GRID)
                it.togglieVisibiltyWithView(mbBookmarkHeaderListFilterIconId)
            }
        }



        mbBookmarkRecyclerViewId.addOnScrollListenerWithViews(
            views = listOf(
                mbBookmarkAppBarLayoutId, mbBookmarkHeaderTitleTextViewId,
                mbBookmarkHeaderTitleLabelTextViewId,
                mbBookmarkHeaderTotBookmarkCardId

            )
        )

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

fun ImageView.setIconDependingOnSortAscending(isSortAscending: Boolean) {
    when (isSortAscending) {
        true -> setImageResource(R.drawable.ic_reorder_up)
        false -> setImageResource(R.drawable.ic_reorder_down)
    }
}

fun View.togglieVisibiltyWithView(view: View) {
    visibility = GONE
    view.visibility = VISIBLE
}
