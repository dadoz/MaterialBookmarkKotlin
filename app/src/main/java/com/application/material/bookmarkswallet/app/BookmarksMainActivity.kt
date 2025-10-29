package com.application.material.bookmarkswallet.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.LifecycleOwner
import com.application.material.bookmarkswallet.app.features.hp.HpScaffoldView
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarksMainActivity : ComponentActivity(),
    LifecycleOwner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialBookmarkMaterialTheme {
                //init hp view
                HpScaffoldView()
            }
        }
    }
}
