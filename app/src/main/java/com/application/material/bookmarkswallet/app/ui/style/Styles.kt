package com.application.material.bookmarkswallet.app.ui.style

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
fun mbYellowLemonDarkLightColor() = when (isSystemInDarkTheme()) {
    true -> MbColor.Yellow
    else -> MbColor.DarkLemonYellow
}

@Composable
fun mbErrorWhiteRedLightDarkColor(): Color = when (isSystemInDarkTheme()) {
    true -> MbColor.White
    else -> MbColor.RedVermilion
}

@Composable
fun mbRedVermilionLightDarkColor(): Color = when (isSystemInDarkTheme()) {
    true -> MbColor.RedVermilion
    else -> MbColor.RedVermilionLight
}

@Composable
fun mbYellowLemonLightColor() = when (isSystemInDarkTheme()) {
    true -> MbColor.Yellow
    else -> MbColor.Yellow
}

@Composable
fun mbYellowLemonLightMustardDarkColor() = when (isSystemInDarkTheme()) {
    true -> MbColor.Yellow
    else -> MbColor.DarkMustardYellow//DarkLemonYellow
}

@Composable
fun mbTitleHExtraBigBoldYellowTextStyle(
    color: Color = mbYellowLemonLightMustardDarkColor()
) =
    mbTitleHExtraBigBoldTextStyle(
        color = color
    )

@Composable
fun mbTitleHExtraBigBoldTextStyle(color: Color = MbColor.GrayBlueMiddleSea) =
    mbTitleBoldTextStyle().copy(
        fontSize = 36.sp,
        color = color
    )

@Composable
fun mbTitleBoldTextStyle() = TextStyle(
    color = MbColor.GrayBlueMiddleSea,
    fontSize = 28.sp,// myVeTitleMedium(),
    fontFamily = MbYantramanavBoldFontFamily,
    fontWeight = FontWeight.Bold
)

@Composable
fun mbTitleMediumBoldYellowLightDarkTextStyle() =
    mbTitleMediumBoldTextStyle(
        color = mbYellowLemonDarkLightColor()
    )

@Composable
fun mbTitleMediumBoldTextStyle(
    color: Color = MbColor.GrayBlueMiddleSea,
) = mbTitleBoldTextStyle()
    .copy(
        fontSize = 26.sp,
        color = color
    )

@Composable
fun mbSubtitleTextAccentStyle() = mbSubtitleTextStyle()
    .copy(
        color = when (isSystemInDarkTheme()) {
            true -> colorResource(R.color.colorAccent)
            else -> MbColor.DarkMustardYellow
        },
        fontFamily = MbYantramanavRegularFontFamily
    )

@Composable
fun mbErrorSubtitleTextAccentStyle() = mbSubtitleTextAccentStyle().copy(
    color = when (isSystemInDarkTheme()) {
        true -> MbColor.RedVermilionLight
        else -> MbColor.RedVermilionLight
    },
)

@Composable
fun mbSuccessSubtitleTextAccentStyle() = mbSubtitleTextAccentStyle().copy(
    color = when (isSystemInDarkTheme()) {
        true -> MbColor.DarkGreenRubin
        else -> MbColor.DarkGreenRubin
    },
)

@Composable
fun mbSubtitleTextColor(isSelected: Boolean) =
    when (isSelected) {
        true ->
            when (isSystemInDarkTheme()) {
                true -> MbColor.GrayBlueDarkNight
                else -> MbColor.White
            }
        else ->
            when (isSystemInDarkTheme()) {
                true -> MbColor.White
                else -> MbColor.DarkMustardYellow
            }
    }

@Composable
fun mbSubtitleTextSmallStyle(color: Color = colorResource(R.color.colorPrimary)) =
    mbSubtitleTextStyle()
        .copy(
            color = color,
            fontSize = 16.sp,
            fontFamily = MbYantramanavBoldFontFamily,
            fontWeight = FontWeight.Bold
        )

@Composable
fun mbSubtitleTextStyle(color: Color = mbMustardDarkWhiteColor()) = TextStyle(
    color = color,
    fontSize = 20.sp,// myVeTitleMedium(),
    fontFamily = MbYantramanavLightFontFamily,
    fontWeight = FontWeight.Normal
)

@Composable
fun homeBackgroundBrushColor(): Brush =
    when (isSystemInDarkTheme()) {
        true ->
            Brush.verticalGradient(
                colors = listOf(
                    MbColor.DarkGray2,
                    MbColor.DarkGray2
                )
            )

        else ->
            Brush.verticalGradient(
                colors = listOf(
                    MbColor.LemonYellowQuater,
                    MbColor.White,
                    MbColor.White
                ),
                startY = .9f
            )
    }

@Composable
fun mbAppBarContainerColor(): Color = Color.Transparent

@Composable
fun mbMustardDarkWhiteColor(): Color =
    when (isSystemInDarkTheme()) {
        true -> MbColor.White
        else -> MbColor.DarkMustardYellow
    }

@Composable
fun mbFilterChipColors(): SelectableChipColors =
    FilterChipDefaults.filterChipColors()
        .copy(
            containerColor = mbExtraLightGrayGrayBlueDarkColor(),
            selectedContainerColor = mbYellowLemonLightMustardDarkColor()
        )

@Composable
fun mbSubtitleMustardYellowDarkLightTextStyle() =
    mbSubtitleLightTextStyle(
        color = when (isSystemInDarkTheme()) {
            true -> MbColor.Yellow
            else -> MbColor.DarkMustardYellow
        }
    )

fun Modifier.mbEnableAlpha(isEnabled: Boolean): Modifier =
    this.alpha(
        alpha = when {
            isEnabled -> 1f
            else -> 0.5f
        }
    )

@Composable
fun mbCardRoundedCornerShape() =
    RoundedCornerShape(
        size = Dimen.mbCardRoundCornerSize
    )

@Composable
fun mbButtonRoundedCornerShape() =
    RoundedCornerShape(
        size = Dimen.mbButtonRoundCornerSize
    )

@Composable
fun mbButtonYellowDarkLightColor() = mbButtonColorStyle(
    lightColor = MbColor.Yellow,
    darkColor = MbColor.DarkMustardYellow,
)

@Composable
fun mbButtonColorStyle(
    lightColor: Color,
    darkColor: Color,
    disabledLightColor: Color = Color.LightGray,
    disabledDarkColor: Color = Color.LightGray
) =
    when (isSystemInDarkTheme()) {
        true -> Pair(darkColor, disabledDarkColor)
        else -> Pair(lightColor, disabledLightColor)
    }.let {
        ButtonDefaults.buttonColors(
            containerColor = it.first,
            contentColor = it.first,
            disabledContainerColor = it.second,
            disabledContentColor = it.second
        )
    }

@Composable
fun mbSubtitleLightTextStyle(color: Color = MbColor.DarkMustardYellow) = mbSubtitleTextStyle()
    .copy(
        color = when (isSystemInDarkTheme()) {
            true -> MbColor.White
            else -> color
        },
        fontSize = 14.sp,
        fontFamily = MbYantramanavThinFontFamily
    )

@Composable
fun mbFilterIconColor(isSelected: Boolean) = when {
    isSystemInDarkTheme() -> when {
        isSelected -> MbColor.GrayBlueDarkNight
        else -> MbColor.White
    }

    else -> when {
        isSelected -> MbColor.White
        else -> MbColor.DarkMustardYellow
    }
}
@Composable
fun mbTabIconColor(isSelected: Boolean) = when (isSystemInDarkTheme()) {
    true -> when {
        isSelected -> MbColor.DarkMustardYellow
        else -> MbColor.White
    }

    else -> when {
        isSelected -> MbColor.White
        else -> MbColor.DarkMustardYellow
    }
}

@Composable
fun mbButtonTextStyle() = TextStyle(
    color = mbWhiteDarkColor(),
    fontSize = MaterialTheme.typography.labelLarge.fontSize,
    fontFamily = MbYantramanavBoldFontFamily,
    fontWeight = FontWeight.Normal
)

@Composable
fun mbButtonTextDarkStyle(
    color: Color = MbColor.DarkGray,
) = TextStyle(
    color = color,
    fontSize = MaterialTheme.typography.labelLarge.fontSize,
    fontFamily = MbYantramanavBoldFontFamily,
    fontWeight = FontWeight.Normal
)

@Composable
fun mbPreviewCardBackgroundColors(): CardColors =
    CardDefaults.cardColors(
        containerColor = mbGrayLightExtraBlueDarkColor()
    )

@Composable
fun mbBasicCardBackgroundColors(): CardColors =
    CardDefaults.cardColors(
        containerColor = mbExtraLightGrayGrayBlueDarkColor()
    )

@Composable
fun mbGrayLightColor2(): Color {
    return when (isSystemInDarkTheme()) {
        true -> MbColor.Black
        else -> MaterialTheme.colorScheme.surface
    }
}

@Composable
fun mbExtraLightGrayGrayBlueDarkColor(): Color {
    return when (isSystemInDarkTheme()) {
        true -> MbColor.GrayBlueDarkNight
        else -> MbColor.ExtraLightGray
    }
}
//@Composable
//fun mbExtraLightMustardYellowGrayBlueColor(): Color {
//    return when (isSystemInDarkTheme()) {
//        true -> MbColor.GrayBlueDarkNight
//        else -> MbColor.ExtraLightMustardYellow
//    }
//}

@Composable
fun mbMustardGrayBlueLightDarkColor(): Color {
    return when (isSystemInDarkTheme()) {
        true -> MbColor.GrayBlueDarkNight
        else -> MbColor.DarkMustardYellow
    }
}

@Composable
fun mbGrayLightExtraBlueDarkColor(): Color {
    return when (isSystemInDarkTheme()) {
        true -> MbColor.BlueBlackExtraDark
        else -> MbColor.White
    }
}

@Composable
fun mbActionBookmarkCardBackgroundColors(): Color {
    return when (isSystemInDarkTheme()) {
        true -> MbColor.GrayBlueDarkNight
        else -> MbColor.UltraLightGray
    }
}

@Composable
fun mbErrorBookmarkCardBackgroundColors(): Color {
    return when (isSystemInDarkTheme()) {
        true -> MbColor.RedVermilion
        else -> MbColor.RedVermilion
    }
}

@Composable
fun mbSuccessBookmarkCardBackgroundColors(): Color {
    return when (isSystemInDarkTheme()) {
        true -> MbColor.LightGreenRubin
        else -> MbColor.LightGreenRubin
    }
}

@Composable
fun mbNavBarBackground(): Color = mbExtraLightGrayGrayBlueDarkColor()//color navbar

@Composable
fun mbWhiteDarkColor(): Color {
    return when (isSystemInDarkTheme()) {
        true -> MbColor.White
        else -> MbColor.DarkGray
    }
}

@Composable
fun mbChipRoundedCornerShape() = RoundedCornerShape(
    size = Dimen.mbChipRoundCornerSize
)

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
    Font(R.font.comfortaa_regular)//yantramanav_regular)
)
val MbYantramanavBoldFontFamily = FontFamily(
    Font(R.font.comfortaa_bold)//yantramanav_bold)
)
val MbYantramanavLightFontFamily = FontFamily(
    Font(R.font.comfortaa_light)//yantramanav_light)
)
val MbYantramanavThinFontFamily = FontFamily(
    Font(R.font.comfortaa_light)//yantramanav_thin)
)
//---------------------FONT FAMILY

