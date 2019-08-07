package com.application.dev.david.materialbookmarkkot.modules.bookmarkWebView

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.application.dev.david.materialbookmarkkot.R
import kotlinx.android.synthetic.main.fragment_bookmark_url_web_view.*
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebView



/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BookmarkUrlWebViewFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [BookmarkUrlWebViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class BookmarkUrlWebViewFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    val args: BookmarkUrlWebViewFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark_url_web_view, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException("$context must implement OnFragmentInteractionListener")
//        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mbBookmarkUrlWebViewId.webViewClient = object: WebViewClient() {
            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.proceed()
            }
        }
        mbBookmarkUrlWebViewId.settings.javaScriptEnabled = true
        val url = "https://${args.BookmarkUrl}"
        mbBookmarkUrlWebViewId.loadUrl(url)
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }
}
