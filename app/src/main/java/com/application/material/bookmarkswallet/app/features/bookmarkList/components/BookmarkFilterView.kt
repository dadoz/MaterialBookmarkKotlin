package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle


@Composable
fun BookmarkFilterView(
    modifier: Modifier = Modifier,
    filterItems: List<String>
) {
    val selectedItem = remember {
        mutableIntStateOf(value = -1)
    }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(Dimen.paddingSmall8dp),
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
                shape = RoundedCornerShape(size = Dimen.mbModalRoundedCornerSize),
                label = {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 12.dp),
                        text = item,
                        style = mbSubtitleTextStyle(
                            color = when (selectedItem.intValue == id) {
                                true -> MbColor.White
                                else -> MbColor.GrayBlueDarkNight
                            }
                        )
                    )
                },
                border = SuggestionChipDefaults.suggestionChipBorder(
                    enabled = false,
                    borderWidth = 0.dp
                ),
                colors = FilterChipDefaults.filterChipColors()
                    .copy(
                        containerColor = mbGrayLightColor(),
                        selectedContainerColor = MbColor.GrayBlueMiddleSea
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
            filterItems = listOf("Filter 1", "Filter 2", "Filter 3")
        )
    }
}