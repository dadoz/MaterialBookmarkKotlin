package com.application.material.bookmarkswallet.app.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.application.material.bookmarkswallet.app.ui.style.mbActionBookmarkCardBackgroundColors
import com.application.material.bookmarkswallet.app.ui.style.mbCardRoundedCornerShape
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor2
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextAccentStyle
import com.application.material.bookmarkswallet.app.utils.EMPTY


@Composable
fun MbBoxActionSecondaryButton(
    modifier: Modifier = Modifier,
    iconRes: Int,
    text: String,
    backgroundColor: Color = mbActionBookmarkCardBackgroundColors(),
    textStyle: TextStyle = mbSubtitleTextAccentStyle(),
    iconTintColor: Color = MbColor.DarkLemonYellow,
    onClickAction: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .clip(shape = mbCardRoundedCornerShape())
            .background(
                color = backgroundColor
            )
            .clickable(
                enabled = onClickAction != null,
                onClick = onClickAction ?: { }
            )
            .padding(all = Dimen.paddingMedium16dp)
            .wrapContentWidth(),
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(size = 32.dp),
                painter = painterResource(id = iconRes),
                contentDescription = EMPTY,
                tint = iconTintColor
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = Dimen.paddingMedium16dp),
                style = textStyle,
                text = text
            )
        }
    }
}

@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchBookmarkView2Preview() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(mbGrayLightColor2())) {
            MbBoxActionSecondaryButton(
                modifier = Modifier,
                iconRes = R.drawable.ic_pin,
                text = "Add title manually",
                onClickAction = {}
            )
        }
    }
}