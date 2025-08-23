package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.models.Bookmark
import com.application.material.bookmarkswallet.app.models.getLocalDate
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleLightTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleMediumBoldTextStyle
import com.application.material.bookmarkswallet.app.utils.EMPTY_BOOKMARK_LABEL
import java.util.Date

@Composable
fun BookmarkCardView(
    modifier: Modifier,
    bookmark: Bookmark,
    onOpenAction: ((Bookmark) -> Unit)? = null,
) {
    //fallbackIcon
    val fallbackIcon = rememberDrawablePainterWithColor(
        res = R.drawable.ic_bookmark,
        colorRes = R.color.colorAccent
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
                colorFilter = ColorFilter.tint(
                    color = MbColor.GrayBlueMiddleSea
                ),
                contentDescription = null,
                modifier = Modifier
                    .align(
                        alignment = Alignment.Start
                    )
                    .padding(bottom = Dimen.paddingMedium16dp)
                    .size(size = Dimen.sizeExtraLarge64dp)
                    .clip(CircleShape)
                    .padding(Dimen.sizeExtraSmall4dp),
            )

            //title
            Text(
                modifier = Modifier
                    .align(
                        alignment = Alignment.Start
                    )
                    .padding(bottom = Dimen.paddingSmall8dp),
                style = mbTitleMediumBoldTextStyle(
                    color = MbColor.DarkLemonYellow
                ),
                maxLines = 2,
                text = bookmark.title ?: EMPTY_BOOKMARK_LABEL
            )

            //description
            Text(
                modifier = Modifier
                    .align(
                        alignment = Alignment.Start
                    ),
                style = mbSubtitleTextStyle(color = MbColor.GrayBlueMiddleSea),
                text = bookmark.url
            )

            //timestampe
            Text(
                modifier = Modifier
                    .align(
                        alignment = Alignment.End
                    ),
                maxLines = 1,
                style = mbSubtitleLightTextStyle(),
                text = bookmark.getLocalDate().toString()
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
            url = "www.google.it",
            appId = null,
            isLike = false,
        ),
    ) {}
}