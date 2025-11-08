package com.application.material.bookmarkswallet.app.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonDarkLightColor
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonLightColor

@Composable
fun MbExtendedFab(
    modifier: Modifier = Modifier,
    onClickAction: () -> Unit,
    title: String,
    iconRes: Int
) {
    ExtendedFloatingActionButton(
        onClick = onClickAction,
        modifier = modifier,
        containerColor = mbYellowLemonLightColor(),
        contentColor = mbMustardGrayBlueLightDarkColor(),
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
                style = mbButtonTextStyle(
                    color = mbMustardGrayBlueLightDarkColor(),
                )
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun MbExtendedFabPreview() {
    MaterialBookmarkMaterialTheme {
        Box(
            modifier = Modifier
                .background(mbGrayLightColor2())
                .padding(
                    all = Dimen.paddingMedium16dp,
                )
        ) {
            MbExtendedFab(
                modifier = Modifier,
                title = stringResource(R.string.add_new_string),
                iconRes = android.R.drawable.ic_input_add,
                onClickAction = { }
            )
        }
    }
}