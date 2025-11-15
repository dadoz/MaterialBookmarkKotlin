package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.Bookmark
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.getTimestampFormatted
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbCardRoundedCornerShape
import com.application.material.bookmarkswallet.app.ui.style.mbMustardDarkWhiteColor
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleLightTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleMediumBoldYellowLightDarkTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonDarkLightColor
import com.application.material.bookmarkswallet.app.utils.EMPTY_BOOKMARK_LABEL
import java.util.Date

@Composable
fun BookmarkCardView(
    modifier: Modifier,
    bookmark: Bookmark,
    onOpenAction: ((Bookmark) -> Unit)? = null,
) {
    val context = LocalContext.current
    //fallbackIcon
    val fallbackIcon = rememberDrawablePainterWithColor(
        res = R.drawable.ic_bookmark,
        color = mbYellowLemonDarkLightColor()
    )

    MbCardView(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onOpenAction?.invoke(bookmark)
            }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            //image icon
            AsyncImage(
                model = bookmark.iconUrl,
                error = fallbackIcon,
                placeholder = fallbackIcon,
                contentScale = ContentScale.Fit,
                contentDescription = null,
                modifier = Modifier
                    .align(
                        alignment = Alignment.Start
                    )
                    .padding(
                        bottom = Dimen.paddingMedium16dp
                    )
                    .clip(
                        shape = mbCardRoundedCornerShape()
                    )
                    .size(
                        size = Dimen.sizeExtraLarge64dp
                    ),
            )

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
    BookmarkCardView(
        modifier = Modifier,
        bookmark = Bookmark(
            title = "This is a title",
            siteName = "Blalallallalala",
            timestamp = Date(),
            iconUrl = "",
            url = "http://www.google.it/bdslfa;sd/sdfsad/sad/f/sdsa/d/fsa.df./as/d/f/asdf//sad/f/sa/df/sa/d/f/asd/f/as/dfsa/df//sa/df/sa/f/sd/f/as/df/a/sd/fa/sd",
            appId = null,
            isLike = false,
        ),
    ) {}
}