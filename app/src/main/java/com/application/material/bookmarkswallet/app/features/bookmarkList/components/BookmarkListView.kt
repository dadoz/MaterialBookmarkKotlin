package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor2
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleBoldTextStyle
import com.application.material.bookmarkswallet.app.utils.EMPTY
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
        )
    }
}

@Composable
fun BookmarkItemsView(
    modifier: Modifier = Modifier,
    bookmarkItems: List<Bookmark> = emptyList()
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
                bookmark= item,
                onOpenAction= {}
            )
        }
    }
}

@Composable
fun BookmarkFilterView(
    modifier: Modifier = Modifier,
    filterItems: List<String>
) {
    var selected = true
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(Dimen.paddingSmall8dp),
        modifier = modifier
    ) {
        itemsIndexed(
            items = filterItems
        ) { id, item ->
            FilterChip(
                onClick = { selected = !selected },
                label = {
                    Text(
                        text = item,
                        style = mbSubtitleTextStyle()
                    )
                },
                selected = selected,
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