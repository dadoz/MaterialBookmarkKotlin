package com.application.material.bookmarkswallet.app.features.searchBookmark

import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.BookmarkListButtonContainerHeight
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.BookmarkPreviewCard
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.Bookmark
import com.application.material.bookmarkswallet.app.features.searchBookmark.model.SearchResultUIState
import com.application.material.bookmarkswallet.app.features.searchBookmark.viewmodels.SearchBookmarkViewModel
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.components.MbBoxActionSecondaryButton
import com.application.material.bookmarkswallet.app.ui.components.MbCardTextFieldView
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.components.MbLoaderView
import com.application.material.bookmarkswallet.app.ui.components.MbPrimaryButton
import com.application.material.bookmarkswallet.app.ui.components.MbTextFieldView
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.application.material.bookmarkswallet.app.ui.style.mbBookmarkFallbackIcon
import com.application.material.bookmarkswallet.app.ui.style.mbButtonTextDarkStyle
import com.application.material.bookmarkswallet.app.ui.style.mbButtonTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbButtonYellowColor
import com.application.material.bookmarkswallet.app.ui.style.mbCardRoundedCornerShape
import com.application.material.bookmarkswallet.app.ui.style.mbErrorBookmarkCardBackgroundColors
import com.application.material.bookmarkswallet.app.ui.style.mbErrorSubtitleTextAccentStyle
import com.application.material.bookmarkswallet.app.ui.style.mbExtraLightGrayGrayBlueDarkColor
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor2
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleLightTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbSuccessBookmarkCardBackgroundColors
import com.application.material.bookmarkswallet.app.ui.style.mbSuccessSubtitleTextAccentStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleBoldTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleHExtraBigBoldYellowTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbWhiteDarkGreyCardBackgroundColors
import com.application.material.bookmarkswallet.app.ui.style.mbWhiteMustardDarkColor
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonLightMustardDarkColor
import com.application.material.bookmarkswallet.app.utils.EMPTY
import com.application.material.bookmarkswallet.app.utils.ZERO
import java.util.Date

@Composable
fun SearchAndAddBookmarkView(
    modifier: Modifier,
    searchResultUIState: SearchResultUIState,
    onSearchBookmarkWithAIAction: ((url: String, title: String?) -> Unit)? = null,
    onEditBookmarkAction: ((bookmark: Bookmark) -> Unit)? = null,
) {
    val context = LocalContext.current
    //clip manager
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    //states
    val searchUrlTextState = remember { mutableStateOf(TextFieldValue(EMPTY)) }
    val searchTitleTextState = remember { mutableStateOf(TextFieldValue(EMPTY)) }
    //is title box visible
    val isTitleBoxVisible = remember {
        mutableStateOf(value = false)
    }
    //title to show
    var title = stringResource(id = R.string.search_bookmark)
    //is icon box visible
    val isIconBoxVisible = remember {
        mutableStateOf(value = false)
    }

    when {
        searchResultUIState.isInEditMode -> {
            //pverriding values
            title = stringResource(id = R.string.edit_bookmark)
            //url
            searchUrlTextState.value = TextFieldValue(searchResultUIState.bookmark?.url ?: EMPTY)
            //title
            searchTitleTextState.value =
                TextFieldValue(searchResultUIState.bookmark?.title ?: EMPTY)
            isTitleBoxVisible.value = true
            //icon box
            isIconBoxVisible.value = true
        }
    }

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
            text = title
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
            searchResultUIState.bookmark != null
                    && searchResultUIState.isInEditMode.not() -> {
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

                if (isIconBoxVisible.value) {
                    AsyncImage(
                        model = searchResultUIState.bookmark?.iconUrl,
                        error = mbBookmarkFallbackIcon(),
                        placeholder = mbBookmarkFallbackIcon(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .padding(
                                horizontal = Dimen.paddingMedium16dp,
                            )
                            .width(Dimen.sizeExtraLarge96dp)
                            .height(Dimen.sizeExtraLarge96dp)
                            .clip(
                                shape = mbCardRoundedCornerShape()
                            ),
                    )
                }

                //search url field
                MbCardTextFieldView(
                    modifier = modifier
                        .padding(
                            top = Dimen.paddingSmall8dp
                        ),
                    hesHorizontalPadding = false,
                    hasVerticalPadding = false,
                    textFieldState = searchUrlTextState
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        //clipboard
                        MbBoxActionSecondaryButton(
                            modifier = Modifier
                                .padding(
                                    top = Dimen.paddingMedium16dp
                                ),
                            iconRes = R.drawable.ic_pin_new_dark,
                            text = stringResource(R.string.paste_clipboard),
                            onClickAction = {
                                Toast.makeText(
                                    context, R.string.past_clip_message, Toast.LENGTH_LONG
                                ).show()

                                //take first item from clip and set to value on url todo make utils
                                searchUrlTextState.value = clipboard.primaryClip
                                    ?.getItemAt(ZERO)
                                    ?.text
                                    ?.toString()
                                    ?.let {
                                        TextFieldValue(it)
                                    } ?: TextFieldValue(EMPTY)
                            },
                        )
                    }
                }

                //title
                MbCustomTitleTextFieldView(
                    modifier = Modifier,
                    searchTitleTextState = searchTitleTextState,
                    isTitleBoxVisible = isTitleBoxVisible
                )

                when {
                    searchResultUIState.isInEditMode -> {
                        //Search and Add button
                        MbPrimaryButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = Dimen.paddingMedium16dp),
                            colors = mbButtonYellowColor(),
                            text = stringResource(id = R.string.update_bookmark_label),
                            textStyle = mbButtonTextDarkStyle(),
                            onClickAction = {
                                searchResultUIState.bookmark
                                    ?.also {
                                        //update title - works only cos shallow copy later
                                        it.title = searchTitleTextState.value.text
                                        //edit action
                                        onEditBookmarkAction?.invoke(
                                            it
                                        )
                                    }
                            }
                        )
                    }

                    else -> {
                        //Search and Add button
                        MbPrimaryButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = Dimen.paddingMedium16dp),
                            colors = mbButtonYellowColor(),
                            text = stringResource(id = R.string.save_ai_label_button),
                            textStyle = mbButtonTextDarkStyle(),
                            onClickAction = {
                                onSearchBookmarkWithAIAction?.invoke(
                                    searchUrlTextState.value.text,
                                    searchTitleTextState.value.text
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MbCustomTitleTextFieldView(
    modifier: Modifier = Modifier,
    searchTitleTextState: MutableState<TextFieldValue>,
    isTitleBoxVisible: MutableState<Boolean>,
) {
    MbCardView(
        modifier = modifier,
        colors = mbWhiteDarkGreyCardBackgroundColors()
    ) {
        AnimatedVisibility(
            modifier = Modifier,
            visible = isTitleBoxVisible.value
        ) {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(space = Dimen.paddingMedium16dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .focusRequester(
                            focusRequester = FocusRequester()
                        )
                        .fillMaxWidth(),
                    textStyle = mbSubtitleTextStyle(),
                    shape = mbCardRoundedCornerShape(),
                    value = searchTitleTextState.value,
                    placeholder = {
                        Text(
                            modifier = Modifier,
                            style = mbSubtitleTextStyle(),
                            text = stringResource(id = R.string.bookmark_title_label)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = mbYellowLemonLightMustardDarkColor(),
                        unfocusedBorderColor = mbWhiteMustardDarkColor(),
                    ),
                    onValueChange = {
                        searchTitleTextState.value = it
                    }
                )
                Text(
                    modifier = Modifier
                        .padding(
                            bottom = Dimen.paddingMedium16dp
                        ),
                    text = stringResource(
                        id = R.string.bookmark_title_description
                    ),
                    style = mbSubtitleLightTextStyle()
                )
            }
        }

        MbBoxActionSecondaryButton(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(R.string.add_title_manually),
            iconRes = R.drawable.ic_text_dark,
            isArrowClicked = isTitleBoxVisible.value,
            isArrowEnabled = true,
            hasFillMaxWidth = true
        ) {
            isTitleBoxVisible.value = isTitleBoxVisible.value.not()
        }
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
                .padding(
                    bottom = Dimen.paddingSmall8dp
                ),
            iconRes = R.drawable.ic_star,
            text = stringResource(R.string.add_bookmark_with_success),
            hasButtonBackground = false,
            hasVerticalPadding = true,
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
        MbTextFieldView(
            modifier = Modifier,
            titleLabel = "Title",
            textFieldState = bookmarkTitle
        )
        MbTextFieldView(
            modifier = Modifier,
            titleLabel = "Bookmark Url",
            textFieldState = bookmarkUrl
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

@Composable
fun EditBookmarkView(
    modifier: Modifier,
    searchResultUIState: SearchResultUIState,
    onEditBookmarkAction: (bookmark: Bookmark) -> Unit
) {
    SearchAndAddBookmarkView(
        modifier = modifier,
        searchResultUIState = searchResultUIState,
        onEditBookmarkAction = onEditBookmarkAction
    )
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
                onSearchBookmarkWithAIAction = { _, _ -> },
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
                onSearchBookmarkWithAIAction = { _, _ -> },
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
                            isPinned = false,
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
                onSearchBookmarkWithAIAction = { _, _ -> },
                searchResultUIState =
                    SearchResultUIState(
                        isLoading = false
                    )
            )
        }
    }
}

@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchBookmarkView2Preview() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(mbGrayLightColor2())) {
            SearchAndAddBookmarkView(
                modifier = Modifier,
                onSearchBookmarkWithAIAction = { _, _ -> },
                searchResultUIState =
                    SearchResultUIState(
                        isLoading = false,
                        isInEditMode = true
                    )
            )
        }
    }
}
