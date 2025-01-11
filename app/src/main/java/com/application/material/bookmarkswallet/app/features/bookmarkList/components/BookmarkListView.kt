package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor2
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleBoldTextStyle
import com.application.material.bookmarkswallet.app.R

internal val filterList = listOf<String>("List", "Sort by date", "Starred")
@Composable
fun BookmarkListView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(all = Dimen.paddingMedium16dp)
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = Dimen.paddingExtraSmall4dp),
            style = mbTitleBoldTextStyle(),
            text = stringResource(R.string.bookmarks_title)
        )
        Text(
            modifier = Modifier
                .padding(bottom = Dimen.paddingExtraSmall4dp),
            style = mbSubtitleTextStyle(),
            text = stringResource(R.string.bookmarks_description)
        )

        BookmarkFilterView(
            modifier = Modifier,
            filterItems = filterList
        )
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