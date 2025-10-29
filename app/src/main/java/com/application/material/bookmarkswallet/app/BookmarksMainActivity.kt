package com.application.material.bookmarkswallet.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.LifecycleOwner
import com.application.material.bookmarkswallet.app.features.hp.HpScaffoldView
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarksMainActivity : ComponentActivity(),
    LifecycleOwner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialBookmarkMaterialTheme {

//                val systemUiController = rememberSystemUiController()
//                DisposableEffect(systemUiController) {
//                    //set status and nav bar color
//                    systemUiController.setSystemBarsColor(
//                        color = MbColor.DarkGray
//                    )
//                    onDispose {}
//                }
                //init hp view
                HpScaffoldView()
            }
        }
    }
}
