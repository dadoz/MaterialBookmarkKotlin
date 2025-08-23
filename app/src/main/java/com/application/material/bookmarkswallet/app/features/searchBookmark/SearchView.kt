package com.application.material.bookmarkswallet.app.features.searchBookmark

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.BookmarkPreviewCard
import com.application.material.bookmarkswallet.app.features.searchBookmark.components.BookmarkModalBottomSheetView
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.application.material.bookmarkswallet.app.ui.style.expandedBottomSheetState
import com.application.material.bookmarkswallet.app.ui.style.mbButtonTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor2
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextAccentStyle
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleBoldTextStyle
import com.application.material.bookmarkswallet.app.utils.EMPTY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@Composable
fun SearchBookmarkView(
    modifier: Modifier,
    searchedBookmarkState: StateFlow<Bookmark?>,
    onSearchBookmarkAction: (url: String) -> Unit
) {
    val searchUrlTextState = remember { mutableStateOf(TextFieldValue(EMPTY)) }
    val bookmark by searchedBookmarkState.collectAsState()

    Column(
        modifier = modifier
            .padding(Dimen.sizeMedium16dp)
    ) {
        Text(
            modifier = Modifier,
            style = mbTitleBoldTextStyle(),
            text = stringResource(id = R.string.search_nbookmark)
        )
        Text(
            modifier = Modifier,
            style = mbSubtitleTextStyle(),
            text = stringResource(id = R.string.search_your_bookmark)
        )
        MbCardView(
            modifier = Modifier
                .padding(vertical = Dimen.paddingMedium16dp)
        ) {

            //search text field
            MbBookmarkTextFieldView(
                modifier = Modifier,
                searchUrlTextState = searchUrlTextState
            )

            //clipboard
            Text(
                modifier = Modifier
                    .padding(vertical = Dimen.paddingExtraSmall4dp),
                style = mbSubtitleTextAccentStyle(),
                text = "Hey Search with GEMINI"
            )
            //clipboard
            Text(
                modifier = Modifier
                    .padding(vertical = Dimen.paddingExtraSmall4dp),
                style = mbSubtitleTextAccentStyle(),
                text = "Hey Search with ChatGPT"
            )
            //clipboard
            Text(
                modifier = Modifier
                    .padding(vertical = Dimen.paddingMedium16dp),
                style = mbSubtitleTextStyle(),
                text = stringResource(id = R.string.paste_clipboard)
            )

            //open cta
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .padding(bottom = Dimen.paddingMedium16dp),
                containerColor = MbColor.Yellow,
                text = {
                    Text(
                        modifier = Modifier,
                        style = mbButtonTextStyle(),
                        text = stringResource(id = R.string.search_nbookmark)
                    )
                },
                icon = {
                    Icon(
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp),
                        painter = painterResource(R.drawable.ic_star),
                        contentDescription = EMPTY
                    )
                },
                onClick = {
                    onSearchBookmarkAction.invoke(searchUrlTextState.value.text)
                }
            )
        }

        bookmark?.let {
            BookmarkPreviewCard(
                modifier = Modifier,
                bookmark = it
            )
        }
        Image(
            modifier = Modifier
                .padding(vertical = Dimen.paddingMedium16dp)
                .align(alignment = Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.ic_bear_illustration),
            contentDescription = EMPTY
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkPreview() {
    BookmarkModalBottomSheetView(
        modifier = Modifier,
        bottomSheetState = expandedBottomSheetState(),
        url = "https://www.ecosia.com",
        onCloseCallback = {

        }
    )
}

@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchBookmarkViewPreview() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(mbGrayLightColor2())) {
            SearchBookmarkView(
                modifier = Modifier,
                searchedBookmarkState = remember {
                    MutableStateFlow(null)
                },
                onSearchBookmarkAction = {}
            )
        }
    }
}

@Composable
fun MbBookmarkTextFieldView(
    modifier: Modifier,
    textLabel: String = stringResource(id = R.string.bookmark_url),
    searchUrlTextState: MutableState<TextFieldValue>
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        textStyle = mbSubtitleTextStyle(),
        value = searchUrlTextState.value,
        label = {
            Text(
                modifier = Modifier,
                style = mbSubtitleTextStyle(),
                text = textLabel
            )
        },
        onValueChange = {
            searchUrlTextState.value = it
        })
}
