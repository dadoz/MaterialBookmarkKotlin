package com.application.material.bookmarkswallet.app.features.bookmarkList

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.BookmarkCardView
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.BookmarkFilterView
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.BookmarkModalPreviewCard
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.MBExtendedFab
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.User
import com.application.material.bookmarkswallet.app.features.bookmarkList.viewmodels.BookmarkViewModel
import com.application.material.bookmarkswallet.app.features.searchBookmark.SearchAndAddBookmarkView
import com.application.material.bookmarkswallet.app.features.searchBookmark.SearchResultUIState
import com.application.material.bookmarkswallet.app.features.searchBookmark.components.BookmarkAddModalBottomSheetView
import com.application.material.bookmarkswallet.app.features.searchBookmark.viewmodels.SearchBookmarkViewModel
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.getBookmarkId
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor2
import com.application.material.bookmarkswallet.app.ui.style.mbTitleHExtraBigBoldYellowTextStyle
import com.application.material.bookmarkswallet.app.utils.EMPTY
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BookmarkListView(
    modifier: Modifier = Modifier,
    bookmarkViewModel: BookmarkViewModel? = hiltViewModel(), //nullable only for preview
    searchBookmarkViewModel: SearchBookmarkViewModel? = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
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

    val localUriHandler = LocalUriHandler.current

    val searchResultUIState = searchBookmarkViewModel?.searchResultUIState?.collectAsState()
        ?: remember { mutableStateOf(SearchResultUIState()) } //todo useless only for preview working

    //init status
    LaunchedEffect(key1 = null) {
        coroutineScope.launch {
            bookmarkViewModel?.getBookmarkList()
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

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = Dimen.paddingMedium16dp)
        ) {
            //items on title and subtitle
            Row(
                modifier = Modifier
                    .padding(vertical = Dimen.paddingMedium24dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = Dimen.paddingMedium16dp)
                        .weight(
                            weight = 2f
                        ),
                    style = mbTitleHExtraBigBoldYellowTextStyle(),
                    text = stringResource(R.string.bookmarks_title),
                )
            }
            //filers
            BookmarkFilterView(
                modifier = Modifier,
                filterItems = filterList
            )

            BookmarkItemsView(
                modifier = Modifier
                    .fillMaxSize(),
                bookmarkItems = bookmarkListState?.value?.itemList ?: listOf(),
                onOpenAction = { bookmark ->
                    isPreviewModalBottomSheetVisible.value = true
                    selectedBookmark.value = bookmark
                }
            )

            selectedBookmark.value
                ?.let {
                    BookmarkModalPreviewCard(
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

            BookmarkAddModalBottomSheetView(
                modifier = Modifier,
                bottomSheetVisible = bottomSheetVisible
            ) {
                SearchAndAddBookmarkView(
                    modifier = Modifier,
                    onSearchBookmarkWithAIAction = {
                        searchBookmarkViewModel?.searchUrlInfoByUrlGenAI(
                            url = it
                        )
//                        //close dialog
//                        bottomSheetVisible.value = false
                    },
                    searchResultUIState = searchResultUIState.value
                )
            }
        }

        //fab button
        MBExtendedFab(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = Dimen.paddingLarge32dp)
                .padding(end = Dimen.paddingMedium16dp),
            title = stringResource(R.string.add_new_string),
            iconRes = R.drawable.ic_add,
            onClickAction = {
                bottomSheetVisible.value = true
            }
        )
    }
}


@Composable
fun BookmarkItemsView(
    modifier: Modifier = Modifier,
    bookmarkItems: List<Bookmark> = emptyList(),
    onOpenAction: (Bookmark) -> Unit = {}
) {
    LazyVerticalGrid(
        modifier = modifier
            .padding(
                top = Dimen.paddingMedium16dp,
                bottom = Dimen.paddingLarge32dp
            ),
        columns = GridCells.Fixed(COLUMN_GRID_SIZE),
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
    }
}


@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun BookmarkListViewPreview() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(mbGrayLightColor2())) {
            BookmarkListView(
                modifier = Modifier,
                searchBookmarkViewModel = null,
                bookmarkViewModel = null
            )
        }
    }
}


const val COLUMN_GRID_SIZE = 1
internal val filterList = listOf("list", "sort by date", "pinned")
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