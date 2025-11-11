package com.application.material.bookmarkswallet.app.features.searchBookmark

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.BookmarkListButtonContainerHeight
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.BookmarkPreviewCard
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.Bookmark
import com.application.material.bookmarkswallet.app.features.searchBookmark.model.SearchResultUIState
import com.application.material.bookmarkswallet.app.features.searchBookmark.viewmodels.SearchBookmarkViewModel
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.components.MbBookmarkTextFieldView
import com.application.material.bookmarkswallet.app.ui.components.MbBoxActionSecondaryButton
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.components.MbLoaderView
import com.application.material.bookmarkswallet.app.ui.components.MbPrimaryButton
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.application.material.bookmarkswallet.app.ui.style.mbButtonTextDarkStyle
import com.application.material.bookmarkswallet.app.ui.style.mbButtonTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbButtonYellowColor
import com.application.material.bookmarkswallet.app.ui.style.mbCardRoundedCornerShape
import com.application.material.bookmarkswallet.app.ui.style.mbErrorBookmarkCardBackgroundColors
import com.application.material.bookmarkswallet.app.ui.style.mbErrorSubtitleTextAccentStyle
import com.application.material.bookmarkswallet.app.ui.style.mbExtraLightGrayGrayBlueDarkColor
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor2
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbSuccessBookmarkCardBackgroundColors
import com.application.material.bookmarkswallet.app.ui.style.mbSuccessSubtitleTextAccentStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleBoldTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleHExtraBigBoldYellowTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbWhiteDarkColor
import com.application.material.bookmarkswallet.app.ui.style.mbWhiteDarkGreyCardBackgroundColors
import com.application.material.bookmarkswallet.app.utils.EMPTY
import com.application.material.bookmarkswallet.app.utils.NINETY_F
import com.application.material.bookmarkswallet.app.utils.TWOHUNDRED_SEVENTY_F
import java.util.Date

@Composable
fun SearchAndAddBookmarkView(
    modifier: Modifier,
    searchResultUIState: SearchResultUIState,
    onSearchBookmarkWithAIAction: (url: String) -> Unit,
) {
    val searchUrlTextState = remember { mutableStateOf(TextFieldValue(EMPTY)) }
    val bookmarkTitle = remember { mutableStateOf(TextFieldValue(EMPTY)) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(Dimen.sizeMedium16dp)
            .verticalScroll(
                state = rememberScrollState(),
                enabled = true
            ),
        verticalArrangement = Arrangement.spacedBy(space = Dimen.paddingMedium16dp)
    ) {
        //title
        Text(
            modifier = Modifier,
            style = mbTitleHExtraBigBoldYellowTextStyle(),
            text = stringResource(id = R.string.search_bookmark)
        )

        when {
            searchResultUIState.isLoading -> {
                MbLoaderView(
                    modifier = Modifier
                        .height(height = Dimen.sizeExtraLarge128dp)
                        .fillMaxWidth()
                )
            }

            //success
            searchResultUIState.bookmark != null -> {
                SearchAndAddBookmarkSuccessView(
                    modifier = Modifier,
                    bookmark = searchResultUIState.bookmark,
                    isActionMenuVisible = false
                )
            }

            else -> {
                if (searchResultUIState.error != null) {
                    MbBoxActionSecondaryButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = Dimen.paddingSmall8dp),
                        iconRes = R.drawable.ic_star,
                        text = stringResource(R.string.oh_snap_error_string),
                        backgroundColor = mbErrorBookmarkCardBackgroundColors(),
                        textStyle = mbErrorSubtitleTextAccentStyle(),
                        iconTintColor = MbColor.RedVermilionLight,
                    )
                }

                //search box and text field
                MbBoxUrlFieldAndTitleSearchView(
                    modifier = Modifier,
                    searchUrlTextState = searchUrlTextState,
                    bookmarkTitle = bookmarkTitle
                )

                //clipboard
                MbBoxActionSecondaryButton(
                    modifier = Modifier,
                    iconRes = R.drawable.ic_pin_new_dark,
                    text = stringResource(R.string.paste_clipboard),
                ) {
                    Toast.makeText(
                        context, R.string.search_bookmark, Toast.LENGTH_LONG
                    ).show()
                }

                //Search and Add button
                MbPrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Dimen.paddingMedium16dp),
                    colors = mbButtonYellowColor(),
                    text = stringResource(id = R.string.save_ai_label_button),
                    onClickAction = {
                        onSearchBookmarkWithAIAction.invoke(searchUrlTextState.value.text)
                    }
                )
            }
        }
    }
}

@Composable
fun MbBoxUrlFieldAndTitleSearchView(
    modifier: Modifier,
    searchUrlTextState: MutableState<TextFieldValue>,
    bookmarkTitle: MutableState<TextFieldValue>
) {
    val context = LocalContext.current
    var isTitleBoxVisible by remember {
        mutableStateOf(value = false)
    }

    MbCardView(
        modifier = modifier,
        colors = mbWhiteDarkGreyCardBackgroundColors()
    ) {
        //search text field
        MbBookmarkTextFieldView(
            modifier = Modifier
                .padding(
                    top = Dimen.paddingSmall8dp
                ),
            searchUrlTextState = searchUrlTextState
        )

        //add title manually
        Row(
            modifier = Modifier
                .clickable {
                    isTitleBoxVisible = isTitleBoxVisible.not()
                }
                .padding(
                    all = Dimen.paddingMedium16dp
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .clickable {
                        Toast.makeText(
                            context, R.string.search_bookmark, Toast.LENGTH_LONG
                        ).show()
                    },
                style = mbSubtitleTextStyle(),
                text = stringResource(R.string.add_title_manually)
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right_dark),
                contentDescription = EMPTY,
                modifier = Modifier
                    .padding(
                        start = Dimen.paddingMedium16dp,
                    )
                    .size(Dimen.size20dp)
                    .rotate(
                        degrees =
                            when {
                                isTitleBoxVisible -> TWOHUNDRED_SEVENTY_F

                                else -> NINETY_F
                            }
                    ),
                tint = mbWhiteDarkColor()
            )
        }

        //title text field
        MbBookmarkTextFieldView(
            modifier = Modifier,
            isVisible = isTitleBoxVisible,
            titleLabel = stringResource(id = R.string.bookmark_title_label),
            subtitleLabel = stringResource(id = R.string.bookmark_title_description),
            searchUrlTextState = bookmarkTitle,

            )
    }
}

@Composable
fun SearchAndAddBookmarkSuccessView(
    modifier: Modifier,
    bookmark: Bookmark,
    isActionMenuVisible: Boolean = true
) {
    Column(
        modifier = modifier
            .padding(
                all = Dimen.sizeMedium16dp
            ),
        verticalArrangement = Arrangement.spacedBy(space = Dimen.paddingSmall8dp)
    ) {
        MbBoxActionSecondaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimen.paddingSmall8dp),
            iconRes = R.drawable.ic_star,
            text = stringResource(R.string.add_bookmark_with_success),
            backgroundColor = mbSuccessBookmarkCardBackgroundColors(),
            textStyle = mbSuccessSubtitleTextAccentStyle(),
            iconTintColor = MbColor.DarkGreenRubin,
        )
        BookmarkPreviewCard(
            modifier = Modifier,
            bookmark = bookmark,
            isActionMenuVisible = isActionMenuVisible,
            isOpenButtonVisible = false
        )
    }
}

@Deprecated("not used anymore")
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SearchAndAddBookmarkWithFullAIView(
    modifier: Modifier = Modifier,
    searchBookmarkViewModel: SearchBookmarkViewModel? = hiltViewModel()
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
            titleLabel = "Title",
            searchUrlTextState = bookmarkTitle
        )
        MbBookmarkTextFieldView(
            modifier = Modifier,
            titleLabel = "Bookmark Url",
            searchUrlTextState = bookmarkUrl
        )
        Box(
            modifier = Modifier
                .clip(shape = mbCardRoundedCornerShape())
                .align(alignment = Alignment.CenterHorizontally)
                .background(color = mbExtraLightGrayGrayBlueDarkColor())
                .padding(all = Dimen.paddingMedium16dp),
        ) {
            Image(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.ic_bear_illustration_200),
                contentDescription = EMPTY
            )
        }
        //MbExtendedFab
        ExtendedFloatingActionButton(
            onClick = {
                searchBookmarkViewModel?.searchUrlInfoByUrlGenAI(
                    url = bookmarkUrl.value.text
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
                    context, R.string.search_bookmark, Toast.LENGTH_LONG
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
                            contentColor = MbColor.GrayBlueAlternativeDark,
                            disabledContainerColor = MbColor.Yellow,
                            disabledContentColor = MbColor.GrayBlueAlternativeDark
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
                            contentColor = MbColor.GrayBlueAlternativeDark,
                            disabledContainerColor = MbColor.Yellow,
                            disabledContentColor = MbColor.GrayBlueAlternativeDark
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


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BookmarkPreview2() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(mbGrayLightColor2())) {
            SearchAndAddBookmarkView(
                modifier = Modifier,
                onSearchBookmarkWithAIAction = {},
                searchResultUIState =
                    SearchResultUIState(
                        isLoading = true,
                        error = Throwable("blbl")
                    )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BookmarkPreview() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(mbGrayLightColor2())) {
            SearchAndAddBookmarkView(
                modifier = Modifier,
                onSearchBookmarkWithAIAction = {},
                searchResultUIState =
                    SearchResultUIState(
                        isLoading = false,
                        error = Throwable("blbl"),
                        bookmark = Bookmark(
                            title = "Dribble blal bll al balalbalalb",
                            url = "www.dribble.com",
                            siteName = "",
                            iconUrl = "",
                            appId = "",
                            timestamp = Date(),
                            isLike = false,
                        )
                    )
            )
        }
    }
}

@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchBookmarkViewPreview() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(mbGrayLightColor2())) {
            SearchAndAddBookmarkView(
                modifier = Modifier,
                onSearchBookmarkWithAIAction = {},
                searchResultUIState =
                    SearchResultUIState(
                        isLoading = false
                    )
            )
        }
    }
}

