package com.application.material.bookmarkswallet.app.features.bookmarkList

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.application.material.bookmarkswallet.app.features.bookmarkList.configurator.filterDefaultListType
import com.application.material.bookmarkswallet.app.features.bookmarkList.configurator.filterHpList
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.Bookmark
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkListType
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkListType.GRID
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkListType.LIST
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterHp
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.User
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.getBookmarkId
import com.application.material.bookmarkswallet.app.features.bookmarkList.viewmodels.BookmarkViewModel
import com.application.material.bookmarkswallet.app.features.searchBookmark.SearchAndAddBookmarkView
import com.application.material.bookmarkswallet.app.features.searchBookmark.components.MbAddBookmarkModalBottomSheetView
import com.application.material.bookmarkswallet.app.features.searchBookmark.model.SearchResultUIState
import com.application.material.bookmarkswallet.app.features.searchBookmark.viewmodels.SearchBookmarkViewModel
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.components.MbExtendedFab
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor2
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleHExtraBigBoldYellowTextStyle
import com.application.material.bookmarkswallet.app.utils.BOOKMARK_COLUMN_GRID_SIZE
import com.application.material.bookmarkswallet.app.utils.BOOKMARK_COLUMN_LIST_SIZE
import com.application.material.bookmarkswallet.app.utils.EMPTY
import kotlinx.coroutines.launch
import java.util.Date


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BookmarkListComponentView(
    modifier: Modifier = Modifier,
    bookmarkViewModel: BookmarkViewModel? = hiltViewModel(), //nullable only for preview
    searchBookmarkViewModel: SearchBookmarkViewModel? = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    //local uri handler
    val localUriHandler = LocalUriHandler.current
    //bottom sheet state
    val bottomSheetVisible = remember { mutableStateOf(value = false) }
    val isPreviewModalBottomSheetVisible = remember { mutableStateOf(value = false) }
    //selected bookmark ui state
    val selectedBookmark = remember { mutableStateOf<Bookmark?>(null) }
    //user state
    val user by remember {
        mutableStateOf(
            value = USER_MOCK
        )
    }

    //bookmark list state
    val bookmarkListState = bookmarkViewModel?.bookmarkListUIState?.collectAsState()
    val bookmarkDeletionState = bookmarkViewModel?.bookmarkDeletionState?.collectAsState()
    //search ui state
    val searchResultUIState = searchBookmarkViewModel?.searchResultUIState?.collectAsState()
        ?: remember { mutableStateOf(SearchResultUIState()) } //todo useless only for preview working

    //filter list on hp
    val selectedFilterHpMap = remember { //todo rememberSaveable custom saver for map :(
        mutableStateMapOf(
            FilterHp.PINNED to false,
            FilterHp.SORT_BY_DATE to false
        )
    }
    //filter on list type
    val selectedFilterListType = rememberSaveable {
        mutableStateOf(
            value = filterDefaultListType
        )
    }

    //bookmark list empty check
    var isBookmarkListEmpty by rememberSaveable {
        mutableStateOf(
            value = false
        )
    }

    LaunchedEffect(key1 = bookmarkListState?.value?.itemList) {
        isBookmarkListEmpty = bookmarkListState?.value?.itemList.isNullOrEmpty()
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
    LaunchedEffect(key1 = selectedFilterHpMap.values.toList()) {
        coroutineScope.launch {
            //update bookmark list
            bookmarkViewModel?.updateListByFilter(
                filterHpMap = selectedFilterHpMap
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
                .padding(
                    bottom = Dimen.paddingMedium16dp
                )
        ) {
            //items on title and subtitle
            MbHeaderBookmarkList(
                modifier = Modifier
            )

            //filter configuration in HP
            MbFilterBookmarkHpView(
                modifier = Modifier,
                selectedFilterHpMap = selectedFilterHpMap,
                selectedFilterListType = selectedFilterListType
            )

            MbEmptyBookmarkListView(
                modifier = Modifier
                    .padding(
                        vertical = Dimen.paddingMedium16dp
                    ),
                isVisible = isBookmarkListEmpty
            )

            //main container view of all bookmarks
            BookmarkListComponentView(
                modifier = Modifier
                    .padding(
                        top = Dimen.paddingMedium16dp
                    )
                    .fillMaxSize(),
                bookmarkListType = selectedFilterListType.value.first(),
                bookmarkItems = bookmarkListState?.value?.itemList ?: listOf(),
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
                            bookmarkViewModel?.deleteBookmark(it)
                        },
                        onOpenAction = {
                            localUriHandler.openUri(it)
                        },
                        bottomSheetVisible = isPreviewModalBottomSheetVisible
                    )
                }
        }

        //fab button
        MbExtendedFab(
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
            title = stringResource(R.string.add_new_string),
            iconRes = android.R.drawable.ic_input_add,
            onClickAction = {
                bottomSheetVisible.value = true
            }
        )
    }

    //modal to show add new Bookmark
    MbAddBookmarkModalBottomSheetView(
        modifier = Modifier,
        bottomSheetVisible = bottomSheetVisible,
        onDismissCallback = {
            //clear state
            searchBookmarkViewModel?.clearSearchResultUIState()
        }
    ) {
        SearchAndAddBookmarkView(
            modifier = Modifier,
            onSearchBookmarkWithAIAction = {
                searchBookmarkViewModel?.searchUrlInfoByUrlGenAI(
                    url = it
                )
            },
            searchResultUIState = searchResultUIState.value
        )
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
fun MbHeaderBookmarkList(modifier: Modifier.Companion) {
    Row(
        modifier = modifier
            .padding(vertical = Dimen.paddingMedium24dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(end = Dimen.paddingMedium16dp),
            style = mbTitleHExtraBigBoldYellowTextStyle(),
            text = stringResource(R.string.bookmarks_title),
        )
    }
}

@Composable
fun MbFilterBookmarkHpView(
    modifier: Modifier,
    selectedFilterHpMap: MutableMap<FilterHp, Boolean>,
    selectedFilterListType: MutableState<List<BookmarkListType>>,
    isVisible: Boolean = true
) {
    AnimatedVisibility(
        modifier = modifier
            .wrapContentWidth(),
        visible = isVisible
    ) {

        //filers move in a component small maybe
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
        ) {
            val (filterHpRef, filterListTypeRef) = createRefs()
            //filter fot the bookmark
            BookmarkFilterView(
                modifier = Modifier
                    .constrainAs(ref = filterHpRef) {
                        top.linkTo(anchor = parent.top)
                        bottom.linkTo(anchor = parent.bottom)
                        start.linkTo(anchor = parent.start)
                        end.linkTo(
                            anchor = filterListTypeRef.start,
                            margin = Dimen.paddingMedium16dp
                        )
                        width = Dimension.fillToConstraints
                    },
                filterItems = filterHpList,
                onSelectedFilter = { selectedFilter, newValue ->
                    selectedFilterHpMap[selectedFilter] = newValue
                }
            )

            //list type
            BookmarkFilterView(
                modifier = Modifier
                    .constrainAs(ref = filterListTypeRef) {
                        top.linkTo(anchor = parent.top)
                        bottom.linkTo(anchor = parent.bottom)
                        end.linkTo(anchor = parent.end)
                    },
                isSelectedOverride = selectedFilterListType.value.first() == GRID, //list is only for generic type
                filterItems = selectedFilterListType.value,
                onSelectedFilter = { selectedFilter, newValue ->
                    selectedFilterListType.value = when {
                        selectedFilter == GRID -> listOf(LIST)
                        else -> listOf(GRID)
                    }
                }
            )
        }
    }
}

@Composable
fun BookmarkListComponentView(
    modifier: Modifier = Modifier,
    bookmarkListType: BookmarkListType = GRID,
    bookmarkItems: List<Bookmark> = emptyList(),
    onOpenAction: (Bookmark) -> Unit = {}
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(
            count = when (bookmarkListType) {
                GRID -> BOOKMARK_COLUMN_GRID_SIZE
                LIST -> BOOKMARK_COLUMN_LIST_SIZE
            }
        ),
        verticalArrangement = Arrangement.spacedBy(Dimen.paddingMedium16dp),
        horizontalArrangement = Arrangement.spacedBy(Dimen.paddingMedium16dp)
    ) {
        items(items = bookmarkItems) { item ->
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

internal val bookmarkListMock = listOf(
    Bookmark(
        appId = getBookmarkId("www.google.it"),
        siteName = EMPTY,
        title = "Google",
        iconUrl = "www.google.it",
        url = "www.google.it",
        timestamp = Date(),//Dates.today,
        isLike = false
    ),
    Bookmark(
        appId = getBookmarkId("www.google.it"),
        siteName = EMPTY,
        title = "Google",
        iconUrl = "www.google.it",
        url = "www.google.it",
        timestamp = Date(),//Dates.today,
        isLike = false
    ),
    Bookmark(
        appId = getBookmarkId("www.google.it"),
        siteName = EMPTY,
        title = "Google",
        iconUrl = "www.google.it",
        url = "www.google.it",
        timestamp = Date(),//Dates.today,
        isLike = false
    ),
    Bookmark(
        appId = getBookmarkId("www.google.it"),
        siteName = EMPTY,
        title = "Facebook",
        iconUrl = "www.facebook.it",
        url = "www.facebook.it",
        timestamp = Date(),//Dates.today,
        isLike = true
    ),
    Bookmark(
        appId = getBookmarkId("www.google.it"),
        siteName = EMPTY,
        title = "Google",
        iconUrl = "www.google.it",
        url = "www.google.it",
        timestamp = Date(),//Dates.today,
        isLike = false
    ),
    Bookmark(
        appId = getBookmarkId("www.google.it"),
        siteName = EMPTY,
        title = "Facebook",
        iconUrl = "www.facebook.it",
        url = "www.facebook.it",
        timestamp = Date(),//Dates.today,
        isLike = true
    ),
    Bookmark(
        appId = getBookmarkId("www.google.it"),
        siteName = EMPTY,
        title = "Google",
        iconUrl = "www.google.it",
        url = "www.google.it",
        timestamp = Date(),//Dates.today,
        isLike = false
    ),
    Bookmark(
        appId = getBookmarkId("www.google.it"),
        siteName = EMPTY,
        title = "Facebook",
        iconUrl = "www.facebook.it",
        url = "www.facebook.it",
        timestamp = Date(),//Dates.today,
        isLike = true
    )
)

val USER_MOCK = User(
    name = "Davide",
    surname = "bllalal",
    username = "blla",
//    profilePictureUrl = "https://images6.alphacoders.com/463/463807.jpg",
    age = 40
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
val BookmarkListButtonContainerHeight = ButtonDefaults.MediumContainerHeight