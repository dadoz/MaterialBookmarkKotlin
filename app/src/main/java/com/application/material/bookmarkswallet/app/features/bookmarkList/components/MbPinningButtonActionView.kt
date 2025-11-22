package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.Bookmark
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkActionTypeEnum.PIN_ACTION
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbActionButtonIconColor
import com.application.material.bookmarkswallet.app.utils.ZERO
import java.util.Date

@Composable
fun MbPinningButtonActionView(
    modifier: Modifier = Modifier,
    bookmark: Bookmark,
    isSelected: Boolean = false,
    onPinningAction: ((Bookmark) -> Unit)? = null
) {
    //ui state
    val isSelectedState = remember {
        mutableStateOf(
            value = isSelected
        )
    }
    //component
    MbActionBoxButtonView(
        modifier = modifier
            .padding(
                top = Dimen.paddingMedium16dp
            ),
        isSelected = isSelectedState.value,
        onClickAction = {
            //only ui check
            isSelectedState.value = isSelectedState.value.not()
            //handle call to change state on BE
            when (actionPinBookmark.first) {
                PIN_ACTION -> onPinningAction?.invoke(
                    bookmark
                )

                else -> {}
            }
        },
    ) {
        Icon(
            modifier = Modifier
                .size(size = Dimen.sizeLarge32dp),
            painter = painterResource(id = actionPinBookmark.second),
            tint = mbActionButtonIconColor(
                isSelected = isSelected
            ),
            contentDescription = ""
        )
    }
}

@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MbPinningButtonActionViewPreview() {
    MaterialBookmarkMaterialTheme {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(Dimen.paddingMedium16dp),
        ) {
            MbPinningButtonActionView(
                modifier = Modifier,
                bookmark = bookmarkMock,
                isSelected = true
            )
            MbPinningButtonActionView(
                modifier = Modifier,
                bookmark = bookmarkMock,
                isSelected = false
            )
        }
    }
}

val bookmarkMock = Bookmark(
    siteName = "Google",
    title = "Google",
    iconUrl = "https://blbla.icon",
    appId = "1",
    url = "https://www.google.it",
    timestamp = Date(),
    isPinned = false
)
