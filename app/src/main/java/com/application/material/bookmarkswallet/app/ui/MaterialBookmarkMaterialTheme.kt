package com.application.material.bookmarkswallet.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MaterialBookmarkMaterialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    rememberSystemUiController()
        .also {
            it.setStatusBarColor(
                color = when {
                    darkTheme -> MbColor.GrayBlueDarkNight
                    else -> MbColor.White
                }
            )
            it.setNavigationBarColor(
                color = when {
                    darkTheme -> MbColor.GrayBlueDarkNight
                    else -> MbColor.LemonYellowTertiary
                }
            )
        }
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}
