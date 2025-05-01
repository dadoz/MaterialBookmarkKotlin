package com.application.material.bookmarkswallet.app.features.searchBookmark.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.application.material.bookmarkswallet.app.ui.style.expandedBottomSheetState
import com.application.material.bookmarkswallet.app.ui.style.getResizedDensity
import com.application.material.bookmarkswallet.app.ui.style.mbBottomSheetRoundedCornerShape
import com.application.material.bookmarkswallet.app.ui.style.mbGrayLightColor
import com.application.material.bookmarkswallet.app.utils.ZERO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkModalBottomSheetView(
    modifier: Modifier,
    bottomSheetState: SheetState,
    url: String,
    onCloseCallback: () -> Unit
) {
    //show modal webview state
//    val coroutineScope = rememberCoroutineScope()
//    var openWebViewSheetState by rememberSaveable { mutableStateOf(false) }
//    val webViewSheetState = expandedBottomSheetState()
//    val showSupportWebViewSheet: (hasToShow: Boolean) -> Unit = {
//        coroutineScope.launch {
//            openWebViewSheetState = it
//            //show hide webview
//            when (openWebViewSheetState) {
//                true -> webViewSheetState.show()
//                else -> webViewSheetState.hide()
//            }
//        }
//    }
//    if (hasToOpen) {
    WevBaseBottomSheetView(
        modifier = modifier
            .wrapContentHeight()
            .padding(top = 120.dp),
        hasDragHandle = true,
        bottomSheetState = bottomSheetState,
        onCloseCallback = onCloseCallback
    ) {
        //show web view modal
        WevWebView(
            modifier = Modifier,
            url = url
        )
    }
//    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WevWebView(
    modifier: Modifier,
    url: String
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    AndroidView(
        modifier = modifier
            .verticalScroll(scrollState),
        factory = {
            WebView(it)
                .apply {
                    //params
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    //webclient
                    webViewClient =
                        getWebViewClient(coroutineScope = coroutineScope, scrollState = scrollState)
                    //set js enabled
                    settings.javaScriptEnabled = true
                    //set dom storage enabled
                    settings.domStorageEnabled = true
                    //load url
                    loadUrl(url)
                }
        },
        update = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WevBaseBottomSheetView(
    modifier: Modifier = Modifier,
    hasDragHandle: Boolean = false,
    bottomSheetState: SheetState,
    containerColor: Color = mbGrayLightColor(),
    onCloseCallback: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier,
        sheetState = bottomSheetState,
        onDismissRequest = onCloseCallback,
        containerColor = containerColor,
        shape = mbBottomSheetRoundedCornerShape(),
        dragHandle = {
            if (hasDragHandle) {
                DragHandle()
            }
        }
    ) {
        CompositionLocalProvider(
            LocalDensity provides Density(
                density = LocalDensity.current.getResizedDensity()
            )
        ) {
            content()
        }
    }
}

/**
 * helper to get webView client
 */
fun getWebViewClient(coroutineScope: CoroutineScope, scrollState: ScrollState) =
    object : WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            coroutineScope.launch {
                scrollState.scrollTo(ZERO)
            }
            super.onPageFinished(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            Timber.e("--------- URL Selected from web")
            Timber.e(view.title)
            Timber.e(view.transitionName)
            Timber.e(request.url.toString())
            return false
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun BookmarkModalViewPreview() {
    BookmarkModalBottomSheetView(
        modifier = Modifier,
        bottomSheetState = expandedBottomSheetState(),
        url = "",
        onCloseCallback = {}
    )
}