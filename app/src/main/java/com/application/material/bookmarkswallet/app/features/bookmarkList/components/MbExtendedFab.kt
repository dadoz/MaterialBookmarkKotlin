package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbButtonTextDarkStyle
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor
import com.application.material.bookmarkswallet.app.ui.style.mbMustardGrayBlueLightDarkColor
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonLightColor
import com.application.material.bookmarkswallet.app.utils.EMPTY

@Composable
fun MBExtendedFab(
    modifier: Modifier,
    title: String,
    iconRes: Int,
    onClickAction: () -> Unit
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        containerColor = mbYellowLemonLightColor(),
        contentColor = mbGrayLightColor(),
        text = {
            Text(
                modifier = Modifier,
                style = mbButtonTextDarkStyle(
                    color = mbMustardGrayBlueLightDarkColor()
                ),
                text = title
            )
        },
        icon = {
            Icon(
                modifier = Modifier
                    .size(size = Dimen.size20dp),
                painter = painterResource(iconRes),
                contentDescription = EMPTY,
                tint = mbMustardGrayBlueLightDarkColor()
            )
        },
        onClick = onClickAction
    )
}
