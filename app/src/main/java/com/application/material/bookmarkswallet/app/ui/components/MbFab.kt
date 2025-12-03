package com.application.material.bookmarkswallet.app.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbButtonTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor2
import com.application.material.bookmarkswallet.app.ui.style.mbMustardGrayBlueLightDarkColor
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonLightColor

@Composable
fun MbFab(
    modifier: Modifier = Modifier,
    onClickAction: () -> Unit,
    iconRes: Int
) {
    FloatingActionButton(
        onClick = onClickAction,
        modifier = modifier,
        containerColor = mbYellowLemonLightColor(),
        contentColor = mbMustardGrayBlueLightDarkColor(),
        content = {
            Icon(
                modifier = Modifier,
//                    .size(size = Dimen.size20dp),
                painter = painterResource(id = iconRes),
                contentDescription = "Add Icon"
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun MbExtendedFabPreview2() {
    MaterialBookmarkMaterialTheme {
        Box(
            modifier = Modifier
                .background(mbGrayLightColor2())
                .padding(
                    all = Dimen.paddingMedium16dp,
                )
        ) {
            MbFab(
                modifier = Modifier,
                iconRes = R.drawable.ic_add_dark,
                onClickAction = { }
            )
        }
    }
}