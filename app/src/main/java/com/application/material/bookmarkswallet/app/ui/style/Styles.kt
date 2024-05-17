package com.application.material.bookmarkswallet.app.ui.style

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.application.material.bookmarkswallet.app.R

@Composable
fun mbTitleBoldTextStyle() = TextStyle(
    color = colorResource(R.color.colorAccent),
    fontSize = 26.sp,// myVeTitleMedium(),
    fontFamily = MbYantramanavBoldFontFamily,
    fontWeight = FontWeight.Normal
)

@Composable
fun mbSubtitleTextStyle() = TextStyle(
    color = colorResource(R.color.colorPrimary),
    fontSize = 18.sp,// myVeTitleMedium(),
    fontFamily = MbYantramanavLightFontFamily,
    fontWeight = FontWeight.Normal
)

@Composable
fun mbSubtitleLightTextStyle() = mbSubtitleTextStyle().copy(
    fontFamily = MbYantramanavThinFontFamily,
)

@Composable
fun mbButtonTextStyle() = TextStyle(
    color = MaterialTheme.colorScheme.primary,
    fontSize = MaterialTheme.typography.labelLarge.fontSize,
    fontFamily = MbYantramanavBoldFontFamily,
    fontWeight = FontWeight.Normal
)

@Composable
fun mbWhiteLightBlackFilter(isEnabled: Boolean = true) =
    ColorFilter.tint(
        when (isEnabled) {
            true -> MbColor.Black
            else -> MbColor.Yellow
        }
    )

@Composable
fun mbBasicCardBackgroundColors(): CardColors =
    CardDefaults.cardColors(
        containerColor =
        when (isSystemInDarkTheme()) {
            true -> MaterialTheme.colorScheme.onSurface
            else -> MaterialTheme.colorScheme.surfaceContainerLow
        }
    )

//---------------------FONT FAMILY
val MbYantramanavRegularFontFamily = FontFamily(
    Font(R.font.yantramanav_regular)
)
val MbYantramanavBoldFontFamily = FontFamily(
    Font(R.font.yantramanav_bold)
)
val MbYantramanavLightFontFamily = FontFamily(
    Font(R.font.yantramanav_light)
)
val MbYantramanavThinFontFamily = FontFamily(
    Font(R.font.yantramanav_thin)
)
//---------------------FONT FAMILY

