package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import android.content.res.Configuration
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asAndroidColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.Bookmark
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkActionTypeEnum.EDIT_ACTION
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkActionTypeEnum.SHARE_ACTION
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.getTimestampFormatted
import com.application.material.bookmarkswallet.app.features.searchBookmark.components.MbBaseBottomSheetView
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.components.MbPrimaryButton
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbActionBookmarkCardBackgroundAlternativeColors
import com.application.material.bookmarkswallet.app.ui.style.mbCardRoundedCornerShape
import com.application.material.bookmarkswallet.app.ui.style.mbErrorWhiteRedLightDarkColor
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor2
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightExtraBlueDarkColor
import com.application.material.bookmarkswallet.app.ui.style.mbMustardDarkWhiteColor
import com.application.material.bookmarkswallet.app.ui.style.mbPreviewCardBackgroundColors
import com.application.material.bookmarkswallet.app.ui.style.mbRedVermilionLightDarkColor
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleLightTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleMediumBoldYellowLightDarkTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonDarkLightColor
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonLightMustardDarkColor
import com.application.material.bookmarkswallet.app.utils.EMPTY_BOOKMARK_LABEL
import com.application.material.bookmarkswallet.app.utils.shareContentIntentBuilder
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkModalPreviewCardView(
    modifier: Modifier,
    bookmark: Bookmark,
    onDeleteCallback: (Bookmark) -> Unit,
    onOpenAction: (String) -> Unit,
    onPinningAction: (Bookmark) -> Unit,
    onEditAction: (Bookmark) -> Unit,
    bottomSheetVisible: MutableState<Boolean>
) {
    //bottom sheet modal
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val coroutineScope = rememberCoroutineScope()

    if (bottomSheetVisible.value) {
        MbBaseBottomSheetView(
            modifier = modifier
                .wrapContentHeight(),
            bottomSheetState = bottomSheetState,
            hasDragHandle = true,
            onCloseCallback = {
                bottomSheetVisible.value = false
                coroutineScope.launch {
                    bottomSheetState.hide()
                }
            }
        ) {
            BookmarkPreviewCard(
                modifier = Modifier,
                bookmark = bookmark,
                onPinningAction = onPinningAction,
                isActionMenuVisible = true,
                onDeleteAction = onDeleteCallback,
                onOpenAction = onOpenAction,
                onEditAction = onEditAction
            )
        }
    }
}

@Composable
fun BookmarkPreviewCard(
    modifier: Modifier,
    bookmark: Bookmark,
    onDeleteAction: ((Bookmark) -> Unit)? = null,
    onOpenAction: ((String) -> Unit)? = null,
    onPinningAction: ((Bookmark) -> Unit)? = null,
    onEditAction: ((Bookmark) -> Unit)? = null,
    isActionMenuVisible: Boolean = true,
    isOpenButtonVisible: Boolean = true,
) {
    val context = LocalContext.current
    //fallbackIcon
    val fallbackIcon = rememberDrawablePainterWithColor(
        res = R.drawable.ic_bookmark,
        color = mbYellowLemonLightMustardDarkColor()
    )
    Column(
        modifier = modifier
            .padding(
                all = Dimen.paddingMedium16dp
            )
    ) {
        AsyncImage(
            model = bookmark.iconUrl,
            error = fallbackIcon,
            placeholder = fallbackIcon,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(
                    horizontal = Dimen.paddingMedium16dp,
                )
                .padding(
                    bottom = Dimen.paddingLarge32dp
                )
                .width(Dimen.sizeExtraLarge96dp)
                .height(Dimen.sizeExtraLarge96dp)
                .clip(
                    shape = mbCardRoundedCornerShape()
                ),
        )

        MbCardView(
            modifier = Modifier,
            colors = mbPreviewCardBackgroundColors(),
        ) {
            //title and header
            Text(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(horizontal = Dimen.paddingMedium16dp)
                    .padding(
                        top = Dimen.paddingMedium16dp,
                        bottom = Dimen.paddingSmall8dp
                    ),
                textAlign = TextAlign.Center,
                style = mbTitleMediumBoldYellowLightDarkTextStyle(),
                text = bookmark.title ?: EMPTY_BOOKMARK_LABEL
            )
            Text(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(bottom = Dimen.paddingExtraSmall4dp),
                style = mbSubtitleTextStyle(),
                text = bookmark.url
            )
            //pinning button
            MbPinningButtonActionView(
                modifier = Modifier
                    .align(
                        alignment = Alignment.End
                    ),
                bookmark = bookmark,
                isSelected = bookmark.isPinned,
                onPinningAction = onPinningAction
            )
            //timestamp
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        top = Dimen.paddingLarge32dp
                    ),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.End,
                    style = mbSubtitleLightTextStyle(),
                    text = bookmark.getTimestampFormatted(
                        context = context
                    )
                )
            }
        }

        MbActionMenuBookmarkPreviewView(
            modifier = Modifier
                .padding(top = Dimen.paddingMedium16dp),
            isActionMenuVisible = isActionMenuVisible,
            bookmark = bookmark,
            onDeleteAction = onDeleteAction,
            onEditAction = onEditAction
        )

        //open action
        if (isOpenButtonVisible) {
            MbPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimen.paddingMedium16dp),
                text = stringResource(id = R.string.open_bookmark),
                onClickAction = {
                    onOpenAction?.invoke(bookmark.url)
                }
            )
        }
    }
}

@Composable
fun MbActionMenuBookmarkPreviewView(
    modifier: Modifier,
    bookmark: Bookmark,
    backgroundColor: Color = mbGrayLightExtraBlueDarkColor(),
    actionItemBackgroundColor: Color = mbActionBookmarkCardBackgroundAlternativeColors(),
    isActionMenuVisible: Boolean = false,
    onEditAction: ((Bookmark) -> Unit)? = null,
    onDeleteAction: ((Bookmark) -> Unit)? = null,
) {
    val context = LocalContext.current

    AnimatedVisibility(
        modifier = modifier
            .wrapContentWidth(),
        visible = isActionMenuVisible
    ) {
        //action item row
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            //delete cta
            MbDeleteBookmarkButtonView(
                modifier = Modifier,
                bookmark = bookmark,
                onDeleteCallback = onDeleteAction
            )
            actionPreviewBookmarkList
                .onEachIndexed { index, actionItem ->
                    MbActionBoxButtonView(
                        modifier = Modifier
                            .let {
                                when (index) {
                                    actionPreviewBookmarkList.size - 1 -> it

                                    else -> it.padding(
                                        horizontal = Dimen.paddingMedium16dp
                                    )
                                }
                            },
                        color = actionItemBackgroundColor,
                        onClickAction = {
                            when (actionItem.first) {
                                SHARE_ACTION -> {
                                    context.startActivity(
                                        shareContentIntentBuilder(
                                            url = bookmark.url
                                        )
                                    )
                                }

                                EDIT_ACTION -> {
                                    onEditAction?.invoke(bookmark)
                                }

                                else -> {
                                    Toast.makeText(
                                        context,
                                        "hey you tap -> ${actionItem.first.name}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(size = Dimen.sizeLarge32dp),
                            painter = painterResource(id = actionItem.second),
                            tint = mbMustardDarkWhiteColor(),
                            contentDescription = ""
                        )
                    }
                }
        }
    }
}

@Composable
fun MbDeleteBookmarkButtonView(
    modifier: Modifier,
    bookmark: Bookmark,
    onDeleteCallback: ((Bookmark) -> Unit)?,
) {
    MbActionBoxButtonView(
        modifier = modifier,
        color = mbRedVermilionLightDarkColor(),
        onClickAction = {
            onDeleteCallback?.invoke(bookmark)
        }
    ) {
        Icon(
            modifier = Modifier
                .size(size = Dimen.sizeLarge32dp),
            painter = painterResource(
                id = R.drawable.ic_delete_dark
            ),
            tint = mbErrorWhiteRedLightDarkColor(),
            contentDescription = ""
        )
    }
}

@Composable
fun rememberDrawablePainterWithColor(res: Int, colorRes: Int = R.color.colorPrimary): Painter =
    rememberDrawablePainter(
        drawable = AppCompatResources.getDrawable(
            LocalContext.current,
            res
        ).also {
            it?.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(LocalContext.current, colorRes),
                PorterDuff.Mode.SRC_ATOP
            )
        }
    )

@Composable
fun rememberDrawablePainterWithColor(
    res: Int,
    color: Color = Color.White
): Painter =
    rememberDrawablePainter(
        drawable = AppCompatResources.getDrawable(
            LocalContext.current,
            res
        ).also {
            it?.colorFilter = ColorFilter.tint(
                color = color
            ).asAndroidColorFilter()
        }
    )

@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun MbDeleteBookmarkButtonViewPreview() {
    MaterialBookmarkMaterialTheme {
        Box(
            modifier = Modifier
                .background(color = mbYellowLemonDarkLightColor())
                .padding(all = Dimen.paddingMedium16dp)
        ) {
            MbDeleteBookmarkButtonView(
                modifier = Modifier,
                bookmark = Bookmark("blal", "blal", "", "", "", Date(), false),
                onDeleteCallback = {}
            )
        }
    }
}

@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun MbDeleteBookmarkButtonViewPreview2() {
    MaterialBookmarkMaterialTheme {
        Box(
            modifier = Modifier
                .background(color = mbYellowLemonDarkLightColor())
                .padding(all = Dimen.paddingMedium16dp)
        ) {
            MbActionMenuBookmarkPreviewView(
                modifier = Modifier,
                bookmark = Bookmark(
                    "blal", "blal", "", "", "", Date(),
                    isPinned = false
                ),
                isActionMenuVisible = true
            )
        }
    }
}

@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun BookmarkPreviewCardPreview() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(mbGrayLightColor2())) {
            BookmarkPreviewCard(
                modifier = Modifier,
                bookmark =
                    Bookmark(
                        title = "Outlook, Email Calendar Office and People",
                        siteName = "Blalallallalala",
                        timestamp = Date(),
                        iconUrl = "",
                        url = "http://outlook.com.ddd.dddddd.ddddd.sdssd/sdafasd/asdfasdf",
                        appId = "1",
                        isPinned = false
                    ),
                onDeleteAction = {},
                {},
            )
        }
    }
}

