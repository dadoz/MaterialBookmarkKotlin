package com.application.material.bookmarkswallet.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.LifecycleOwner
import com.application.material.bookmarkswallet.app.features.hp.HpScaffoldView
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BookmarksMainActivity : ComponentActivity(),
    LifecycleOwner {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        authAnonymousWithFirebase()
        setContent {
            MaterialBookmarkMaterialTheme {
                //init hp view
                HpScaffoldView()
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val user = auth.currentUser
        Timber.e(
            user?.email + " - " + user?.uid
        )
    }

    fun authAnonymousWithFirebase() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.e( "signInAnonymously:success")
                    val user = auth.currentUser
                    Timber.e(
                        user?.email + " - " + user?.uid
                    )
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.e( "signInAnonymously:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    Timber.e(
                        "FAILED TO LOAD USER"
                    )
                }
            }

    }
}
