package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.User
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbCardRoundedCornerShape
import com.application.material.bookmarkswallet.app.ui.style.mbMustardGrayBlueLightDarkColor
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTabIconColor
import com.application.material.bookmarkswallet.app.ui.style.mbTitleMediumBoldYellowLightDarkTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonDarkLightColor
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonLightMustardDarkColor
import com.application.material.bookmarkswallet.app.utils.GUEST

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
        Column(
            modifier = Modifier
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                //image icon
                AsyncImage(
                    model = user.photoUrl,
                    error = fallbackIcon,
                    placeholder = fallbackIcon,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(
                        color = mbYellowLemonLightMustardDarkColor()
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
                        .takeIf {
                            it.isNotEmpty()
                        }
                        ?: GUEST
                )

                Icon(
                    modifier = Modifier
                        .align(
                            alignment = Alignment.CenterVertically
                        ),
                    painter = painterResource(
                        id = R.drawable.ic_edit_dark
                    ),
                    contentDescription = "item",
                    tint = mbYellowLemonLightMustardDarkColor()
                )
            }

            //title
            Text(
                modifier = Modifier
                    .padding(
                        top = Dimen.paddingSmall8dp
                    )
                    .padding(
                        horizontal = Dimen.paddingSmall8dp
                    ),
                style = mbSubtitleTextStyle(),
                maxLines = 4,
                text = stringResource(id = R.string.userid_login, user.uid)
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
            photoUrl = "https://p.kindpng.com/picc/s/727-7271359_philip-j-fry-avatar-hd-png-download.png",
            uid = "3xfcd11120334cdeffacl123eeeddd11222244",
            email = "blallal@gmail.com"
        ),
    ) {}
}