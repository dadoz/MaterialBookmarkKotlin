package com.application.material.bookmarkswallet.app.features.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.application.material.bookmarkswallet.app.R

@Composable
fun SettingsView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = stringResource(R.string.settings))
    }
}