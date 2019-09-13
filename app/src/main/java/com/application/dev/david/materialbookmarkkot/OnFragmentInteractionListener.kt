package com.application.dev.david.materialbookmarkkot

import android.net.Uri

interface OnFragmentInteractionListener {
    fun showSearchView()
    fun showActionBarView(title: String)
    fun onFragmentInteraction(uri: Uri)
}
