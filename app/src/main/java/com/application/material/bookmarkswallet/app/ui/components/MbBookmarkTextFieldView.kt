package com.application.material.bookmarkswallet.app.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.searchBookmark.components.BookmarkModalBottomSheetView
import com.application.material.bookmarkswallet.app.ui.style.mbCardRoundedCornerShape
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle

@Composable
fun MbBookmarkTextFieldView(
    modifier: Modifier,
    textLabel: String = stringResource(id = R.string.bookmark_url),
    searchUrlTextState: MutableState<TextFieldValue>
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        textStyle = mbSubtitleTextStyle(),
        shape = mbCardRoundedCornerShape(),
        value = searchUrlTextState.value,
        placeholder = {
            Text(
                modifier = Modifier,
                style = mbSubtitleTextStyle(),
                text = textLabel
            )
        },
//        colors = OutlinedTextFieldDefaults.colors().copy(
//            textSelectionColors = TextSelectionColors(
//                handleColor = MbColor.Black,
//                backgroundColor = MbColor.Black
//            )
//        ),
//        label = {
//            Text(
//                modifier = Modifier,
//                style = mbSubtitleTextStyle(),
//                text = textLabel
//            )
//        },
        onValueChange = {
            searchUrlTextState.value = it
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun BookmarkModalView2Preview() {
    BookmarkModalBottomSheetView(
        modifier = Modifier,
        bottomSheetState = rememberModalBottomSheetState(),
        onCloseCallback = { },
        url = ""
    )
}
