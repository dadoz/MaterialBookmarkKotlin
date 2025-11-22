package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbButtonRoundedCornerShape
import com.application.material.bookmarkswallet.app.ui.style.mbExtraLightGrayGrayBlueDarkColor
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonLightColor

@Composable
fun MbActionBoxButtonView(
    modifier: Modifier,
    hasBackground: Boolean = true,
    isSelected: Boolean = false,
    color: Color = mbExtraLightGrayGrayBlueDarkColor(),
    selectedColor: Color = mbYellowLemonLightColor(),
    onClickAction: (() -> Unit)? = null,
    item: @Composable (BoxScope.() -> Unit),
) {
    val colorBySelected = when {
        isSelected -> selectedColor
        else -> color
    }

    Box(
        modifier = modifier
            .clip(
                shape = mbButtonRoundedCornerShape()
            )
            .let {
                when {
                    hasBackground -> it.background(
                        color = colorBySelected
                    )

                    else -> it
                }
            }
            .clickable(
                enabled = onClickAction != null,
                onClick = onClickAction ?: {}
            )
            .padding(
                all = Dimen.paddingMedium16dp
            ),
        content = item
    )
}


@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MbPinningButtonActionView2Preview() {
    MaterialBookmarkMaterialTheme {
        MbActionBoxButtonView(
            modifier = Modifier,
            hasBackground = true,
            isSelected = false
        ) {
            Box(
                modifier = Modifier
                    .size(
                        size = Dimen.size36dp
                    )
            ) { }
        }
    }
}