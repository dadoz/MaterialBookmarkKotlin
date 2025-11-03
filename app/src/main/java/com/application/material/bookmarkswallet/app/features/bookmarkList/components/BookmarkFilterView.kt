package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FilterChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterHp
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbChipRoundedCornerShape
import com.application.material.bookmarkswallet.app.ui.style.mbFilterChipColors
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextColor
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextSmallStyle

@Composable
fun BookmarkFilterView(
    modifier: Modifier = Modifier,
    filterItems: List<FilterHp>
) {
    val selectedItem = remember {
        mutableIntStateOf(value = -1)
    }
    LazyRow(
        horizontalArrangement = Arrangement
            .spacedBy(space = Dimen.paddingSmall8dp),
        modifier = modifier
    ) {
        itemsIndexed(
            items = filterItems
        ) { id, item ->
            FilterChip(
                modifier = Modifier,
                onClick = {
                    selectedItem.intValue = id
                },
                shape = mbChipRoundedCornerShape(),
                label = {
                    Text(
                        modifier = Modifier
                            .padding(
                                vertical = Dimen.paddingMedium12dp
                            ),
                        text = stringResource(id = item.labelRes),
                        style = mbSubtitleTextSmallStyle(
                            color = mbSubtitleTextColor(
                                isSelected = selectedItem.intValue == id
                            )
                        )
                    )
                },
                border = SuggestionChipDefaults.suggestionChipBorder(
                    enabled = false,
                    borderWidth = 0.dp
                ),
                colors = mbFilterChipColors(),
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
            filterItems =
                listOf(
                    FilterHp.SORT_BY_DATE,
                    FilterHp.PINNED
                )
        )
    }
}