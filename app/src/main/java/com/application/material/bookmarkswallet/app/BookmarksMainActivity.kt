package com.application.material.bookmarkswallet.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarksMainActivity : AppCompatActivity(),
    LifecycleOwner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //inflate frag
        NavHostFragment.create(R.navigation.kmb_main_navigation)
            .also { host ->
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, host)
                    .setPrimaryNavigationFragment(host)
                    .commit()
            }
    }
}
