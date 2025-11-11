package com.application.material.bookmarkswallet.app.ui.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbCardRoundedCornerShape
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor2
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbWhiteMustardDarkColor
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonLightMustardDarkColor

@Composable
fun MbBookmarkTextFieldView(
    modifier: Modifier,
    textFieldState: MutableState<TextFieldValue>,
    isVisible: Boolean = true,
    titleLabel: String = stringResource(id = R.string.bookmark_url),
    subtitleLabel: String? = null
) {
    val focusRequester = remember {
        FocusRequester()
    }

    //component
    AnimatedVisibility(
        modifier = modifier
            .wrapContentWidth(),
        visible = isVisible
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(
                space = Dimen.paddingMedium16dp
            )
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .focusRequester(
                        focusRequester = focusRequester
                    )
                    .fillMaxWidth(),
                textStyle = mbSubtitleTextStyle(),
                shape = mbCardRoundedCornerShape(),
                value = textFieldState.value,
                placeholder = {
                    Text(
                        modifier = Modifier,
                        style = mbSubtitleTextStyle(),
                        text = titleLabel
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = mbYellowLemonLightMustardDarkColor(),
                    unfocusedBorderColor = mbWhiteMustardDarkColor(),
                ),
                onValueChange = {
                    textFieldState.value = it
                }
            )

            if (subtitleLabel != null) {
                Text(
                    modifier = Modifier,
                    style = mbSubtitleTextStyle(),
                    text = subtitleLabel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun MbBookmarkTextFieldViewPreview() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(mbGrayLightColor2())) {
            MbBookmarkTextFieldView(
                modifier = Modifier,
                isVisible = true,
                titleLabel = "blalalalla",
                subtitleLabel = "hey description",
                textFieldState = mutableStateOf(
                    TextFieldValue()
                ),
            )
        }
    }
}
