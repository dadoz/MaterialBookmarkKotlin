package com.application.dev.david.materialbookmarkkot.modules.bookmarkList.pager

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.application.dev.david.materialbookmarkkot.R
import kotlinx.android.synthetic.main.bookmark_list_layout_view.*
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.application.dev.david.materialbookmarkkot.application.BookmarkApplication
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.ListViewTypeEnum
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.ListViewTypeEnum.*
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.SortTypeListEnum.*
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.StarFilterTypeEnum.IS_DEFAULT_VIEW
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.StarFilterTypeEnum.IS_STAR_VIEW
import com.application.dev.david.materialbookmarkkot.modules.addBookmark.AddBookmarkFragment.Companion.UPDATE_ACTION_BOOKMARK
import com.application.dev.david.materialbookmarkkot.modules.bookmarkList.BookmarkListAdapter
import com.application.dev.david.materialbookmarkkot.modules.bookmarkList.BookmarkListFragmentDirections
import com.application.dev.david.materialbookmarkkot.ui.setIconDependingOnSortAscending
import com.application.dev.david.materialbookmarkkot.ui.setStarColor
import com.application.dev.david.materialbookmarkkot.ui.toggleVisibiltyWithView
import com.application.dev.david.materialbookmarkkot.ui.views.behaviors.addOnScrollListenerWithViews
import com.application.dev.david.materialbookmarkkot.ui.views.behaviors.setGridOrListLayout
import com.application.dev.david.materialbookmarkkot.viewModels.BookmarkViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_bookmark_list.*

class BookmarkListPageFragment: Fragment() {
    private var viewType: Int = 0
    private val bookmarkViewModel by lazy {
        ViewModelProviders.of(this).get(BookmarkViewModel::class.java)
    }

    private val bookmarkFilters by lazy {
            (activity?.application as BookmarkApplication).bookmarkFilters
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bookmark_list_layout_view, container, false)
    }

    /**
     * init view app
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewType = arguments?.getInt("MB_VIEWPAGER_TYPE")?: 0
        initView()
    }

    private fun initView() {
        when (viewType) {
            0 -> initViewDefault()
            else -> initViewStar()
        }
    }

    private fun initViewStar() {
        initViewDefault()
    }

    /**
     *
     */
    @SuppressLint("FragmentLiveDataObserve")
    private fun initViewDefault() {
        mbBookmarkHeaderTotBookmarkCardId.setStarColor(IS_DEFAULT_VIEW)

        bookmarkViewModel.retrieveBookmarkList(bookmarkFilter = bookmarkFilters)

        //event update list
        bookmarkViewModel.bookmarksRemovedBookmarkPairData.observe(this, Observer { pairData ->
            val positionList = pairData.first
            pairData.second?.let { list ->
                mbBookmarkRecyclerViewId.apply {
                    adapter?.let {
                        (it as BookmarkListAdapter).setItems(list)
                        it.notifyItemRemoved(positionList[0])
                        if (positionList[1] != -1)
                            it.notifyItemRemoved(positionList[1])
                        it.notifyItemRangeChanged(positionList[0], list.size - positionList[0])
                    }
                }
            }
        })

        //event retrieve list
        bookmarkViewModel.bookmarksLiveData.observe(this, Observer { list ->
            //set recyclerview
            mbBookmarkRecyclerViewId.apply {
                layoutManager = GridLayoutManager(context, 2)
                setGridOrListLayout(ListViewTypeEnum.values()[bookmarkFilters.listViewType])

                adapter = BookmarkListAdapter(list as MutableList<Any>,
                    bookmarkFilters,
                    { position, bookmark -> openPreviewView(position, bookmark) },
                    { position, bookmark ->
                        bookmark.isStar = !bookmark.isStar
                        //toggling status
                        bookmarkViewModel.setStarBookmark(bookmark)
                        adapter?.notifyDataSetChanged()
                        if (bookmarkFilters.starFilterType == IS_STAR_VIEW) {
                            adapter?.notifyItemRemoved(position)
                            bookmarkViewModel.retrieveBookmarkList(bookmarkFilter = bookmarkFilters)
                        }
                    }
                )
            }
        })

        bookmarkViewModel.bookmarkListSize.observe(this, Observer {
            mbBookmarkHeaderTotBookmarkLabelId.apply { text = it }
        })

        mbBookmarkHeaderSortFilterIconId.apply {
            setIconDependingOnSortAscending(bookmarkFilters.isSortAscending())
            setOnClickListener {
                bookmarkFilters.toggleSortAscending()
                bookmarkViewModel.sortBookmarkAscending(bookmarkFilters = bookmarkFilters)
                setIconDependingOnSortAscending(bookmarkFilters.isSortAscending())
            }
        }

        mbBookmarkHeaderSortFilterByTitleChipId.apply {
            visibility = bookmarkFilters.getVisibilityBySortType(IS_BY_TITLE)
            setOnClickListener {
                bookmarkFilters.setSortByTitle()
                bookmarkViewModel.sortBookmarkByTitle(bookmarkFilters = bookmarkFilters)
                it.toggleVisibiltyWithView(mbBookmarkHeaderSortFilterByDateChipId)
            }
        }

        mbBookmarkHeaderSortFilterByDateChipId.apply {
            visibility = bookmarkFilters.getVisibilityBySortType(IS_BY_DATE)
            setOnClickListener {
                bookmarkFilters.setSortByDate()
                bookmarkViewModel.sortBookmarkByDate(bookmarkFilters = bookmarkFilters)
                it.toggleVisibiltyWithView(mbBookmarkHeaderSortFilterByTitleChipId)
            }
        }

        mbBookmarkHeaderListFilterIconId.apply  {
            visibility = bookmarkFilters.getVisibilityByViewType(IS_LIST)
            setOnClickListener {
                bookmarkFilters.setListViewType()
                mbBookmarkRecyclerViewId.setGridOrListLayout(IS_LIST)
                it.toggleVisibiltyWithView(mbBookmarkHeaderCardFilterIconId)

                //TODO is really bad this updated
                bookmarkViewModel.bookmarksLiveData.value?.clear()
                bookmarkViewModel.retrieveBookmarkList(bookmarkFilter = bookmarkFilters)
            }

        }

        mbBookmarkHeaderCardFilterIconId.apply {
            visibility = bookmarkFilters.getVisibilityByViewType(IS_GRID)
            setOnClickListener {
                bookmarkFilters.setGridViewType()
                mbBookmarkRecyclerViewId.setGridOrListLayout(IS_GRID)
                it.toggleVisibiltyWithView(mbBookmarkHeaderListFilterIconId)
                mbBookmarkRecyclerViewId.adapter?.notifyDataSetChanged()

                //TODO is really bad this updated
                bookmarkViewModel.bookmarksLiveData.value?.clear()
                bookmarkViewModel.retrieveBookmarkList(bookmarkFilter = bookmarkFilters)
            }
        }

        mbBookmarkRecyclerViewId.addOnScrollListenerWithViews(
            views = listOf(
                mbBookmarkAppBarLayoutId, mbBookmarkHeaderTitleTextViewId,
                mbBookmarkHeaderTitleLabelTextViewId, mbBookmarkHeaderTotBookmarkCardId
            )
        )

        //handle empty view
        val owner = this
        mbBookmarkEmptyViewId.apply {
            setViewModel(bookmarkViewModel)
            init(owner = owner, bookmarkFilter = bookmarkFilters,
            views = listOf(mbBookmarkRecyclerViewId, mbBookmarkHeaderTotBookmarkCardId, mbBookmarkMainBackgroundImageId))
        }
    }


    /**
     *
     */
    private fun openPreviewView(position: Int, bookmark: Bookmark) =
        mbBookmarkPreviewCardviewId.apply {
            setStarColor(bookmarkFilters.starFilterType)
            initView(BottomSheetBehavior.from(mbBookmarkPreviewCardviewId))
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


}