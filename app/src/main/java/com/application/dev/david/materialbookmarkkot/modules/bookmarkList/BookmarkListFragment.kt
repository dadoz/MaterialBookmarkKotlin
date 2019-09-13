package com.application.dev.david.materialbookmarkkot.modules.bookmarkList

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.application.dev.david.materialbookmarkkot.OnFragmentInteractionListener
import com.application.dev.david.materialbookmarkkot.R
import kotlinx.android.synthetic.main.fragment_bookmark_list.*
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import com.application.dev.david.materialbookmarkkot.modules.bookmarkList.BookmarkListAdapter.*
import com.application.dev.david.materialbookmarkkot.ui.mbMaterialSearchView.MbMaterialSearchView
import com.application.dev.david.materialbookmarkkot.viewModels.BookmarkViewModel
import kotlinx.android.synthetic.main.activity_main.*


class BookmarkListFragment : Fragment()  {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        initActionBar()
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
    }

    /**
     * init actionbar
     */
    private fun initActionBar() {
        listener?.showSearchView()
    }

    /**
     *
     */
    private fun initView() {
        val bookmarkViewModel = ViewModelProviders.of(this).get(BookmarkViewModel::class.java)
        bookmarkViewModel.retrieveBookmarkList()
        bookmarkViewModel.bookmarksLiveData.observe(this, Observer { list ->
            mbBookmarkRecyclerViewId.layoutManager = GridLayoutManager(context, 2)
            mbBookmarkRecyclerViewId.adapter = BookmarkListAdapter(list, object : OnBookmarkItemClickListener {
                override fun onBookmarkItemClicked(position: Int, bookmark : Bookmark) {
                    Toast.makeText(context, "hey you clicked $position ${bookmark.url}", Toast.LENGTH_LONG).show()
                    val action = BookmarkListFragmentDirections.setBookmarkUrlArgsActionId(bookmark.url)
                    findNavController().navigate(action)
                }
            })
        })
        mbBookmarkAddNewButtonId.setOnClickListener {
            findNavController().navigate(R.id.addBookmarkFragment)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
