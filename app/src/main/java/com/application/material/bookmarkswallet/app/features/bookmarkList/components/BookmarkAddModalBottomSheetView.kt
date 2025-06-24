package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.application.material.bookmarkswallet.app.features.searchBookmark.components.WevBaseBottomSheetView
import com.application.material.bookmarkswallet.app.ui.style.expandedBottomSheetState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkAddModalBottomSheetView(
    modifier: Modifier = Modifier,
    bottomSheetVisible: MutableState<Boolean>,
    content: @Composable () -> Unit
) {
    val bottomSheetState = expandedBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    if (bottomSheetVisible.value) {
        WevBaseBottomSheetView(
            modifier = modifier
                .wrapContentHeight()
                .padding(top = 120.dp),
            bottomSheetState = bottomSheetState,
            hasDragHandle = true,
            onCloseCallback = {
                bottomSheetVisible.value = false
                coroutineScope.launch {
                    bottomSheetState.hide()
                }
            }
        ) {
            content()
        }
    }
}