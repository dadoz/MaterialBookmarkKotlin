package com.application.material.bookmarkswallet.app.features.searchBookmark

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonDefaults.leadingButtonContentPaddingFor
import androidx.compose.material3.SplitButtonDefaults.leadingButtonShapesFor
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.BookmarkListButtonContainerHeight
import com.application.material.bookmarkswallet.app.features.searchBookmark.components.BookmarkModalBottomSheetView
import com.application.material.bookmarkswallet.app.features.searchBookmark.viewmodels.SearchBookmarkViewModel
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.application.material.bookmarkswallet.app.ui.style.expandedBottomSheetState
import com.application.material.bookmarkswallet.app.ui.style.mbBasicCardGrayBackgroundColors
import com.application.material.bookmarkswallet.app.ui.style.mbButtonTextDarkStyle
import com.application.material.bookmarkswallet.app.ui.style.mbButtonTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor2
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextAccentStyle
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleBoldTextStyle
import com.application.material.bookmarkswallet.app.utils.EMPTY
import timber.log.Timber


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SearchAndAddBookmarkInternalView(
    modifier: Modifier = Modifier,
    searchBookmarkViewModel: SearchBookmarkViewModel? = hiltViewModel(),
    onSuccessCallback: (bookmark: Bookmark) -> Unit = { bookmark -> },
    onErrorCallback: (e: Throwable) -> Unit = { e -> }
) {
    var checked by remember { mutableStateOf(value = false) }
    val rotation by animateFloatAsState(
        targetValue = if (checked) 180f else 0f,
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

        //MbExtendedFab
        ExtendedFloatingActionButton(
            onClick = {
                searchBookmarkViewModel?.searchUrlInfoByUrlGenAI(
                    url = bookmarkUrl.value.text,
                    onCompletion = {
                        Timber.d("stored on list")
                    },
                    onErrorCallback = onErrorCallback,
                    onSuccessCallback = onSuccessCallback
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

        //MbExtendedFab
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
            modifier = Modifier
                .padding(bottom = Dimen.paddingMedium16dp)
                .padding(end = Dimen.paddingMedium16dp),
            leadingButton = {
                SplitButtonDefaults
                    .ElevatedLeadingButton(
                        modifier = Modifier.defaultMinSize(
                            minHeight = BookmarkListButtonContainerHeight,
                        ),
                        shapes = leadingButtonShapesFor(
                            buttonHeight = BookmarkListButtonContainerHeight
                        ),
                        contentPadding = leadingButtonContentPaddingFor(
                            buttonHeight = BookmarkListButtonContainerHeight
                        ),
                        onClick = {
                        },
                        colors = ButtonColors(
                            containerColor = MbColor.Yellow,
                            contentColor = MbColor.GrayBlueMiddleSea,
                            disabledContainerColor = MbColor.Yellow,
                            disabledContentColor = MbColor.GrayBlueMiddleSea
                        ),
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            modifier = Modifier
                                .size(
                                    size = SplitButtonDefaults
                                        .leadingButtonIconSizeFor(
                                            buttonHeight = BookmarkListButtonContainerHeight
                                        )
                                ),
                            contentDescription = "Localized description",
                        )
                        Text(
                            modifier = Modifier
                                .padding(start = 4.dp),
                            text = "Save or Gemini",
                            style = mbButtonTextDarkStyle()
                            //ButtonDefaults.textStyleFor(BookmarkListButtonContainerHeight)
                        )
                    }
            },
            trailingButton = {
                SplitButtonDefaults
                    .ElevatedTrailingButton(
                        modifier =
                            Modifier
                                .defaultMinSize(
                                    minHeight = BookmarkListButtonContainerHeight,
                                )
                                .semantics {
                                    stateDescription =
                                        when (checked) {
                                            true -> "Add"

                                            else -> "Collapsed"
                                        }
                                    contentDescription = "Toggle Button"
                                },
                        contentPadding = leadingButtonContentPaddingFor(
                            buttonHeight = BookmarkListButtonContainerHeight
                        ),
                        checked = checked,
                        colors = ButtonColors(
                            containerColor = MbColor.Yellow,
                            contentColor = MbColor.GrayBlueMiddleSea,
                            disabledContainerColor = MbColor.Yellow,
                            disabledContentColor = MbColor.GrayBlueMiddleSea
                        ),
                        onCheckedChange = { checked = it },
                    ) {
                        Icon(
                            Icons.Filled.KeyboardArrowDown,
                            modifier = Modifier
                                .size(
                                    size = SplitButtonDefaults
                                        .leadingButtonIconSizeFor(
                                            buttonHeight = BookmarkListButtonContainerHeight
                                        )
                                )
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
fun SearchBookmarkView(
    modifier: Modifier,
    onSearchBookmarkAction: (url: String) -> Unit,
    onSearchBookmarkWithAIAction: (url: String) -> Unit,
) {
    val searchUrlTextState = remember { mutableStateOf(TextFieldValue(EMPTY)) }
    val bookmarkTitle = remember { mutableStateOf(TextFieldValue(EMPTY)) }
//    val bookmark by searchedBookmarkState.collectAsState()

    Column(
        modifier = modifier
            .padding(Dimen.sizeMedium16dp)
    ) {
        //title
        Text(
            modifier = Modifier,
            style = mbTitleBoldTextStyle(),
            text = stringResource(id = R.string.search_nbookmark)
        )

        //search text field
        MbBookmarkTextFieldView(
            modifier = Modifier,
            searchUrlTextState = searchUrlTextState
        )
        //title text field
//        MbBookmarkTextFieldView(
//            modifier = Modifier,
//            textLabel = "Title",
//            searchUrlTextState = bookmarkTitle
//        )

        //clipboard
        Text(
            modifier = Modifier
                .padding(vertical = Dimen.paddingExtraSmall4dp),
            textDecoration = TextDecoration.Underline,
            style = mbSubtitleTextStyle(),
            text = stringResource(R.string.add_title_manually)
        )

        //clipboard
        MbCardView(
            modifier = Modifier
                .width(180.dp)
                .padding(vertical = Dimen.paddingMedium16dp),
            roundCornerSize = 34.dp,
            colors = mbBasicCardGrayBackgroundColors()
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = Dimen.paddingSmall8dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                    painter = painterResource(R.drawable.ic_star),
                    contentDescription = EMPTY
                )

                Text(
                    modifier = Modifier,
                    style = mbSubtitleTextAccentStyle(),
                    text = stringResource(R.string.paste_clipboard)
                )
            }
        }

        //icon image
        Image(
            modifier = Modifier
                .padding(vertical = Dimen.paddingMedium16dp)
                .align(alignment = Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.ic_bear_illustration),
            contentDescription = EMPTY
        )

        //open cta MbExtendedFab
        ExtendedFloatingActionButton(
            modifier = Modifier
                .padding(bottom = Dimen.paddingMedium16dp),
            containerColor = MbColor.Yellow,
            text = {
                Text(
                    modifier = Modifier,
                    style = mbButtonTextStyle(),
                    text = stringResource(id = R.string.save_label_button)
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
//MbExtendedFab
        ExtendedFloatingActionButton(
            modifier = Modifier
                .padding(bottom = Dimen.paddingMedium16dp),
            containerColor = MbColor.Yellow,
            text = {
                Text(
                    modifier = Modifier,
                    style = mbButtonTextStyle(),
                    text = stringResource(id = R.string.save_ai_label_button)
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
                onSearchBookmarkWithAIAction.invoke(searchUrlTextState.value.text)
            }
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
                onSearchBookmarkAction = {},
                onSearchBookmarkWithAIAction = {}
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
        }
    )
}

@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun BookmarkListViewPreview2() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(mbGrayLightColor2())) {
            SearchAndAddBookmarkInternalView(
                modifier = Modifier,
                searchBookmarkViewModel = null
            )
        }
    }
}

