package com.application.dev.david.materialbookmarkkot.modules.bookmarkList

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.dev.david.materialbookmarkkot.OnFragmentInteractionListener
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import khronos.toDate
import khronos.toString
import kotlinx.android.synthetic.main.fragment_bookmark_list.*
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BookmarkListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    val list : ArrayList<Bookmark> = ArrayList()

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
//        navigateBookmarkSendButtonId.setOnClickListener { findNavController().navigate(R.id.addBookmarkFragment) }
        list.add(Bookmark("bll", "Instagram", "Image", "", "www.instagram.com", "2019-06-19".toDate("yyyy-MM-dd")))
        list.add(Bookmark("bll", "Facebook", "Image", "", "www.facebook.com", "2019-05-01".toDate("yyyy-MM-dd")))
        list.add(Bookmark("bll", "Google", "Image", "", "www.google.com", "2019-02-11".toDate("yyyy-MM-dd")))
        list.add(Bookmark("bll", "Vodafone", "Image", "", "www.vodafone.com", "2019-04-22".toDate("yyyy-MM-dd")))
        list.add(Bookmark("bll", "Outlook", "Image", "", "www.outlook.it", "2019-04-30".toDate("yyyy-MM-dd")))
        list.add(Bookmark("bll", "Gmail", "Image", "", "www.gmail.com", "2019-05-18".toDate("yyyy-MM-dd")))
        list.add(Bookmark("bll", "Blablacar", "Image", "", "www.blablacar.com", "2019-05-12".toDate("yyyy-MM-dd")))

        mbBookmarkRecyclerViewId.layoutManager = GridLayoutManager(context, 2)
        mbBookmarkRecyclerViewId.adapter = BookmarkListAdapter(list)
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
