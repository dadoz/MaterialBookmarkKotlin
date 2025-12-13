package com.application.material.bookmarkswallet.app.features.bookmarkList

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.BookmarkCardView
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.BookmarkFilterView
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.BookmarkModalPreviewCardView
import com.application.material.bookmarkswallet.app.features.bookmarkList.configurator.filterDefaultHpListType
import com.application.material.bookmarkswallet.app.features.bookmarkList.configurator.filterDefaultListType
import com.application.material.bookmarkswallet.app.features.bookmarkList.configurator.filterHpList
import com.application.material.bookmarkswallet.app.features.bookmarkList.extension.resetMapToDefault
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.Bookmark
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkListType
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkListType.GRID
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkListType.GROUP
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkListType.LIST
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterHp
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.getBookmarkId
import com.application.material.bookmarkswallet.app.features.bookmarkList.saveable.rememberSaveableList
import com.application.material.bookmarkswallet.app.features.bookmarkList.saveable.rememberSaveableMap
import com.application.material.bookmarkswallet.app.features.bookmarkList.state.BookmarkListUIState
import com.application.material.bookmarkswallet.app.features.bookmarkList.viewmodels.BookmarkViewModel
import com.application.material.bookmarkswallet.app.features.hp.SearchBarHeaderView
import com.application.material.bookmarkswallet.app.features.searchBookmark.EditBookmarkView
import com.application.material.bookmarkswallet.app.features.searchBookmark.SearchAndAddBookmarkView
import com.application.material.bookmarkswallet.app.features.searchBookmark.components.MbAddBookmarkModalBottomSheetView
import com.application.material.bookmarkswallet.app.features.searchBookmark.components.MbDeleteBookmarkModalBottomSheetView
import com.application.material.bookmarkswallet.app.features.searchBookmark.model.SearchResultUIState
import com.application.material.bookmarkswallet.app.features.searchBookmark.viewmodels.SearchBookmarkViewModel
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.components.MbFab
import com.application.material.bookmarkswallet.app.ui.components.MbPrimaryButton
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbAppBarContainerColor
import com.application.material.bookmarkswallet.app.ui.style.mbButtonRedVermillionColor
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor2
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleHExtraBigBoldYellowTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleMediumBoldYellowLightDarkTextStyle
import com.application.material.bookmarkswallet.app.utils.BOOKMARK_COLUMN_GRID_SIZE
import com.application.material.bookmarkswallet.app.utils.BOOKMARK_COLUMN_LIST_SIZE
import com.application.material.bookmarkswallet.app.utils.EMPTY
import com.application.material.bookmarkswallet.app.utils.ZERO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date


@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookmarkListComponentView(
    modifier: Modifier = Modifier,
    bookmarkViewModel: BookmarkViewModel? = hiltViewModel(), //nullable only for preview
    searchBookmarkViewModel: SearchBookmarkViewModel? = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    //grid state
    val bookmarkLazyGridState = rememberLazyGridState()
    //local uri handler
    val localUriHandler = LocalUriHandler.current
    //bottom sheet state
    val isSearchModalBottomSheetVisible = remember {
        mutableStateOf(value = false)
    }
    val isPreviewModalBottomSheetVisible = remember {
        mutableStateOf(value = false)
    }
    val isDeleteModalBottomSheetVisible = remember {
        mutableStateOf(value = false)
    }
    val isEditModalBottomSheetVisible = remember {
        mutableStateOf(value = false)
    }
    //selected bookmark ui state
    val selectedBookmark = remember {
        mutableStateOf<Bookmark?>(
            value = null
        )
    }

    //bookmark list state
    val bookmarkListState = bookmarkViewModel?.bookmarkListUIState?.collectAsState()
    val bookmarkDeletionState = bookmarkViewModel?.bookmarkDeletionState?.collectAsState()
    //search ui state
    val searchResultUIState = searchBookmarkViewModel?.searchResultUIState?.collectAsState()
        ?: remember { mutableStateOf(SearchResultUIState()) } //todo useless only for preview working

    //filter list on hp - FilterHp to Bool
    val selectedFilterHpMapState = remember {
        mutableStateOf(
            value = mapOf<FilterHp, Boolean>()
        )
    }

    //filter list on hp - FilterHp to Bool - removing save on store filtering
    val filterHpMapState = rememberSaveableMap(
        init = {
            //init value
            mutableStateOf(
                value = filterDefaultHpListType
            )
        },
        onStore = { }
    )

    //filter on list type
    val filterListTypeState = rememberSaveableList(
        init = {
            //init value
            mutableStateOf(
                value = filterDefaultListType
            )
                .also {
                    coroutineScope.launch {
                        it.value = bookmarkViewModel?.selectedFilterListTypeStored
                            ?.first()
                            ?: filterDefaultListType
                    }
                }
        },
        onStore = {
            bookmarkViewModel?.setSelectedFilterListType(
                value = it[ZERO]
            )
        }
    )

    //bookmark list empty check
    val isBookmarkListEmpty by rememberSaveable(
        saver = Saver(
            save = {
                it.value
            },
            restore = {
                mutableStateOf(
                    value = it
                )
            }
        )
    ) {
        derivedStateOf {
            bookmarkViewModel?.bookmarkListUIState?.value
                ?.itemList?.size
                ?.let { it > ZERO }
                ?: false
        }
    }

    //search state
    val textFieldState = rememberTextFieldState()
    //search state state
    val searchBarState = rememberSearchBarState()

    LaunchedEffect(key1 = searchBarState.currentValue) {
        bookmarkViewModel?.setFilterListBySearchState(
            searchBarState = searchBarState.currentValue
        )
    }

    LaunchedEffect(key1 = textFieldState.text) {
        coroutineScope.launch {
            textFieldState.text.toString()
                .let { query ->
                    bookmarkViewModel?.filterListBySearchQuery(
                        query = query
                    )
                }
        }
    }

    //init status
    LaunchedEffect(key1 = null) {
        coroutineScope.launch {
            bookmarkViewModel?.getBookmarkList()
        }
    }

    //delete status
    LaunchedEffect(key1 = bookmarkDeletionState?.value) {
        coroutineScope.launch {
            if (bookmarkDeletionState?.value == true) {
                //retrieve bookmarks
                bookmarkViewModel.getBookmarkList()
                //clear state
                bookmarkViewModel.clearDeleteStatus()
            }
        }
    }

    //update
    LaunchedEffect(key1 = searchResultUIState.value) {
        //only with success status
        if (searchResultUIState.value.bookmark != null) {
            coroutineScope.launch {
                //update bookmark list
                bookmarkViewModel?.getBookmarkList()
                //TODO clear state - move on closing modal please
//                searchBookmarkViewModel?.clearSearchResultUIState()
            }
        }
    }

    //filter update
    LaunchedEffect(key1 = filterHpMapState.value.toList()) {
        coroutineScope.launch {
            //update bookmark list
            bookmarkViewModel?.updateListByFilter(
                singleFilterHpMap = selectedFilterHpMapState.value
            )
        }
    }

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = Dimen.paddingMedium16dp
                )
        ) {
            SearchBarHeaderView(
                modifier = Modifier,
                textFieldState = textFieldState,
                searchBarState = searchBarState,
                appBarContainerColor = mbAppBarContainerColor(),
                onSearchCallback = {
                    bookmarkViewModel?.filterListBySearchQuery(
                        query = it
                    )
                },
                onClearCallback = {
                    bookmarkViewModel?.setFilterListBySearchState(
                        searchBarState = SearchBarValue.Collapsed
                    )
                }
            )

            //items on title and subtitle
            MbHeaderBookmarkList(
                modifier = Modifier,
                filterListTypeState = filterListTypeState
            )


            //filter configuration in HP
            MbFilterBookmarkHpView(
                modifier = Modifier,
                filterHpMapState = filterHpMapState,
                selectedFilterHpMapState = selectedFilterHpMapState
            )

            MbEmptyBookmarkListView(
                modifier = Modifier
                    .padding(
                        vertical = Dimen.paddingMedium16dp
                    ),
                isVisible = isBookmarkListEmpty
            )

            //main container view of all bookmarks
            BookmarkListInternalComponentView(
                modifier = Modifier
                    .padding(
                        top = Dimen.paddingMedium16dp
                    )
                    .fillMaxSize(),
                lazyGridState = bookmarkLazyGridState,
                bookmarkListType = filterListTypeState.value.first(),
                bookmarkListState = bookmarkListState?.value,
                onOpenAction = { bookmark ->
                    isPreviewModalBottomSheetVisible.value = true
                    selectedBookmark.value = bookmark
                }
            )

            //preview only if is selected
            selectedBookmark.value
                ?.let {
                    BookmarkModalPreviewCardView(
                        modifier = Modifier,
                        bookmark = it,
                        onDeleteCallback = {
                            isDeleteModalBottomSheetVisible.value = true
                        },
                        onOpenAction = {
                            localUriHandler.openUri(it)
                        },
                        onPinningAction = {
                            bookmarkViewModel?.updateBookmarkByPinning(
                                bookmark = it
                            )
                        },
                        onEditAction = {
                            isPreviewModalBottomSheetVisible.value = false
                            isEditModalBottomSheetVisible.value = true
                            searchBookmarkViewModel?.updateSearchUIStateInEditMode(
                                bookmark = it
                            )
                        },
                        bottomSheetVisible = isPreviewModalBottomSheetVisible
                    )
                }
        }

        //fab button
        MbFab(
            modifier = Modifier
                .align(
                    alignment = Alignment.BottomEnd
                )
                .padding(
                    bottom = Dimen.paddingMedium16dp
                )
                .padding(
                    end = Dimen.paddingMedium16dp
                ),
            iconRes = R.drawable.ic_add_dark,
            onClickAction = {
                isSearchModalBottomSheetVisible.value = true
            }
        )
    }

    //modal to show add new Bookmark
    MbAddBookmarkModalBottomSheetView(
        modifier = Modifier,
        bottomSheetVisible = isEditModalBottomSheetVisible,
        onDismissCallback = {
            //clear state
            searchBookmarkViewModel?.clearSearchResultUIState()
        }
    ) {
        EditBookmarkView(
            modifier = Modifier,
            onEditBookmarkAction = { bookmark ->
                bookmarkViewModel?.updateBookmark(
                    bookmark = bookmark
                )
            },
            searchResultUIState = searchResultUIState.value
        )
    }

    //modal to show add new Bookmark
    MbAddBookmarkModalBottomSheetView(
        modifier = Modifier,
        bottomSheetVisible = isSearchModalBottomSheetVisible,
        onDismissCallback = {
            //clear state
            searchBookmarkViewModel?.clearSearchResultUIState()
        }
    ) {
        SearchAndAddBookmarkView(
            modifier = Modifier,
            onSearchBookmarkWithAIAction = { url, title ->
                searchBookmarkViewModel?.searchUrlInfoByUrlGenAI(
                    url = url,
                    customTitle = title
                )
            },
            searchResultUIState = searchResultUIState.value
        )
    }

    //modal to show dlete new Bookmark
    MbDeleteBookmarkModalBottomSheetView(
        modifier = Modifier,
        bottomSheetVisible = isDeleteModalBottomSheetVisible,
        onDismissCallback = {
            //todo make som clean
        }
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = Dimen.paddingLarge32dp
                ),
            verticalArrangement = Arrangement.spacedBy(space = Dimen.paddingMedium16dp)
        ) {
            Text(
                modifier = Modifier,
                text = "Delete Bookmark",
                style = mbTitleMediumBoldYellowLightDarkTextStyle(),
            )
            Text(
                modifier = Modifier,
                text = "Hey are you sure to delete?",
                style = mbSubtitleTextStyle(),
            )
            MbPrimaryButton(
                modifier = Modifier
                    .padding(
                        top = Dimen.paddingMedium16dp
                    ),
                colors = mbButtonRedVermillionColor(),
                text = stringResource(id = R.string.delete_button_label),
                onClickAction = {
                    selectedBookmark.value
                        ?.let {
                            bookmarkViewModel?.deleteBookmark(
                                bookmark = it
                            )
                            //dismiss
                            isDeleteModalBottomSheetVisible.value = false
                            isPreviewModalBottomSheetVisible.value = false
                        }
                }
            )
        }
    }
}

@Composable
fun MbEmptyBookmarkListView(
    modifier: Modifier = Modifier,
    isVisible: Boolean
) {
    AnimatedVisibility(
        modifier = modifier
            .wrapContentWidth(),
        visible = isVisible
    ) {
        MbCardView(
            modifier = Modifier
                .width(
                    width = Dimen.size240dp
                )
                .heightIn(
                    min = Dimen.size180dp
                )
        ) {
            Column(
                modifier = Modifier
                    .heightIn(
                        min = Dimen.size180dp
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(
                            size = Dimen.size160dp
                        ),
                    painter = painterResource(id = R.drawable.ic_sleep_foz_illustration_200),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier
                        .padding(
                            top = Dimen.paddingMedium16dp
                        ),
                    textAlign = TextAlign.Center,
                    style = mbSubtitleTextStyle(),
                    text = stringResource(R.string.empty_bookmark_list_label),
                )
            }
        }
    }
}

@Composable
fun MbHeaderBookmarkList(
    modifier: Modifier = Modifier,
    filterListTypeState: MutableState<List<BookmarkListType>>
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = Dimen.paddingMedium16dp
            ),
    ) {
        val (titleRef, filterRef) = createRefs()
        Text(
            modifier = Modifier
                .constrainAs(ref = titleRef) {
                    top.linkTo(anchor = parent.top)
                    start.linkTo(anchor = parent.start)
                    end.linkTo(anchor = filterRef.start)
                    bottom.linkTo(anchor = parent.bottom)
                    width = Dimension.fillToConstraints

                }
                .padding(end = Dimen.paddingMedium16dp),
            style = mbTitleHExtraBigBoldYellowTextStyle(),
            text = stringResource(R.string.bookmarks_title),
        )

        //list type
        BookmarkFilterView(
            modifier = Modifier
                .constrainAs(ref = filterRef) {
                    top.linkTo(anchor = parent.top)
                    end.linkTo(anchor = parent.end)
                    bottom.linkTo(anchor = parent.bottom)
                },
            isSelectedOverride = filterListTypeState.value.first() == GRID, //list is only for generic type
            filterItems = filterListTypeState.value,
            onSelectedFilter = { selectedFilter, newValue ->
                filterListTypeState.value = when {
                    selectedFilter == GRID -> listOf(
                        LIST
                    )

                    else -> listOf(
                        GRID
                    )
                }
            }
        )
    }
}

@Composable
fun MbFilterBookmarkHpView(
    modifier: Modifier,
    selectedFilterHpMapState: MutableState<Map<FilterHp, Boolean>>,
    filterHpMapState: MutableState<Map<FilterHp, Boolean>>,
    isVisible: Boolean = true
) {
    AnimatedVisibility(
        modifier = modifier
            .wrapContentWidth(),
        visible = isVisible
    ) {
        //filter fot the bookmark
        BookmarkFilterView(
            modifier = Modifier,
            filterItems = filterHpList,
            initValues = filterHpMapState.value,
            onSelectedFilter = { selectedFilter, newValue ->
                selectedFilterHpMapState.value = mapOf(
                    selectedFilter to newValue
                )
                //update state since now is a mutablestate instead of a value
                filterHpMapState.value = filterHpMapState.value
                    .toMutableMap()
                    .resetMapToDefault()
                    .also {
                        it[selectedFilter] = newValue
                    }
            }
        )
    }
}

@Composable
fun BookmarkListInternalComponentView(
    modifier: Modifier = Modifier,
    bookmarkListType: BookmarkListType = GRID,
    bookmarkListState: BookmarkListUIState?,
    onOpenAction: (Bookmark) -> Unit = {},
    lazyGridState: LazyGridState,
) {
    LazyVerticalGrid(
        modifier = modifier,
        state = lazyGridState,
        columns = GridCells.Fixed(
            count = when (bookmarkListType) {
                GRID -> BOOKMARK_COLUMN_GRID_SIZE
                LIST -> BOOKMARK_COLUMN_LIST_SIZE
                GROUP -> BOOKMARK_COLUMN_LIST_SIZE
            }
        ),
        verticalArrangement = Arrangement.spacedBy(Dimen.paddingMedium16dp),
        horizontalArrangement = Arrangement.spacedBy(Dimen.paddingMedium16dp)
    ) {
        items(items = bookmarkListState?.itemList ?: listOf()) { item ->
            BookmarkCardView(
                modifier = Modifier
                    .animateItem(),
                bookmark = item,
                onOpenAction = onOpenAction
            )
        }
        //spacing last item
        item {
            Box(
                modifier = Modifier
                    .padding(
                        all = Dimen.paddingMedium16dp
                    )
            ) {}
        }
    }
}

@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun BookmarkListViewPreview() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(mbGrayLightColor2())) {
            BookmarkListComponentView(
                modifier = Modifier,
                searchBookmarkViewModel = null,
                bookmarkViewModel = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
val BookmarkListButtonContainerHeight = ButtonDefaults.MediumContainerHeight

internal val bookmarkListMock = listOf(
    Bookmark(
        appId = getBookmarkId("www.google.it"),
        siteName = EMPTY,
        title = "Google",
        iconUrl = "www.google.it",
        url = "www.google.it",
        timestamp = Date(),//Dates.today,
    ),
    Bookmark(
        appId = getBookmarkId("www.google.it"),
        siteName = EMPTY,
        title = "Google",
        iconUrl = "www.google.it",
        url = "www.google.it",
        timestamp = Date(),//Dates.today,
    ),
    Bookmark(
        appId = getBookmarkId("www.google.it"),
        siteName = EMPTY,
        title = "Google",
        iconUrl = "www.google.it",
        url = "www.google.it",
        timestamp = Date(),//Dates.today,
    ),
    Bookmark(
        appId = getBookmarkId("www.google.it"),
        siteName = EMPTY,
        title = "Facebook",
        iconUrl = "www.facebook.it",
        url = "www.facebook.it",
        timestamp = Date(),//Dates.today,
    ),
    Bookmark(
        appId = getBookmarkId("www.google.it"),
        siteName = EMPTY,
        title = "Google",
        iconUrl = "www.google.it",
        url = "www.google.it",
        timestamp = Date(),//Dates.today,
    ),
    Bookmark(
        appId = getBookmarkId("www.google.it"),
        siteName = EMPTY,
        title = "Facebook",
        iconUrl = "www.facebook.it",
        url = "www.facebook.it",
        timestamp = Date(),//Dates.today,
    ),
    Bookmark(
        appId = getBookmarkId("www.google.it"),
        siteName = EMPTY,
        title = "Google",
        iconUrl = "www.google.it",
        url = "www.google.it",
        timestamp = Date(),//Dates.today,
    ),
    Bookmark(
        appId = getBookmarkId("www.google.it"),
        siteName = EMPTY,
        title = "Facebook",
        iconUrl = "www.facebook.it",
        url = "www.facebook.it",
        timestamp = Date(),//Dates.today,
    )
)
