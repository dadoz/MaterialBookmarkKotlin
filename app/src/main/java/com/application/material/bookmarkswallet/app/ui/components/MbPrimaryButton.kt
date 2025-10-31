package com.application.material.bookmarkswallet.app.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.application.material.bookmarkswallet.app.ui.style.mbButtonRoundedCornerShape
import com.application.material.bookmarkswallet.app.ui.style.mbButtonTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbButtonYellowDarkLightColor
import com.application.material.bookmarkswallet.app.ui.style.mbEnableAlpha

@Composable
fun MbPrimaryButton(
    text: String,
    textStyle: TextStyle = mbButtonTextStyle(),
    colors: ButtonColors = mbButtonYellowDarkLightColor(),
    shape: Shape = mbButtonRoundedCornerShape(),
    hasMaxWidthEnable: Boolean = true,
    modifier: Modifier,
    isEnabled: Boolean = true,
    onClickAction: () -> Unit,
) {
    Button(
        enabled = isEnabled,
        onClick = onClickAction,
        colors = colors,
        shape = shape,
        modifier = modifier
            .mbEnableAlpha(
                isEnabled = isEnabled
            )
            .clickable(
                enabled = isEnabled,
                indication = ripple(
                    color = MbColor.DarkGray
                ),
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    if (isEnabled) {
                        onClickAction.invoke()
                    }
                }
            )
            .heightIn(
                min = Dimen.size48dp
            )
            .let { modifier ->
                when (hasMaxWidthEnable) {
                    true -> modifier.fillMaxWidth()
                    else -> modifier
                }
            }
    ) {
        Text(
            text = text,
            style = textStyle,
            maxLines = 1
        )
    }
}


@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewPrimaryButton() {
    MaterialBookmarkMaterialTheme {
        Row(
            modifier =
                Modifier
                    .background(color = MbColor.White)
                    .padding(all = Dimen.paddingMedium16dp)
        ) {
            MbPrimaryButton(
                text = stringResource(id = R.string.add_new_string),
                isEnabled = true,
                modifier = Modifier
            ) { }
        }
    }
}
