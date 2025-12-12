package com.application.material.bookmarkswallet.app.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.MbActionBoxButtonView
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbActionBookmarkCardBackgroundColors
import com.application.material.bookmarkswallet.app.ui.style.mbButtonMinRoundedCornerShape
import com.application.material.bookmarkswallet.app.ui.style.mbIconBoxButtonBackgroundColor
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextAccentStyle
import com.application.material.bookmarkswallet.app.ui.style.mbWhiteYellowLemonDarkLightColor
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonLightMustardDarkColor
import com.application.material.bookmarkswallet.app.utils.EMPTY
import com.application.material.bookmarkswallet.app.utils.NINETY_F
import com.application.material.bookmarkswallet.app.utils.TWO
import com.application.material.bookmarkswallet.app.utils.TWOHUNDRED_SEVENTY_F


@Composable
fun MbBoxActionSecondaryButton(
    modifier: Modifier = Modifier,
    iconRes: Int,
    text: String? = null,
    isArrowEnabled: Boolean = false,
    isArrowClicked: Boolean = false,
    hasButtonBackground: Boolean = true,
    hasVerticalPadding: Boolean = false,
    hasFillMaxWidth: Boolean = false,
    backgroundColor: Color = mbActionBookmarkCardBackgroundColors(),
    textStyle: TextStyle = mbSubtitleTextAccentStyle(),
    iconTintColor: Color = mbWhiteYellowLemonDarkLightColor(),
    iconBoxColor: Color = mbIconBoxButtonBackgroundColor(),
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
            .padding(
                all = Dimen.paddingExtraSmall4dp
            )
    ) {
        Row(
            modifier = Modifier
                .let {
                    when {
                        isArrowEnabled -> it.padding(
                            end = Dimen.paddingSmall8dp
                        )

                        else -> it
                    }
                }
                .align(
                    alignment = Alignment.CenterStart
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MbActionBoxButtonView(
                modifier = Modifier,
                hasBackground = hasButtonBackground,
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
                            .let {
                                when {
                                    hasFillMaxWidth -> it.weight(
                                        weight = 1.0f
                                    )

                                    else -> it
                                }
                            }
                            .let {
                                when {
                                    hasVerticalPadding -> it.padding(
                                        vertical = Dimen.paddingMedium16dp
                                    )

                                    else -> it
                                }
                            }
                            .padding(
                                horizontal = Dimen.paddingMedium16dp
                            ),
                        maxLines = TWO,
                        overflow = TextOverflow.Ellipsis,
                        style = textStyle,
                        text = text
                    )
                }

            isArrowEnabled
                .takeIf { it }
                ?.let {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_arrow_right_dark
                        ),
                        contentDescription = EMPTY,
                        modifier = Modifier
                            .size(size = Dimen.size20dp)
                            .rotate(
                                degrees =
                                    when {
                                        isArrowClicked -> TWOHUNDRED_SEVENTY_F

                                        else -> NINETY_F
                                    }
                            ),
                        tint = iconTintColor
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
                iconRes = R.drawable.ic_pin_new_dark,
                text = "Add  \nmanually",
                onClickAction = {},
            )
        }
    }
}

@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchBookmarkView3Preview() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(mbYellowLemonLightMustardDarkColor())) {
            MbBoxActionSecondaryButton(
                iconRes = R.drawable.ic_pin_new_dark,
                text = "Add  \nmanually",
                isArrowEnabled = true,
                isArrowClicked = true,
                hasFillMaxWidth = true,
                onClickAction = {},
            )
        }
    }
}