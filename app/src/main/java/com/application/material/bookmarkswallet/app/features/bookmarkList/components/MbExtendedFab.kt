package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.application.material.bookmarkswallet.app.ui.style.mbButtonTextStyle
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
        containerColor = MbColor.Yellow,
        text = {
            Text(
                modifier = Modifier,
                style = mbButtonTextStyle(),
                text = title
            )
        },
        icon = {
            Icon(
                modifier = Modifier
                    .width(Dimen.sizeMedium16dp)
                    .height(Dimen.sizeMedium16dp),
                painter = painterResource(iconRes),
                contentDescription = EMPTY
            )
        },
        onClick = onClickAction
    )
}
