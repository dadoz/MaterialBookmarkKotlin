package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.searchBookmark.components.WevBaseBottomSheetView
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.application.material.bookmarkswallet.app.ui.style.expandedBottomSheetState
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor2
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleBoldTextStyle
import com.application.material.bookmarkswallet.app.utils.EMPTY
import kotlinx.coroutines.launch
import java.util.Date

val COLUMN_GRID_SIZE = 2
internal val filterList = listOf<String>("List", "Sort by date", "Starred")
internal val bookmarkList = listOf(
    Bookmark(
        appId = Bookmark.getId("www.google.it"),
        siteName = EMPTY,
        title = "Google",
        iconUrl = "www.google.it",
        url = "www.google.it",
        timestamp = Date(),//Dates.today,
        isLike = false
    ),
    Bookmark(
        appId = Bookmark.getId("www.google.it"),
        siteName = EMPTY,
        title = "Google",
        iconUrl = "www.google.it",
        url = "www.google.it",
        timestamp = Date(),//Dates.today,
        isLike = false
    ),
    Bookmark(
        appId = Bookmark.getId("www.google.it"),
        siteName = EMPTY,
        title = "Google",
        iconUrl = "www.google.it",
        url = "www.google.it",
        timestamp = Date(),//Dates.today,
        isLike = false
    ),
    Bookmark(
        appId = Bookmark.getId("www.google.it"),
        siteName = EMPTY,
        title = "Facebook",
        iconUrl = "www.facebook.it",
        url = "www.facebook.it",
        timestamp = Date(),//Dates.today,
        isLike = true
    ),
    Bookmark(
        appId = Bookmark.getId("www.google.it"),
        siteName = EMPTY,
        title = "Google",
        iconUrl = "www.google.it",
        url = "www.google.it",
        timestamp = Date(),//Dates.today,
        isLike = false
    ),
    Bookmark(
        appId = Bookmark.getId("www.google.it"),
        siteName = EMPTY,
        title = "Facebook",
        iconUrl = "www.facebook.it",
        url = "www.facebook.it",
        timestamp = Date(),//Dates.today,
        isLike = true
    ),
    Bookmark(
        appId = Bookmark.getId("www.google.it"),
        siteName = EMPTY,
        title = "Google",
        iconUrl = "www.google.it",
        url = "www.google.it",
        timestamp = Date(),//Dates.today,
        isLike = false
    ),
    Bookmark(
        appId = Bookmark.getId("www.google.it"),
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
    modifier: Modifier = Modifier
) {
    val bottomSheetVisible = remember { mutableStateOf(value = false) }

    Column(
        modifier = modifier
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
            modifier = Modifier,
            bookmarkItems = bookmarkList
        ) {
            bottomSheetVisible.value = true
        }

        BookmarkDetailsModalBottomSheetView(
            modifier = Modifier,
            bottomSheetVisible = bottomSheetVisible
        ) {
            Text(
                modifier = Modifier
                    .padding(all = Dimen.paddingMedium16dp),
                style = mbTitleBoldTextStyle(),
                text = "blalallala"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkDetailsModalBottomSheetView(
    modifier: Modifier = Modifier,
    bottomSheetVisible: MutableState<Boolean>,
    content: @Composable () -> Unit
) {
    val bottomSheetState = expandedBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    if (bottomSheetVisible.value) {
        WevBaseBottomSheetView(
            modifier = modifier
                .wrapContentHeight()
                .padding(top = 120.dp),
            bottomSheetState = bottomSheetState,
            hasDragHandle = true,
            onCloseCallback = {
                bottomSheetVisible.value = false
                coroutineScope.launch {
                    bottomSheetState.hide()
                }
            }
        ) {
            content()
        }
    }
}

@Composable
fun BookmarkItemsView(
    modifier: Modifier = Modifier,
    bookmarkItems: List<Bookmark> = emptyList(),
    onOpenAction: (String) -> Unit = {}
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
                modifier = Modifier,
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
                modifier = Modifier
            )
        }
    }
}