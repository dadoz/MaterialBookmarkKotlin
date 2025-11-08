package com.application.material.bookmarkswallet.app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbButtonTextStyle

@Composable
fun MbExtendedFab(
    modifier: Modifier = Modifier,
    onClickAction: () -> Unit,
    title: String,
    iconRes: Int
) {
    ExtendedFloatingActionButton(
        onClick = onClickAction,
        modifier = modifier
            .padding(
                top = Dimen.paddingMedium16dp
            ),
        content = {
            Icon(
                modifier = Modifier
                    .size(size = Dimen.size22dp),
                painter = painterResource(id = iconRes),
                contentDescription = "Add Icon"
            )
            Text(
                modifier = Modifier
                    .padding(start = Dimen.paddingSmall8dp),
                text = title,
                style = mbButtonTextStyle()
            )
        }
    )
}