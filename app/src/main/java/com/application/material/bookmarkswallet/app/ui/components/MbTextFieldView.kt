package com.application.material.bookmarkswallet.app.ui.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.application.material.bookmarkswallet.app.ui.style.mbWhiteDarkGreyCardBackgroundColors
import com.application.material.bookmarkswallet.app.ui.style.mbWhiteMustardDarkColor
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonLightMustardDarkColor

@Composable
fun MbTextFieldView(
    modifier: Modifier,
    textFieldState: MutableState<TextFieldValue>,
    isVisible: Boolean = true,
    titleLabel: String = stringResource(id = R.string.bookmark_url)
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
    }
}

@Composable
fun MbCardTextFieldView(
    modifier: Modifier,
    textFieldState: MutableState<TextFieldValue>,
    hesHorizontalPadding: Boolean = true,
    hasVerticalPadding: Boolean = true,
    isVisible: Boolean = true,
    titleLabel: String = stringResource(id = R.string.bookmark_url),
    outerComponent: @Composable (BoxScope.() -> Unit),
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
        MbCardView(
            modifier = modifier,
            colors = mbWhiteDarkGreyCardBackgroundColors()
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

            Box(
                modifier = Modifier
                    .let {
                        when {
                            hesHorizontalPadding -> it.padding(
                                horizontal = Dimen.paddingMedium16dp
                            )

                            else -> it
                        }
                    }
                    .let {
                        when {
                            hasVerticalPadding -> it.padding(
                                top = Dimen.paddingMedium16dp
                            )

                            else -> it
                        }
                    }
                    .wrapContentWidth(),
            ) {
                outerComponent()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun MbTextFieldViewPreview() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(mbGrayLightColor2())) {
            MbTextFieldView(
                modifier = Modifier,
                isVisible = true,
                titleLabel = "blalalalla",
                textFieldState = remember {
                    mutableStateOf(
                        TextFieldValue()
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun MbTextCardFieldViewPreview() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(mbGrayLightColor2())) {
            MbCardTextFieldView(
                modifier = Modifier,
                textFieldState = remember {
                    mutableStateOf(
                        TextFieldValue()
                    )
                },
                titleLabel = "blalalalla",
                outerComponent = {
                    Text(
                        modifier = Modifier,
                        style = mbSubtitleTextStyle(),
                        text = "blalalalla"
                    )
                },
            )
        }
    }
}
