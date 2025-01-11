package com.application.material.bookmarkswallet.app.ui.style

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.sp
import com.application.material.bookmarkswallet.app.R

const val densityResizeFactor = 1f

@Composable
fun mbTitleBoldTextStyle() = TextStyle(
    color = colorResource(R.color.colorAccent),
    fontSize = 36.sp,// myVeTitleMedium(),
    fontFamily = MbYantramanavRegularFontFamily,
    fontWeight = FontWeight.Light
)

@Composable
fun mbSubtitleTextAccentStyle() = mbSubtitleTextStyle().copy(
    color = when (isSystemInDarkTheme()) {
        true -> colorResource(R.color.colorAccent)
        else -> MbColor.DarkYellow
    },
    fontFamily = MbYantramanavRegularFontFamily
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
    color = mbWhiteDarkColor(),
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
        containerColor = mbGrayLightColor()
    )

@Composable
fun mbGrayLightColor2(): Color {
    return when (isSystemInDarkTheme()) {
        true -> MbColor.Black
        else -> MaterialTheme.colorScheme.surface
    }
}
@Composable
fun mbGrayLightColor(): Color {
    return when (isSystemInDarkTheme()) {
        true -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.surfaceContainerLow
    }
}
@Composable
fun mbWhiteDarkColor(): Color {
    return when (isSystemInDarkTheme()) {
        true -> MbColor.White
        else -> MbColor.DarkGray
    }
}

@Composable
fun mbBottomSheetRoundedCornerShape() = RoundedCornerShape(
    topStart = Dimen.mbModalRoundedCornerSize,
    topEnd = Dimen.mbModalRoundedCornerSize
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun expandedBottomSheetState(): SheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = true,
    confirmValueChange = { true }
)
fun Density.getResizedDensity(): Float {
    return when {
        (this.density > 2.5f && this.density < 3.0f) -> this.density
        else -> this.density * densityResizeFactor
    }
}

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

