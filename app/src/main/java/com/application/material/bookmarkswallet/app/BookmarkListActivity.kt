package com.application.material.bookmarkswallet.app

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.NavHostFragment

class BookmarkListActivity : AppCompatActivity(), LifecycleOwner, OnFragmentInteractionListener {

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

    override fun onFragmentInteraction(uri: Uri) {
    }

}
