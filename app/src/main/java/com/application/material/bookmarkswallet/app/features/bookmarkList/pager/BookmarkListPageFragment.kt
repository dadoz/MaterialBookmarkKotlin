package com.application.material.bookmarkswallet.app.features.bookmarkList.pager

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.application.BookmarkApplication
import com.application.material.bookmarkswallet.app.databinding.BookmarkListLayoutViewBinding
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.ListViewTypeEnum
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.ListViewTypeEnum.IS_GRID
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.ListViewTypeEnum.IS_LIST
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortTypeListEnum.IS_BY_DATE
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortTypeListEnum.IS_BY_TITLE
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_DEFAULT_VIEW
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_STAR_VIEW
import com.application.material.bookmarkswallet.app.features.addBookmark.AddBookmarkFragment.Companion.UPDATE_ACTION_BOOKMARK
import com.application.material.bookmarkswallet.app.features.bookmarkList.BookmarkListAdapter
import com.application.material.bookmarkswallet.app.setIconDependingOnSortAscending
import com.application.material.bookmarkswallet.app.setStarColor
import com.application.material.bookmarkswallet.app.toggleVisibiltyWithView
import com.application.material.bookmarkswallet.app.ui.views.behaviors.setGridOrListLayout
import com.application.material.bookmarkswallet.app.viewModels.BookmarkViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BookmarkListPageFragment : Fragment() {
    private lateinit var binding: BookmarkListLayoutViewBinding

    private var viewType: Int = 0
    private val bookmarkViewModel by lazy {
        ViewModelProvider(this)[BookmarkViewModel::class.java]
    }

    private val bookmarkFilters by lazy {
        (activity?.application as BookmarkApplication).bookmarkFilters
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = BookmarkListLayoutViewBinding.inflate(inflater, container, false)
        .also {
            binding = it
        }.root

    /**
     * init view app
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewType = arguments?.getInt("MB_VIEWPAGER_TYPE") ?: 0
        initView()
    }

    fun initView() {
        when (viewType) {
            0 -> {
                bookmarkFilters.starFilterType = IS_DEFAULT_VIEW
                initViewDefault()
            }

            else -> {
                bookmarkFilters.starFilterType = IS_STAR_VIEW
                initViewStar()
            }
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
        binding.mbBookmarkHeaderTotBookmarkCardId.setStarColor(IS_DEFAULT_VIEW)

        bookmarkViewModel.retrieveBookmarkList(bookmarkFilter = bookmarkFilters)

        //event update list
        bookmarkViewModel.bookmarksRemovedBookmarkPairData.observe(this, Observer { pairData ->
            val positionList = pairData.first
            pairData.second?.let { list ->
                binding.mbBookmarkRecyclerViewId.apply {
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
            binding.mbBookmarkRecyclerViewId.apply {
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
            binding.mbBookmarkHeaderTotBookmarkLabelId.apply { text = it }
        })

        binding.mbBookmarkHeaderSortFilterIconId.apply {
            setIconDependingOnSortAscending(bookmarkFilters.isSortAscending())
            setOnClickListener {
                bookmarkFilters.toggleSortAscending()
                bookmarkViewModel.sortBookmarkAscending(bookmarkFilters = bookmarkFilters)
                setIconDependingOnSortAscending(bookmarkFilters.isSortAscending())
            }
        }

        binding.mbBookmarkHeaderSortFilterByTitleChipId.apply {
            visibility = bookmarkFilters.getVisibilityBySortType(IS_BY_TITLE)
            setOnClickListener {
                bookmarkFilters.setSortByTitle()
                bookmarkViewModel.sortBookmarkByTitle(bookmarkFilters = bookmarkFilters)
                it.toggleVisibiltyWithView(binding.mbBookmarkHeaderSortFilterByDateChipId)
            }
        }

        binding.mbBookmarkHeaderSortFilterByDateChipId.apply {
            visibility = bookmarkFilters.getVisibilityBySortType(IS_BY_DATE)
            setOnClickListener {
                bookmarkFilters.setSortByDate()
                bookmarkViewModel.sortBookmarkByDate(bookmarkFilters = bookmarkFilters)
                it.toggleVisibiltyWithView(binding.mbBookmarkHeaderSortFilterByTitleChipId)
            }
        }

        binding.mbBookmarkHeaderListFilterIconId.apply {
            visibility = bookmarkFilters.getVisibilityByViewType(IS_LIST)
            setOnClickListener {
                bookmarkFilters.setListViewType()
                binding.mbBookmarkRecyclerViewId.setGridOrListLayout(IS_LIST)
                it.toggleVisibiltyWithView(binding.mbBookmarkHeaderCardFilterIconId)

                //TODO is really bad this updated
                bookmarkViewModel.bookmarksLiveData.value?.toMutableList()?.clear()
                bookmarkViewModel.retrieveBookmarkList(bookmarkFilter = bookmarkFilters)
            }

        }

        binding.mbBookmarkHeaderCardFilterIconId.apply {
            visibility = bookmarkFilters.getVisibilityByViewType(IS_GRID)
            setOnClickListener {
                bookmarkFilters.setGridViewType()
                binding.mbBookmarkRecyclerViewId.setGridOrListLayout(IS_GRID)
                it.toggleVisibiltyWithView(binding.mbBookmarkHeaderListFilterIconId)
                binding.mbBookmarkRecyclerViewId.adapter?.notifyDataSetChanged()

                //TODO is really bad this updated
                bookmarkViewModel.bookmarksLiveData.value?.toMutableList()?.clear()
                bookmarkViewModel.retrieveBookmarkList(bookmarkFilter = bookmarkFilters)
            }
        }

//        mbBookmarkRecyclerViewId.addOnScrollListenerWithViews(
//            views = listOf(
//                mbBookmarkAppBarLayoutId, mbBookmarkHeaderTitleTextViewId,
//                mbBookmarkHeaderTitleLabelTextViewId, mbBookmarkHeaderTotBookmarkCardId
//            )
//        )

        //handle empty view
        val owner = this
        binding.mbBookmarkEmptyViewId
            .also {
                it.setViewModel(bookmarkViewModel)
                it.init(
                    owner = owner,
                    bookmarkFilter = bookmarkFilters,
                    recyclerView = binding.mbBookmarkRecyclerViewId
                )
            }
    }

    /**
     *
     */
    private fun openPreviewView(position: Int, bookmark: Bookmark) =
        binding.mbBookmarkPreviewCardviewId
            .also {
                it.setStarColor(bookmarkFilters.starFilterType)
                it.initView(BottomSheetBehavior.from(binding.mbBookmarkPreviewCardviewId))
                it.initData(bookmark, bookmarkViewModel)
                it.setMoreButtonAction { }

                it.setDeleteButtonAction {
                    bookmarkViewModel.deleteBookmark(bookmark)
                    bookmarkViewModel.deleteBookmarkFromList(position)
                }

                it.actionEditBookmark {
                    findNavController().navigate(
                        R.id.bookmarkListFragment, bundleOf(
                            "actionType" to UPDATE_ACTION_BOOKMARK,
                            "bookmark" to bundleOf(
                                "bookmark_url" to bookmark.url,
                                "bookmark_title" to bookmark.title,
                                "bookmark_icon_url" to bookmark.image
                            )
                        )
                    )
                }

                it.actionShareBookmark { intent -> startActivity(intent) }

                it.actionOpenPreviewBookmark { intent -> startActivity(intent) }
            }

}