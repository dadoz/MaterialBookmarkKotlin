package com.application.dev.david.materialbookmarkkot.modules.bookmarkList

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.View.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.application.dev.david.materialbookmarkkot.OnFragmentInteractionListener
import com.application.dev.david.materialbookmarkkot.R
import kotlinx.android.synthetic.main.fragment_bookmark_list.*
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import com.application.dev.david.materialbookmarkkot.modules.bookmarkList.BookmarkListAdapter.*
import com.application.dev.david.materialbookmarkkot.viewModels.BookmarkViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.preview_bookmark_view.*


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
//        (activity as AppCompatActivity).setSupportActionBar(mbToolbarListId)
//        val view: View = MbMaterialSearchView(context)
//        mbToolbarListId.addView(view)
    }

    /**
     *
     */
    private fun initView() {
        val bookmarkViewModel = ViewModelProviders.of(this).get(BookmarkViewModel::class.java)
        bookmarkViewModel.retrieveBookmarkList()
        //event retrieve list
        bookmarkViewModel.bookmarksLiveData.observe(this, Observer { list ->
            mbBookmarkRecyclerViewId.layoutManager = GridLayoutManager(context, 2)

            mbBookmarkRecyclerViewId.adapter = BookmarkListAdapter(list, object : OnBookmarkItemClickListener {
                override fun onBookmarkItemClicked(position: Int, bookmark : Bookmark) {
                    BottomSheetBehavior.from(mbBookmarkPreviewCardviewId).state = STATE_EXPANDED
                }
            })

            //please replace with basic manager
            when (list.isNotEmpty()) {
                true -> {
                    mbBookmarkEmptyViewId.visibility =  GONE
                    mbBookmarkHeaderLayoutId.visibility =  VISIBLE
                }
                false -> {
                    mbBookmarkEmptyViewId.visibility =  VISIBLE
                    mbBookmarkHeaderLayoutId.visibility =  GONE
                }
            }

        })

        mbBookmarkEmptyAddNewButtonId.setOnClickListener {
            findNavController().navigate(R.id.searchBookmarkFragment)
        }

        mbBookmarkAddNewButtonId.setOnClickListener {
            findNavController().navigate(R.id.searchBookmarkFragment)
        }

        mbBookmarkHeaderListFilterIconId.setOnClickListener {
            (mbBookmarkRecyclerViewId.layoutManager as GridLayoutManager).spanCount = 1
            mbBookmarkRecyclerViewId.adapter?.notifyDataSetChanged()
            mbBookmarkHeaderCardFilterIconId.visibility = VISIBLE
            it.visibility = INVISIBLE
        }
        mbBookmarkHeaderCardFilterIconId.setOnClickListener {
            (mbBookmarkRecyclerViewId.layoutManager as GridLayoutManager).spanCount = 2
            mbBookmarkRecyclerViewId.adapter?.notifyDataSetChanged()
            mbBookmarkHeaderListFilterIconId.visibility = VISIBLE
            it.visibility = GONE
        }
        mbBookmarkHeaderStarFilterIconId.setOnClickListener {
            Toast.makeText(context, "hey you clicked Bla", Toast.LENGTH_LONG).show()
        }
        mbBookmarkHeaderSortFilterIconId.setOnClickListener {
            Toast.makeText(context, "hey you clicked Bla", Toast.LENGTH_LONG).show()
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
