package com.application.material.bookmarkswallet.app.ui.style

import androidx.compose.ui.graphics.Color

object MbColor {
    val Gray = Color(0xFF4A4D4E)
    val Yellow = Color(0xFFEED500)
    val LightLemonYellow = Color(0xFFFFF287)
    val LemonYellowTertiary = Color(0xFFE5DEA5)
    val LemonYellowQuater = Color(0xFFFFF9CB)
    val DarkLemonYellow = Color(0xFF8F8013) //C7AF00) //8F8013)//
    val RedVermillion = Color(0xFFC4133D)
    val LightGray = Color(0xFFBEBEBE)
    val DarkGray: Color = Color(0xFF262B2B)
    val DarkGray2: Color = Color(0xFF121316)
    val UltraLightGray: Color = Color(0xFFEEEEEE)
    val White: Color = Color.White
    val ExtraLightGray: Color = Color(0xFFF8F8F8)
    val Black: Color = Color.Black

    //new colors
    val BlueBlackExtraDark: Color = Color(0xFF1E243A)
    val GrayBlueDarkNight: Color = Color(0xFF181C21)
    val GrayBlueMiddleSea: Color = Color(0xFF434E68)
}

//light theme
object LightTheme {
    val mdThemePrimary = MbColor.Gray
    val mdThemeOnPrimary = Color(0xFFFFFFFF)
    val mdThemePrimaryContainer = Color(0xFFC7F089)
    val mdThemeOnPrimaryContainer = Color(0xFF476810)
    val mdThemeSurfaceVariant = Color(0xFFffffff)
}

//light theme
object DarkTheme {
    val mdThemePrimary = MbColor.Gray
    val mdThemeOnPrimary = Color(0xFF213600)
    val mdThemePrimaryContainer = Color(0xFF213600)
    val mdThemeOnPrimaryContainer = Color(0xFF324F00)
    val mdThemeSurfaceVariant = Color(0xFF4A4D4E)
}
