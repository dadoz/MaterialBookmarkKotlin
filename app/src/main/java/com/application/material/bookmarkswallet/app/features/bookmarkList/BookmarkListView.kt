package com.application.material.bookmarkswallet.app.features.bookmarkList

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.BookmarkAddModalBottomSheetView
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.BookmarkCardView
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.BookmarkModalPreviewCard
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.MBExtendedFab
import com.application.material.bookmarkswallet.app.features.searchBookmark.MbBookmarkTextFieldView
import com.application.material.bookmarkswallet.app.features.searchBookmark.viewmodels.SearchBookmarkViewModel
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.getBookmarkId
import com.application.material.bookmarkswallet.app.network.models.Response
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.application.material.bookmarkswallet.app.ui.style.mbButtonTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor2
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleBoldTextStyle
import com.application.material.bookmarkswallet.app.utils.EMPTY
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date

const val COLUMN_GRID_SIZE = 1
internal val filterList = listOf<String>("list", "sort by date", "pinned")
internal val bookmarkList = listOf(
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
    ),

    )

@Composable
fun BookmarkListView(
    modifier: Modifier = Modifier,
    searchBookmarkViewModel: SearchBookmarkViewModel? = hiltViewModel<SearchBookmarkViewModel>(),
) {
    val bottomSheetVisible = remember { mutableStateOf(value = false) }
    val isPreviewModalBottomSheetVisible = remember { mutableStateOf(value = false) }
    val bookmarkItems = remember { mutableStateOf(value = emptyList<Bookmark>()) }
    val coroutineScope = rememberCoroutineScope()
    val selectedBookmark = remember { mutableStateOf<Bookmark?>(null) }

    LaunchedEffect(key1 = null) {
        //this is wrong move on VM TODO in right VM please
        coroutineScope
            .launch {
                searchBookmarkViewModel?.getBookmarksFlow()
                    ?.collect {
                        when (it) {
                            is Response.Success -> {
                                Timber.d("item ${it.data.joinToString()}")
                                bookmarkItems.value = it.data
                            }

                            is Response.Error -> {
                                Timber.d("ERROR - retrieve list ")
                            }
                        }
                    }
            }
    }

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(all = Dimen.paddingMedium16dp)
        ) {
            //items on title and subtitle
            Text(
                modifier = Modifier
                    .padding(bottom = Dimen.paddingExtraSmall4dp),
                style = mbTitleBoldTextStyle(),
                text = stringResource(R.string.bookmarks_title)
            )
//        Text(
//            modifier = Modifier
//                .padding(bottom = Dimen.paddingExtraSmall4dp),
//            style = mbSubtitleTextStyle(),
//            text = stringResource(R.string.bookmarks_description)
//        )

            //filers
            BookmarkFilterView(
                modifier = Modifier,
                filterItems = filterList
            )

            BookmarkItemsView(
                modifier = Modifier
                    .fillMaxSize(),
                bookmarkItems = bookmarkList,
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
                        onDeleteCallback = { },
                        onOpenAction = { },
                        bottomSheetVisible = isPreviewModalBottomSheetVisible
                    )
                }

            BookmarkAddModalBottomSheetView(
                modifier = Modifier,
                bottomSheetVisible = bottomSheetVisible
            ) {
                BookmarkAddModalInternalView(
                    modifier = Modifier
                )
            }
        }

        //fab button
        MBExtendedFab(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = Dimen.paddingMedium16dp)
                .padding(end = Dimen.paddingMedium16dp),
            title = "Add",
            iconRes = R.drawable.ic_add,
            onClickAction = {
                bottomSheetVisible.value = true
            }
        )
    }

}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BookmarkAddModalInternalView(
    modifier: Modifier = Modifier,
    searchBookmarkViewModel: SearchBookmarkViewModel? = hiltViewModel<SearchBookmarkViewModel>(),
) {
    val checked = remember { mutableStateOf(value = false) }
    val rotation by animateFloatAsState(
        targetValue = if (checked.value) 180f else 0f,
        label = "Trailing Icon Rotation"
    )
    val bookmarkUrl = remember { mutableStateOf(value = TextFieldValue()) }
    val bookmarkTitle = remember { mutableStateOf(value = TextFieldValue()) }
    val context = LocalContext.current
    Column(
        modifier = modifier
            .padding(horizontal = Dimen.paddingMedium16dp)
            .padding(bottom = Dimen.paddingMedium16dp),
    ) {
        Text(
            modifier = Modifier,
            style = mbTitleBoldTextStyle(),
            text = "New Search"
        )
        MbBookmarkTextFieldView(
            modifier = Modifier,
            textLabel = "Title",
            searchUrlTextState = bookmarkTitle
        )
        MbBookmarkTextFieldView(
            modifier = Modifier,
            textLabel = "Bookmark Url",
            searchUrlTextState = bookmarkUrl
        )
        Image(
            modifier = Modifier
                .padding(vertical = Dimen.paddingMedium16dp)
                .align(alignment = Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.ic_bear_illustration),
            contentDescription = EMPTY
        )

        ExtendedFloatingActionButton(
            onClick = {
                searchBookmarkViewModel?.searchUrlInfoByUrlGenAI(
                    url = bookmarkUrl.value.text,
                    onCompletion = {
                        Timber.d("stored on list")
                    }
                )
            },
            modifier = Modifier
                .padding(top = Dimen.paddingMedium16dp),
            content = {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Icon"
                )
                Text(
                    modifier = Modifier
                        .padding(start = Dimen.paddingSmall8dp),
                    text = "Search with Gemini",
                    style = mbButtonTextStyle()
                )
            }
        )
        ExtendedFloatingActionButton(
            onClick = {
                Toast.makeText(
                    context, R.string.search_nbookmark, Toast.LENGTH_LONG
                ).show()
            },
            modifier = Modifier
                .padding(top = Dimen.paddingMedium16dp),
            content = {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Icon"
                )
                Text(
                    modifier = Modifier
                        .padding(start = Dimen.paddingSmall8dp),
                    text = "Search your own",
                    style = mbButtonTextStyle()
                )
            }
        )

        SplitButtonLayout(
            modifier = Modifier,
            leadingButton = {
                SplitButtonDefaults
                    .ElevatedLeadingButton(
                        onClick = { /* Do Nothing */ },
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            modifier = Modifier.size(SplitButtonDefaults.LeadingIconSize),
                            contentDescription = "Localized description",
                        )
                        Text(
                            modifier = Modifier
                                .padding(start = 4.dp),
                            text = "Save or Gemini",
                            style = mbButtonTextStyle()
                        )
                    }
            },
            trailingButton = {
                SplitButtonDefaults
                    .ElevatedTrailingButton(
                        checked = checked.value,
                        onCheckedChange = { checked.value = it },
                        modifier =
                            Modifier
                                .semantics {
                                    stateDescription =
                                        if (checked.value) "Expanded" else "Collapsed"
                                    contentDescription = "Toggle Button"
                                },
                    ) {
                        Icon(
                            Icons.Filled.KeyboardArrowDown,
                            modifier = Modifier
                                .size(SplitButtonDefaults.TrailingIconSize)
                                .graphicsLayer {
                                    this.rotationZ = rotation
                                },
                            contentDescription = "Localized description"
                        )
                    }
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
            .padding(vertical = Dimen.paddingSmall8dp),
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
fun BookmarkFilterView(
    modifier: Modifier = Modifier,
    filterItems: List<String>
) {
    val selectedItem = remember { mutableIntStateOf(value = -1) }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(Dimen.paddingSmall8dp),
        modifier = modifier
    ) {
        itemsIndexed(
            items = filterItems
        ) { id, item ->
            FilterChip(
                onClick = {
                    selectedItem.intValue = id
                },
                label = {
                    Text(
                        text = item,
                        style = mbSubtitleTextStyle()
                    )
                },
                border = SuggestionChipDefaults.suggestionChipBorder(false),
                colors = FilterChipDefaults.filterChipColors()
                    .copy(
                        containerColor = mbGrayLightColor(),
                        selectedContainerColor = MbColor.Yellow
                    ),
                selected = selectedItem.intValue == id,
                leadingIcon = { }
//                Icon(
//                    imageVector = Icons.Filled.Done,
//                    contentDescription = "Done icon",
//                    modifier = Modifier
//                        .size(FilterChipDefaults.IconSize),
//                    active = true
//                )
            )
        }
    }
}

@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun BookmarkFilterViewPreview() {
    MaterialBookmarkMaterialTheme {
        BookmarkFilterView(
            modifier = Modifier,
            filterItems = listOf<String>("Filter 1", "Filter 2", "Filter 3")
        )
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
                searchBookmarkViewModel = null
            )
        }
    }
}

@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun BookmarkListViewPreview2() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(mbGrayLightColor2())) {
            BookmarkAddModalInternalView(
                modifier = Modifier,
                searchBookmarkViewModel = null
            )
        }
    }
}