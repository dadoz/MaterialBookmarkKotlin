package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import android.content.res.Configuration
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkActionTypeEnum.EDIT_ACTION
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkActionTypeEnum.PIN_ACTION
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkActionTypeEnum.SHARE_ACTION
import com.application.material.bookmarkswallet.app.features.searchBookmark.components.WevBaseBottomSheetView
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.application.material.bookmarkswallet.app.ui.style.mbActionBookmarkCardBackgroundColors
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor
import com.application.material.bookmarkswallet.app.ui.style.mbPreviewCardBackgroundColors
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleLightTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleMediumBoldYellowLightDarkTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonDarkLightColor
import com.application.material.bookmarkswallet.app.utils.EMPTY_BOOKMARK_LABEL
import com.application.material.bookmarkswallet.app.utils.formatDateToStringNew
import com.application.material.bookmarkswallet.app.utils.shareContentIntentBuilder
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkModalPreviewCard(
    modifier: Modifier,
    bookmark: Bookmark,
    onDeleteCallback: (Bookmark) -> Unit,
    onOpenAction: (String) -> Unit,
    bottomSheetVisible: MutableState<Boolean>
) {
    //bottom sheet modal
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val coroutineScope = rememberCoroutineScope()

    if (bottomSheetVisible.value) {
        WevBaseBottomSheetView(
            modifier = modifier
                .wrapContentHeight(),
//                .padding(top = 120.dp),
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
                modifier = modifier,
                bookmark = bookmark,
                onDeleteAction = onDeleteCallback,
                onOpenAction = onOpenAction,
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
    isActionMenuVisible: Boolean = true,
) {
    //fallbackIcon
    val fallbackIcon = rememberDrawablePainterWithColor(
        res = R.drawable.ic_bookmark,
        color = mbYellowLemonDarkLightColor()
    )
    MbCardView(
        modifier = modifier
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
//                .clip(CircleShape)
                .padding(Dimen.sizeExtraSmall4dp),
        )


        MbCardView(
            modifier = modifier,
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
            //timestamp
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = Dimen.paddingLarge32dp
                    ),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.End,
                    style = mbSubtitleLightTextStyle(),
                    text = bookmark.timestamp.formatDateToStringNew()
                )
            }

            if (isActionMenuVisible) {
                MbActionMenuBookmarkPreviewView(
                    modifier = modifier,
                    bookmark = bookmark,
                    onDeleteAction = onDeleteAction
                )
            }
        }


        //open action
        MBExtendedFab(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimen.paddingMedium16dp),
            title = stringResource(id = R.string.open_bookmark),
            iconRes = R.drawable.ic_send,
            onClickAction = {
                onOpenAction?.invoke(bookmark.url)
            }
        )
    }
}

@Composable
fun MbActionMenuBookmarkPreviewView(
    modifier: Modifier,
    bookmark: Bookmark,
    onDeleteAction: ((Bookmark) -> Unit)? = null,
) {
    val context = LocalContext.current
    val isActionMenuVisible = remember { mutableStateOf(false) }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimen.paddingMedium16dp)
    ) {
        AnimatedVisibility(
            modifier = Modifier.wrapContentWidth(),
            visible = isActionMenuVisible.value
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimen.paddingMedium16dp)
            ) {
                //delete cta
                MbDeleteBookmarkButtonView(
                    modifier = Modifier,
                    bookmark = bookmark,
                    onDeleteCallback = onDeleteAction ?: {}
                )
                //action item row
                Row(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(22.dp))
                        .background(
                            color = mbActionBookmarkCardBackgroundColors()
                        )
                        .padding(all = Dimen.paddingMedium16dp)
                        .wrapContentWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimen.paddingSmall8dp)
                ) {
                    listOf(
                        Pair(
                            first = EDIT_ACTION,
                            second = AppCompatResources.getDrawable(
                                LocalContext.current,
                                R.drawable.ic_edit_light_square
                            )
                        ),
                        Pair(
                            first = PIN_ACTION,
                            second =
                                AppCompatResources.getDrawable(
                                    LocalContext.current,
                                    R.drawable.ic_pin
                                )
                        ),
                        Pair(
                            first = SHARE_ACTION,
                            second =
                                AppCompatResources.getDrawable(
                                    LocalContext.current,
                                    R.drawable.ic_share_light
                                )
                        )
                    )
                        .onEach { actionItem ->
                            Image(
                                modifier = Modifier
                                    .padding(horizontal = Dimen.paddingExtraSmall4dp)
                                    .size(size = Dimen.sizeLarge32dp)
                                    .clickable {
                                        when (actionItem.first) {
                                            SHARE_ACTION -> {
                                                context.startActivity(
                                                    shareContentIntentBuilder(
                                                        url = bookmark.url
                                                    )
                                                )
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
                                painter = rememberDrawablePainter(drawable = actionItem.second),
                                colorFilter = ColorFilter
                                    .tint(color = colorResource(R.color.colorPrimary)),
                                contentDescription = ""
                            )
                        }
                }
            }
        }
        //action item row
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(22.dp))
                .background(
                    color = mbGrayLightColor()
                )
                .padding(all = Dimen.paddingMedium16dp)
                .wrapContentWidth(),
        ) {
            Image(
                modifier = Modifier
                    .padding(horizontal = Dimen.paddingExtraSmall4dp)
                    .size(size = Dimen.sizeLarge32dp)
                    .clickable {
                        //toggle status
                        isActionMenuVisible.value = isActionMenuVisible.value.not()
                        //show text
                        Toast.makeText(
                            context,
                            "hey you tap more",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                painter = rememberDrawablePainter(
                    drawable = AppCompatResources.getDrawable(
                        LocalContext.current,
                        R.drawable.ic_apps
                    )
                ),
                colorFilter = ColorFilter
                    .tint(color = colorResource(R.color.colorPrimary)),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun MbDeleteBookmarkButtonView(
    modifier: Modifier,
    bookmark: Bookmark,
    onDeleteCallback: (Bookmark) -> Unit,
) {
    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(22.dp))
            .background(MbColor.RedVermillion)
            .padding(Dimen.paddingMedium16dp)
            .clickable {
                onDeleteCallback.invoke(bookmark)
            }) {
        Image(
            modifier = Modifier
                .width(Dimen.sizeLarge32dp)
                .height(Dimen.sizeLarge32dp),
            painter = rememberDrawablePainter(
                drawable = AppCompatResources.getDrawable(
                    LocalContext.current, R.drawable.ic_delete
                )
            ),
            colorFilter = ColorFilter.tint(color = colorResource(R.color.colorPrimary)),
            contentDescription = ""
        )
//        Text(
//            modifier = Modifier
//                .align(Alignment.CenterVertically)
//                .padding(start = Dimen.paddingSmall8dp),
//            style = mbSubtitleLightTextStyle(),
//            text = stringResource(id = R.string.delete_label_text)
//                .lowercase(Locale.getDefault())
//        )
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
    MbDeleteBookmarkButtonView(
        modifier = Modifier,
        bookmark = Bookmark("blal", "blal", "", "", "", Date(), false),
        onDeleteCallback = {}
    )
}

@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun BookmarkPreviewCardPreview() {
    BookmarkPreviewCard(
        modifier = Modifier,
        bookmark =
            Bookmark(
                title = "Outlook, Email Calendar Office and People",
                siteName = "Blalallallalala",
                timestamp = Date(),
                iconUrl = "",
                url = "http://outlook.com.ddd.dddddd.ddddd.sdssd/sdafasd/asdfasdf",
                appId = null,
                isLike = false,
            ),
        onDeleteAction = {},
        {},
    )
}

