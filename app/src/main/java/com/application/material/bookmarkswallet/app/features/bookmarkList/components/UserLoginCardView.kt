package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.User
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbCardRoundedCornerShape
import com.application.material.bookmarkswallet.app.ui.style.mbTitleMediumBoldYellowLightDarkTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonDarkLightColor

@Composable
fun UserLoginCardView(
    modifier: Modifier = Modifier,
    user: User,
    onOpenAction: ((User) -> Unit)? = null,
) {
    //fallbackIcon
    val fallbackIcon = rememberDrawablePainterWithColor(
        res = R.drawable.ic_user,
        colorRes = R.color.colorAccent
    )

    MbCardView(
        modifier = modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .clickable {
                onOpenAction?.invoke(user)
            }
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            //image icon
            AsyncImage(
                model = user.profilePictureUrl,
                error = fallbackIcon,
                placeholder = fallbackIcon,
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    color = mbYellowLemonDarkLightColor()
                ),
                contentDescription = null,
                modifier = Modifier
                    .align(
                        alignment = Alignment.CenterVertically
                    )
                    .size(size = 38.dp)
                    .clip(
                        shape = mbCardRoundedCornerShape()
                    )
            )

            //title
            Text(
                modifier = Modifier
                    .align(
                        alignment = Alignment.CenterVertically
                    )
                    .padding(
                        start = Dimen.paddingSmall8dp
                    ),
                style = mbTitleMediumBoldYellowLightDarkTextStyle(),
                maxLines = 2,
                text = user.name
            )

        }
    }
}

@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun UserLoginCardViewPreview() {
    UserLoginCardView(
        modifier = Modifier,
        user = User(
            name = "Davide",
            surname = "Rossi",
            profilePictureUrl = "https://p.kindpng.com/picc/s/727-7271359_philip-j-fry-avatar-hd-png-download.png",
            username = "@davide.rossi",
            age = 40
        ),
    ) {}
}