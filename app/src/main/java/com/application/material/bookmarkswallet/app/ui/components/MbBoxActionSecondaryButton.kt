package com.application.material.bookmarkswallet.app.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.MbActionBoxButtonView
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbActionBookmarkCardBackgroundColors
import com.application.material.bookmarkswallet.app.ui.style.mbButtonMinRoundedCornerShape
import com.application.material.bookmarkswallet.app.ui.style.mbExtraLightYellowGrayBlueDarkColor
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextAccentStyle
import com.application.material.bookmarkswallet.app.ui.style.mbWhiteDarkColor
import com.application.material.bookmarkswallet.app.ui.style.mbWhiteYellowLemonDarkLightColor
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonLightMustardDarkColor
import com.application.material.bookmarkswallet.app.utils.EMPTY
import com.application.material.bookmarkswallet.app.utils.NINETY_F
import com.application.material.bookmarkswallet.app.utils.TWOHUNDRED_SEVENTY_F


@Composable
fun MbBoxActionSecondaryButton(
    modifier: Modifier = Modifier,
    iconRes: Int,
    text: String? = null,
    isArrowEnabled: Boolean = false,
    isArrowClicked: Boolean = false,
    backgroundColor: Color = mbActionBookmarkCardBackgroundColors(),
    textStyle: TextStyle = mbSubtitleTextAccentStyle(),
    iconTintColor: Color = mbWhiteYellowLemonDarkLightColor(),
    iconBoxColor: Color = mbExtraLightYellowGrayBlueDarkColor(),
    onClickAction: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .clip(
                shape = mbButtonMinRoundedCornerShape()
            )
            .background(
                color = backgroundColor
            )
            .clickable(
                enabled = onClickAction != null,
                onClick = onClickAction ?: { }
            )
            .padding(all = Dimen.paddingExtraSmall2dp)
            .wrapContentWidth(),
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MbActionBoxButtonView(
                modifier = Modifier,
                color = iconBoxColor,
            ) {
                Icon(
                    modifier = Modifier
                        .size(size = Dimen.size32dp),
                    painter = painterResource(id = iconRes),
                    contentDescription = EMPTY,
                    tint = iconTintColor
                )
            }

            text
                ?.let {
                    Text(
                        modifier = Modifier
                            .padding(
                                horizontal = Dimen.paddingMedium16dp
                            ),
                        style = textStyle,
                        text = text
                    )
                }

            isArrowEnabled
                .takeIf { it }
                ?.let {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right_dark),
                        contentDescription = EMPTY,
                        modifier = Modifier
                            .padding(
                                horizontal = Dimen.paddingMedium16dp,
                            )
                            .size(Dimen.size20dp)
                            .rotate(
                                degrees =
                                    when {
                                        isArrowClicked -> TWOHUNDRED_SEVENTY_F

                                        else -> NINETY_F
                                    }
                            ),
                        tint = mbWhiteDarkColor()
                    )
                }
        }
    }
}

@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchBookmarkView2Preview() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(mbYellowLemonLightMustardDarkColor())) {
            MbBoxActionSecondaryButton(
                modifier = Modifier,
                iconRes = R.drawable.ic_pin_new_dark,
                text = "Add title manually",
                onClickAction = {}
            )
        }
    }
}