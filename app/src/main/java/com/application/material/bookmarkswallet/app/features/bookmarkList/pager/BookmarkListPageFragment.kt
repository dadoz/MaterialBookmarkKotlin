package com.application.material.bookmarkswallet.app.features.bookmarkList.pager

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.application.BookmarkApplication
import com.application.material.bookmarkswallet.app.databinding.BookmarkListLayoutViewBinding
import com.application.material.bookmarkswallet.app.extensions.setIconDependingOnSortAscending
import com.application.material.bookmarkswallet.app.extensions.setStarColor
import com.application.material.bookmarkswallet.app.extensions.toggleVisibiltyWithView
import com.application.material.bookmarkswallet.app.features.bookmarkList.BookmarkListAdapter
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.ListViewTypeEnum
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.ListViewTypeEnum.IS_GRID
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.ListViewTypeEnum.IS_LIST
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortTypeListEnum.IS_BY_DATE
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.SortTypeListEnum.IS_BY_TITLE
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_DEFAULT_VIEW
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_STAR_VIEW
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbButtonTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleLightTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleBoldTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbWhiteLightBlackFilter
import com.application.material.bookmarkswallet.app.ui.views.behaviors.setGridOrListLayout
import com.application.material.bookmarkswallet.app.viewModels.BookmarkViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import java.util.Date

const val EMPTY_TITLE: String = "Bookmark"

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
                setGridOrListLayout(ListViewTypeEnum.entries[bookmarkFilters.listViewType])

                adapter = BookmarkListAdapter(
                    items = list,
                    bookmarkFilter = bookmarkFilters,
                    onBookmarkItemClicked = { position, bookmark ->
                        openPreviewView(
                            position,
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

    private fun openPreviewView(position: Int, bookmark: Bookmark) {
        //compose view
        binding.mbBookmarkPreviewComposeCard
            .apply {
                // is destroyed
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    // In Compose world
                    MaterialBookmarkMaterialTheme {
                        BookmarkPreviewCard(
                            modifier = Modifier,
                            bookmark = bookmark
                        )
                    }
                }
            }
        BottomSheetBehavior.from(binding.mbBookmarkPreviewComposeCard).state = STATE_EXPANDED
    }

    @Composable
    fun BookmarkPreviewCard(
        modifier: Modifier,
        bookmark: Bookmark
    ) {
        MbCardView(
            modifier = modifier
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(alignment = Alignment.CenterEnd),
                    horizontalArrangement = Arrangement.spacedBy(Dimen.paddingSmall8dp)
                ) {
                    listOf(
                        AppCompatResources.getDrawable(
                            LocalContext.current,
                            R.drawable.ic_share_light
                        ),
                        AppCompatResources.getDrawable(
                            LocalContext.current,
                            R.drawable.ic_star_light
                        ),
                        AppCompatResources.getDrawable(
                            LocalContext.current,
                            R.drawable.ic_pen_field
                        )
                    ).onEach {
                        Image(
                            modifier = Modifier
                                .width(Dimen.sizeLarge32dp)
                                .height(Dimen.sizeLarge32dp),
                            painter = rememberDrawablePainter(drawable = it),
                            contentDescription = ""
                        )
                    }
                }
            }

            AppCompatResources.getDrawable(
                LocalContext.current,
                R.drawable.ic_bookmark_light
            ).also {
                Image(
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(Dimen.paddingMedium16dp)
                        .width(Dimen.sizeExtraLarge64dp)
                        .height(Dimen.sizeExtraLarge64dp),
                    colorFilter = mbWhiteLightBlackFilter(),
                    painter = rememberDrawablePainter(drawable = it),
                    contentDescription = ""
                )
            }

            Text(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(bottom = Dimen.paddingExtraSmall4dp),
                style = mbTitleBoldTextStyle(),
                text = bookmark.title ?: EMPTY_TITLE
            )
            Text(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(bottom = Dimen.paddingExtraSmall4dp),
                style = mbSubtitleTextStyle(),
                text = bookmark.url
            )
            Text(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(bottom = Dimen.paddingMedium16dp),
                style = mbSubtitleLightTextStyle(),
                text = bookmark.timestamp.toString()
            )

            ExtendedFloatingActionButton(
                modifier = Modifier
                    .align(alignment = Alignment.End),
                onClick = { }) {
                Text(
                    modifier = Modifier,
                    style = mbButtonTextStyle(),
                    text = "Open"
                )
            }
        }
    }

    @Composable
    @Preview
    @Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
    fun BookmarkPreviewCardPreview() {
        BookmarkPreviewCard(
            modifier = Modifier,
            bookmark = Bookmark(
                title = "This is a title",
                siteName = "Blalallallalala",
                timestamp = Date(),
                image = "",
                url = "www.google.it",
                appId = null,
                isLike = false
            )
        )
    }
}