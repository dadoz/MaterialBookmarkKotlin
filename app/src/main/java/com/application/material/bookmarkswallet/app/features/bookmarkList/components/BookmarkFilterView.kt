package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkListType
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterHp
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterType
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbChipRoundedCornerShape
import com.application.material.bookmarkswallet.app.ui.style.mbFilterChipColors
import com.application.material.bookmarkswallet.app.ui.style.mbFilterIconColor
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor2
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextColor
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextSmallStyle

@Composable
fun <T : FilterType> BookmarkFilterView(
    modifier: Modifier = Modifier,
    filterItems: List<T>,
    initValues: Map<T, Boolean> = mapOf(),
    isSelectedOverride: Boolean = false,
    hasToShowLabel: Boolean = true,
    onSelectedFilter: (T, Boolean) -> Unit
) {
    //single state of filter
    val isSelectedMap: MutableMap<T, Boolean> = remember {
        //<3 mutableStateMap remember
        mutableStateMapOf<T, Boolean>()
    }
        .apply {
            initValues
                .onEachIndexed { index, item ->
                    this[item.key] = item.value
                }
        }

    LazyRow(
        horizontalArrangement = Arrangement
            .spacedBy(space = Dimen.paddingSmall8dp),
        modifier = modifier
    ) {
        items(
            items = filterItems
        ) { item ->

            //!!!!!!!important take for each item new value
            val isSelected = isSelectedOverride
                .takeIf { it }
                ?: isSelectedMap[item] ?: false

            //item please clean up this stuff
            FilterChip(
                modifier = Modifier,
                onClick = {
                    (isSelectedMap[item]?.not() ?: false)
                        .also {
                            //update map
                            isSelectedMap[item] = it
                        }
                        .also { newValue ->
                            //callback to handle view
                            onSelectedFilter.invoke(
                                item,
                                isSelectedOverride
                                    .takeIf { it }
                                    ?: newValue
                            )
                        }
                },
                shape = mbChipRoundedCornerShape(),
                label = {
                    when {
                        hasToShowLabel -> {
                            Text(
                                modifier = Modifier
                                    .padding(
                                        vertical = Dimen.paddingMedium12dp
                                    ),
                                text = stringResource(id = item.labelRes),
                                style = mbSubtitleTextSmallStyle(
                                    color = mbSubtitleTextColor(
                                        isSelected = isSelected
                                    )
                                )
                            )
                        }
                    }
                },
                border = null,
                colors = mbFilterChipColors(),
                selected = isSelected,
                trailingIcon = {
                    item.iconRes
                        ?.let {
                            Icon(
                                modifier = Modifier
                                    .let {
                                        when {
                                            hasToShowLabel -> it

                                            else -> it.padding(
                                                vertical = Dimen.paddingMedium12dp
                                            )
                                        }
                                    }
                                    .size(
                                        size = FilterChipDefaults.IconSize
                                    ),
                                painter = painterResource(id = it),
                                contentDescription = "Done icon",
                                tint = mbFilterIconColor(
                                    isSelected = isSelected
                                )
                            )
                        }
                }
            )
        }
    }
}


@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun BookmarkFilterViewPreview() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(mbGrayLightColor2())) {
            Column() {
                BookmarkFilterView(
                    modifier = Modifier,
                    filterItems = FilterHp.entries,
                    onSelectedFilter = { _, _ -> },
                )
                BookmarkFilterView(
                    modifier = Modifier,
                    isSelectedOverride = true,
                    filterItems = FilterHp.entries,
                    onSelectedFilter = { _, _ -> },
                )
            }
        }
    }
}

@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun BookmarkFilterViewPreview2() {
    MaterialBookmarkMaterialTheme {
        Column() {
            BookmarkFilterView(
                modifier = Modifier,
                isSelectedOverride = true,
                hasToShowLabel = false,
                filterItems =
                    BookmarkListType.entries,
                onSelectedFilter = { _, _ -> },
            )
            BookmarkFilterView(
                modifier = Modifier,
                isSelectedOverride = false,
                hasToShowLabel = false,
                filterItems =
                    BookmarkListType.entries,
                onSelectedFilter = { _, _ -> },
            )
        }
    }
}