package com.application.dev.david.materialbookmarkkot.ui

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.NavHostFragment
import com.application.dev.david.materialbookmarkkot.OnFragmentInteractionListener
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.ui.mbMaterialSearchView.MbMaterialSearchView
import kotlinx.android.synthetic.main.activity_main.*

class BookmarkListActivity : AppCompatActivity(), OnFragmentInteractionListener, LifecycleOwner {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val host = NavHostFragment.create(R.navigation.kmb_main_navigation)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, host).setPrimaryNavigationFragment(host).commit()
    }

    override fun showActionBarView(title: String) {
        setSupportActionBar(mbToolbarId)
        mbMaterialSearchVIewId?.visibility = GONE
        mbToolbarId.title = title
        mbToolbarId.visibility = VISIBLE
    }

    override fun showSearchView() {
        setSupportActionBar(mbToolbarId)
        mbToolbarId.visibility = GONE
        mbMaterialSearchVIewId?.addListener(MbMaterialSearchView.SearchViewSearchListener {
            fun onSearch(@NonNull searchTerm: String) {
                Toast.makeText(this, searchTerm, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
