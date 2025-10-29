package com.application.material.bookmarkswallet.app.ui.components

import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.application.material.bookmarkswallet.app.ui.style.Dimen

@Composable
fun MbLoaderView(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier
            .width(
                width = Dimen.size32dp
            ),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}