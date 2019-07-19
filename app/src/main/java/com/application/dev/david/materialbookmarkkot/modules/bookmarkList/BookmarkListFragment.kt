package com.application.dev.david.materialbookmarkkot.modules.bookmarkList

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.application.dev.david.materialbookmarkkot.OnFragmentInteractionListener
import com.application.dev.david.materialbookmarkkot.R
import kotlinx.android.synthetic.main.fragment_bookmark_list.*
import androidx.lifecycle.ViewModelProviders
import com.application.dev.david.materialbookmarkkot.modules.bookmarkList.BookmarkListAdapter.*
import com.application.dev.david.materialbookmarkkot.viewModels.BookmarkViewModel


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BookmarkListFragment : Fragment()  {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookmark_list, container, false)
    }

    /**
     * init view app
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        val bookmarkViewModel = ViewModelProviders.of(this).get(BookmarkViewModel::class.java)
        bookmarkViewModel.retrieveBookmarkList()
        bookmarkViewModel.bookmarksLiveData.observe(this, Observer { list ->
            mbBookmarkRecyclerViewId.layoutManager = GridLayoutManager(context, 2)
            mbBookmarkRecyclerViewId.adapter = BookmarkListAdapter(list, object : OnBookmarkItemClickListener {
                override fun onBookmarkItemClicked(view: View) {
                    Toast.makeText(context, "hey you clicked", Toast.LENGTH_LONG).show()
                }
            })
        })

    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookmarkListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookmarkListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
