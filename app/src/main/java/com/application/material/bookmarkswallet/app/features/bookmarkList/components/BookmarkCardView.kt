package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.Bookmark
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.getTimestampFormatted
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbBookmarkFallbackIcon
import com.application.material.bookmarkswallet.app.ui.style.mbCardRoundedCornerShape
import com.application.material.bookmarkswallet.app.ui.style.mbLightYellowLemonDarkYellowLemonColor
import com.application.material.bookmarkswallet.app.ui.style.mbMustardDarkWhiteColor
import com.application.material.bookmarkswallet.app.ui.style.mbMustardDarkYellowColor
import com.application.material.bookmarkswallet.app.ui.style.mbSelectedCardBackgroundColors
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleLightTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleMediumBoldYellowLightDarkTextStyle
import com.application.material.bookmarkswallet.app.utils.EMPTY_BOOKMARK_LABEL
import java.util.Date

@Composable
fun BookmarkCardView(
    modifier: Modifier,
    bookmark: Bookmark,
    onOpenAction: ((Bookmark) -> Unit)? = null
) {
    val context = LocalContext.current
    MbCardView(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onOpenAction?.invoke(bookmark)
            },
        colors = mbSelectedCardBackgroundColors(
            isSelected = bookmark.isPinned,
            isSelectedColor = mbLightYellowLemonDarkYellowLemonColor()
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        bottom = Dimen.paddingMedium16dp
                    )
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //image icon
                AsyncImage(
                    model = bookmark.iconUrl,
                    error = mbBookmarkFallbackIcon(),
                    placeholder = mbBookmarkFallbackIcon(),
                    contentScale = ContentScale.Fit,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(
                            shape = mbCardRoundedCornerShape()
                        )
                        .size(
                            size = Dimen.sizeExtraLarge64dp
                        ),
                )

                if (bookmark.isPinned) {
                    Icon(
                        modifier = Modifier
                            .size(
                                size = Dimen.size32dp
                            )
                            .align(
                                alignment = Alignment.CenterVertically
                            ),
                        painter = painterResource(
                            id = R.drawable.ic_pin_new_dark
                        ),
                        tint = mbMustardDarkYellowColor(),
                        contentDescription = null
                    )
                }
            }

            //title
            Text(
                modifier = Modifier
                    .align(
                        alignment = Alignment.Start
                    )
                    .padding(bottom = Dimen.paddingSmall8dp),
                style = mbTitleMediumBoldYellowLightDarkTextStyle(),
                maxLines = 2,
                text = bookmark.title ?: EMPTY_BOOKMARK_LABEL
            )

            //description
            Text(
                modifier = Modifier
                    .align(
                        alignment = Alignment.Start
                    )
                    .padding(
                        bottom = Dimen.paddingMedium16dp
                    ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 4,
                style = mbSubtitleTextStyle(
                    color =
                        mbMustardDarkWhiteColor()
                ),
                text = bookmark.url
            )

            //timestamp
            Text(
                modifier = Modifier
                    .align(
                        alignment = Alignment.End
                    ),
                maxLines = 1,
                style = mbSubtitleLightTextStyle(),
                text = bookmark.getTimestampFormatted(
                    context = context
                )
            )
        }
    }
}

@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun BookmarkCardPreview() {
    MaterialBookmarkMaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(
                space = Dimen.paddingMedium16dp
            )
        ) {
            BookmarkCardView(
                modifier = Modifier,
                bookmark = Bookmark(
                    title = "This is a title",
                    siteName = "Blalallallalala",
                    timestamp = Date(),
                    iconUrl = "",
                    url = "http://www.google.it/bdslfa;sd/sdfsad/sad/f/sdsa/d/fsa.df./as/d/f/asdf//sad/f/sa/df/sa/d/f/asd/f/as/dfsa/df//sa/df/sa/f/sd/f/as/df/a/sd/fa/sd",
                    appId = "1",
                    isPinned = false
                )
            )
            BookmarkCardView(
                modifier = Modifier,
                bookmark = Bookmark(
                    title = "This is a title",
                    siteName = "Blalallallalala",
                    timestamp = Date(),
                    iconUrl = "",
                    url = "http://www.google.it/bdslfa;sd/sdfsad/sad/f/sdsa/d/fsa.df./as/d/f/asdf//sad/f/sa/df/sa/d/f/asd/f/as/dfsa/df//sa/df/sa/f/sd/f/as/df/a/sd/fa/sd",
                    appId = "1",
                    isPinned = true
                )
            )
        }
    }
}
