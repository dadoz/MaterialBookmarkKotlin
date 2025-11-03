package com.application.material.bookmarkswallet.app.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbBasicCardBackgroundColors

/**
 * old BookmarkPreviewCard
 */
@Composable
fun MbCardView(
    modifier: Modifier,
    roundCornerSize: Dp = Dimen.mbCardRoundCornerSize,
    colors: CardColors = mbBasicCardBackgroundColors(),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(size = roundCornerSize),
        colors = colors
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimen.paddingMedium16dp),
            content = content
        )
    }
}

@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun MbCardViewPreview() {
    MbCardView(
        modifier = Modifier
    ) {
        Text(text = "blalalall al lla lal ldl lasdl")
    }
}