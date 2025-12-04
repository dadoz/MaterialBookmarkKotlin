package com.application.material.bookmarkswallet.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MaterialBookmarkMaterialTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}
