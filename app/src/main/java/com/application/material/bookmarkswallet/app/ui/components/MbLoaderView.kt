package com.application.material.bookmarkswallet.app.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.MbColor

@Composable
fun MbLoaderView(
    modifier: Modifier = Modifier,
    size: Dp = Dimen.size48dp
) {
    Box(
        modifier = modifier
            .padding(
                all = Dimen.paddingMedium16dp
            )
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(
                    size = size
                )
                .align(
                    alignment = Alignment.Center
                ),
            strokeWidth = 8.dp,
            color = MbColor.Yellow
        )
    }
}

@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun MbLoaderViewPreview(
    modifier: Modifier = Modifier
) {
    MaterialBookmarkMaterialTheme {
        MbLoaderView(
            modifier = modifier
        )
    }
}
