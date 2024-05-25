package com.application.material.bookmarkswallet.app.features.bookmarkList.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.application.material.bookmarkswallet.app.application.BookmarkApplication
import com.application.material.bookmarkswallet.app.databinding.BookmarkListLayoutViewBinding
import com.application.material.bookmarkswallet.app.extensions.setIconDependingOnSortAscending
import com.application.material.bookmarkswallet.app.extensions.setStarColor
import com.application.material.bookmarkswallet.app.extensions.toggleVisibiltyWithView
import com.application.material.bookmarkswallet.app.features.bookmarkList.adapter.BookmarkListAdapter
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.BookmarkPreviewCard
import com.application.material.bookmarkswallet.app.features.bookmarkList.viewmodels.BookmarkViewModel
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.ListViewTypeEnum
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.ListViewTypeEnum.IS_GRID
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.ListViewTypeEnum.IS_LIST
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortTypeListEnum.IS_BY_DATE
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortTypeListEnum.IS_BY_TITLE
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_DEFAULT_VIEW
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_STAR_VIEW
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.views.behaviors.setGridOrListLayout
import com.application.material.bookmarkswallet.app.utils.N_COUNT_GRID_BOOKMARKS
import com.application.material.bookmarkswallet.app.utils.ZERO
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_DRAGGING
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_SETTLING
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkListPageFragment(val bookmarkAddButtonVisibleCallback: (hasToShow: Boolean) -> Unit) :
    Fragment() {
    private lateinit var binding: BookmarkListLayoutViewBinding
    val bookmarkViewModel: BookmarkViewModel by viewModels()

    private var viewType: Int = ZERO

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
        viewType = arguments?.getInt("MB_VIEWPAGER_TYPE") ?: ZERO
        //init data
        loadData()
        //init view
        initView()
    }

    //todo move to vm
    fun loadData() {
        bookmarkViewModel.retrieveBookmarkList(bookmarkFilter = bookmarkFilters)
    }

    /**
     *
     */
    private fun initView() {
        binding.mbBookmarkHeaderTotBookmarkCardId.setStarColor(IS_DEFAULT_VIEW)

        //event update list
        bookmarkViewModel.bookmarksRemovedBookmarkPairData.observe(viewLifecycleOwner) { pairData ->
            val positionList = pairData.first
            pairData.second
                ?.let { list ->
                    (binding.mbBookmarkRecyclerViewId.adapter as? BookmarkListAdapter)
                        ?.apply {
                            setItems(list)
                            notifyItemRemoved(positionList[0])
                            if (positionList[1] != -1) {
                                notifyItemRemoved(positionList[1])
                            }
                            notifyItemRangeChanged(
                                positionList[0],
                                list.size - positionList[0]
                            )
                        }
                }
        }

        //event retrieve list
        bookmarkViewModel.bookmarksLiveData.observe(viewLifecycleOwner) { list ->
            //set recyclerview
            binding.mbBookmarkRecyclerViewId
                .apply {
                    //set layout manager
                    layoutManager = GridLayoutManager(context, N_COUNT_GRID_BOOKMARKS)

                    //set grid layout
                    setGridOrListLayout(listViewType = ListViewTypeEnum.entries[bookmarkFilters.listViewType])

                    //set adapter
                    adapter = BookmarkListAdapter(
                        items = list,
                        bookmarkFilter = bookmarkFilters,
                        onBookmarkItemClicked = { position, bookmark ->
                            openPreviewView(
                                bookmark
                            )
                        },
                        onBookmarkStarClicked = { position, bookmark ->
                            bookmark.isLike = !bookmark.isLike
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
        }

        bookmarkViewModel.bookmarkListSize.observe(viewLifecycleOwner) {
            binding.mbBookmarkHeaderTotBookmarkLabelId
                .apply { text = it }
        }

        binding.mbBookmarkHeaderSortFilterIconId
            .apply {
                setIconDependingOnSortAscending(bookmarkFilters.isSortAscending())
                setOnClickListener {
                    bookmarkFilters.toggleSortAscending()
                    bookmarkViewModel.sortBookmarkAscending(bookmarkFilters = bookmarkFilters)
                    setIconDependingOnSortAscending(bookmarkFilters.isSortAscending())
                }
            }

        binding.mbBookmarkHeaderSortFilterByTitleChipId
            .apply {
                visibility = bookmarkFilters.getVisibilityBySortType(IS_BY_TITLE)
                setOnClickListener {
                    bookmarkFilters.setSortByTitle()
                    bookmarkViewModel.sortBookmarkByTitle(bookmarkFilters = bookmarkFilters)
                    it.toggleVisibiltyWithView(binding.mbBookmarkHeaderSortFilterByDateChipId)
                }
            }

        binding.mbBookmarkHeaderSortFilterByDateChipId
            .apply {
                visibility = bookmarkFilters.getVisibilityBySortType(IS_BY_DATE)
                setOnClickListener {
                    bookmarkFilters.setSortByDate()
                    bookmarkViewModel.sortBookmarkByDate(bookmarkFilters = bookmarkFilters)
                    it.toggleVisibiltyWithView(binding.mbBookmarkHeaderSortFilterByTitleChipId)
                }
            }

        binding.mbBookmarkHeaderListFilterIconId
            .apply {
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

        binding.mbBookmarkHeaderCardFilterIconId
            .apply {
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
        binding.mbBookmarkEmptyViewId
            .also {
                it.setViewModel(bookmarkViewModel)
                it.init(
                    owner = viewLifecycleOwner,
                    bookmarkFilter = bookmarkFilters,
                    recyclerView = binding.mbBookmarkRecyclerViewId
                )
            }
    }

    private fun openPreviewView(bookmark: Bookmark) {
        //compose view
        binding.mbBookmarkPreviewComposeCard
            .apply {
                // is destroyed
                setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
                //set content
                setContent {
                    // In Compose world
                    MaterialBookmarkMaterialTheme {
                        BookmarkPreviewCard(
                            modifier = Modifier,
                            bookmark = bookmark
                        )
                    }
                }
                //set callbacks
                BottomSheetBehavior.from(this)
                    .apply {
                        //set state and add
                        this.state = STATE_EXPANDED
                        //set callback
                        this.addBottomSheetCallback(object : BottomSheetCallback() {
                            override fun onStateChanged(view: View, state: Int) {
                                val isVisible =
                                    state != STATE_DRAGGING && state != STATE_SETTLING && state != STATE_EXPANDED
                                bookmarkAddButtonVisibleCallback.invoke(isVisible)
                            }

                            override fun onSlide(p0: View, p1: Float) {
                            }
                        })

                    }
            }
    }
}
