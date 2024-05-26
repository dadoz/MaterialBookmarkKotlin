package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import android.content.res.Configuration
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.application.material.bookmarkswallet.app.ui.style.mbButtonTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleLightTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleBoldTextStyle
import com.application.material.bookmarkswallet.app.utils.EMPTY
import com.application.material.bookmarkswallet.app.utils.EMPTY_BOOKMARK_LABEL
import com.application.material.bookmarkswallet.app.utils.HTTPS_SCHEMA
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import java.util.Date
import java.util.Locale

@Composable
fun BookmarkPreviewCard(
    modifier: Modifier,
    bookmark: Bookmark,
    onDeleteCallback: (Bookmark) -> Unit
) {
    val localUriHandler = LocalUriHandler.current
    //fallbackIcon
    val fallbackIcon = rememberDrawablePainterWithColor(res = R.drawable.ic_bookmark_light)

    MbCardView(
        modifier = modifier
    ) {
        Box(
            Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .align(alignment = Alignment.CenterEnd),
                horizontalArrangement = Arrangement.spacedBy(Dimen.paddingSmall8dp)
            ) {
                listOf(
                    AppCompatResources.getDrawable(
                        LocalContext.current,
                        R.drawable.ic_edit_light_square
                    ),
                    AppCompatResources.getDrawable(
                        LocalContext.current,
                        R.drawable.ic_star_light
                    ),
                    AppCompatResources.getDrawable(
                        LocalContext.current,
                        R.drawable.ic_share_light
                    )
                ).onEach {
                    Image(
                        modifier = Modifier
                            .padding(horizontal = Dimen.paddingExtraSmall4dp)
                            .width(Dimen.sizeLarge32dp)
                            .height(Dimen.sizeLarge32dp),
                        painter = rememberDrawablePainter(drawable = it),
                        colorFilter = ColorFilter.tint(color = colorResource(R.color.colorPrimary)),
                        contentDescription = ""
                    )
                }
            }
        }

        AsyncImage(
            model = bookmark.iconUrl,
            error = fallbackIcon,
            placeholder = fallbackIcon,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(Dimen.paddingMedium16dp)
                .width(Dimen.sizeExtraLarge96dp)
                .height(Dimen.sizeExtraLarge96dp)
                .clip(CircleShape)
                .padding(Dimen.sizeExtraSmall4dp),
        )

        Text(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(bottom = Dimen.paddingExtraSmall4dp),
            style = mbTitleBoldTextStyle(),
            text = bookmark.title ?: EMPTY_BOOKMARK_LABEL
        )
        Text(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(bottom = Dimen.paddingExtraSmall4dp),
            style = mbSubtitleTextStyle(),
            text = bookmark.url ?: EMPTY
        )
        Text(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(bottom = Dimen.paddingMedium16dp),
            style = mbSubtitleLightTextStyle(),
            text = bookmark.timestamp.toString()
        )

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val (deleteButtonRef, openButtonRef) = createRefs()
            //delete cta
            MbDeleteBookmarkButtonView(
                modifier = Modifier
                    .constrainAs(ref = deleteButtonRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints

                    },
                bookmark = bookmark,
                onDeleteCallback = onDeleteCallback
            )
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .constrainAs(ref = openButtonRef) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                containerColor = MbColor.Yellow,
                text = {
                    Text(
                        modifier = Modifier,
                        style = mbButtonTextStyle(),
                        text = "Open"
                    )
                },
                icon = {
                    Icon(
                        modifier = Modifier
                            .width(Dimen.sizeMedium16dp)
                            .height(Dimen.sizeMedium16dp),
                        painter = painterResource(R.drawable.ic_send),
                        contentDescription = EMPTY
                    )
                },
                onClick = {
                    localUriHandler.openUri("$HTTPS_SCHEMA${bookmark.url}")
                }
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
            .padding(Dimen.paddingSmall8dp)
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
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = Dimen.paddingSmall8dp),
            style = mbSubtitleLightTextStyle(),
            text = stringResource(id = R.string.delete_label_text)
                .lowercase(Locale.getDefault())
        )
    }
}

@Composable
fun rememberDrawablePainterWithColor(res: Int): Painter = rememberDrawablePainter(
    drawable = AppCompatResources.getDrawable(
        LocalContext.current,
        res
    ).also {
        it?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(LocalContext.current, R.color.colorPrimary),
            PorterDuff.Mode.SRC_ATOP
        )
    })

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
        bookmark = Bookmark(
            title = "This is a title",
            siteName = "Blalallallalala",
            timestamp = Date(),
            iconUrl = "",
            url = "www.google.it",
            appId = null,
            isLike = false,
        ),
        onDeleteCallback = {}
    )
}