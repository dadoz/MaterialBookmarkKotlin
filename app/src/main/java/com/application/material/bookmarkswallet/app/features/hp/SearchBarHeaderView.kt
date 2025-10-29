package com.application.material.bookmarkswallet.app.features.hp

import android.R
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor2
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.utils.EMPTY
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarHeaderView(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    appBarContainerColor: Color = MbColor.White,
    searchResults: List<String>,
) {
    // Controls expansion state of the search bar
    var expanded by rememberSaveable { mutableStateOf(false) }
    val searchBarState = rememberSearchBarState()
    val scope = rememberCoroutineScope()

    val oldInputField = @Composable {
        SearchBarDefaults.InputField(
            query = textFieldState.text.toString(),
            onQueryChange = { textFieldState.edit { replace(0, length, it) } },
            onSearch = {
                onSearch(textFieldState.text.toString())
                expanded = false
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
            placeholder = { Text("Search") },
            trailingIcon = {
                Icon(
                    modifier = Modifier
                        .size(size = Dimen.size20dp),
                    painter = painterResource(R.drawable.ic_menu_search),
                    contentDescription = EMPTY
                )
            }
        )
    }
    val inputField =
        @Composable {
            SearchBarDefaults.InputField(
                modifier = Modifier,
                searchBarState = searchBarState,
                textFieldState = textFieldState,
                onSearch = {
                    scope.launch { searchBarState.animateToCollapsed() }
                },
                placeholder = {
                    if (searchBarState.currentValue == SearchBarValue.Collapsed) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Search",
                            textAlign = TextAlign.Center,
                            style = mbSubtitleTextStyle()
                        )
                    }
                },
                leadingIcon = {
                    if (searchBarState.currentValue == SearchBarValue.Expanded) {
                        TooltipBox(
                            positionProvider =
                                TooltipDefaults.rememberTooltipPositionProvider(
                                    TooltipAnchorPosition.Above
                                ),
                            tooltip = { PlainTooltip { Text("Back") } },
                            state = rememberTooltipState(),
                        ) {
                            IconButton(
                                onClick = { scope.launch { searchBarState.animateToCollapsed() } }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = "Back",
                                )
                            }
                        }
                    } else {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                },
                trailingIcon = {
                    TooltipBox(
                        positionProvider =
                            TooltipDefaults.rememberTooltipPositionProvider(
                                TooltipAnchorPosition.Above
                            ),
                        tooltip = { PlainTooltip { Text("Mic") } },
                        state = rememberTooltipState(),
                    ) {
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(imageVector = Icons.Default.Mic, contentDescription = "Mic")
                        }
                    }
                },
            )
        }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .semantics { isTraversalGroup = true }

    ) {

        AppBarWithSearch(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            colors = SearchBarDefaults.appBarWithSearchColors(
                searchBarColors = SearchBarColors(
                    containerColor = MbColor.White,
                    dividerColor = MbColor.White,
                    inputFieldColors = inputFieldColors(
                        focusedContainerColor = MbColor.White,
                        unfocusedContainerColor = MbColor.White,
                        disabledContainerColor = MbColor.White,
                    ),
                ),
                appBarContainerColor = appBarContainerColor
            ),
            state = searchBarState,
            inputField = inputField,
//            expanded = expanded,
//            onExpandedChange = { expanded = it },
        )
    }
}

@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun BookmarkListViewPreview2() {
    MaterialBookmarkMaterialTheme {
        Box(
            modifier = Modifier.background(mbGrayLightColor2())
        ) {
            SearchBarHeaderView(
                modifier = Modifier,
                textFieldState = rememberTextFieldState(),
                onSearch = {},
                searchResults = listOf(),
            )
        }
    }
}
