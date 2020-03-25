package com.application.dev.david.materialbookmarkkot.modules.bookmarkList

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.View.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.application.dev.david.materialbookmarkkot.OnFragmentInteractionListener
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import com.application.dev.david.materialbookmarkkot.modules.addBookmark.AddBookmarkFragment.Companion.UPDATE_ACTION_BOOKMARK
import com.application.dev.david.materialbookmarkkot.viewModels.BookmarkViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.fragment_bookmark_list.*


class BookmarkListFragment : Fragment()  {
    private var listener: OnFragmentInteractionListener? = null
    private val bookmarkViewModel by lazy {
        ViewModelProviders.of(this).get(BookmarkViewModel::class.java)
    }

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
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
    }

    /**
     *
     */
    @SuppressLint("FragmentLiveDataObserve")
    private fun initView() {
        bookmarkViewModel.retrieveBookmarkList()

        //event retrieve list
        bookmarkViewModel.bookmarksRemovedBookmarkPairData.observe(this, Observer { pairData ->
            val position = pairData.first
            pairData.second?.let { list ->
                mbBookmarkRecyclerViewId.apply {
                    adapter?.let {
                        (it as BookmarkListAdapter).setItems(list)
                        it.notifyItemRemoved(position)
                        it.notifyItemRangeChanged(position, list.size - position)
                    }
                }
            }
        })

        bookmarkViewModel.bookmarksLiveData.observe(this, Observer { list ->
            mbBookmarkRecyclerViewId.apply {
                layoutManager = GridLayoutManager(context, 2)
                adapter = BookmarkListAdapter(list, { position, bookmark ->  openPreviewView(position, bookmark) })
            }

            //please replace with viewModel isEmptyDataList
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

    /**
     *
     */
    private fun openPreviewView(position: Int, bookmark: Bookmark) =
        mbBookmarkPreviewCardviewId.apply {
            initView(BottomSheetBehavior.from(mbBookmarkPreviewCardviewId))
            initData(bookmark, bookmarkViewModel)
            setMoreButtonAction { }

            setDeleteButtonAction {
                bookmarkViewModel.deleteBookmark(bookmark)
                bookmarkViewModel.deleteBookmarkFromList(position)
            }

            actionEditBookmark {
                val action = BookmarkListFragmentDirections
                    .actionBookmarkListFragmentToAddBookmarkFragment(actionType = UPDATE_ACTION_BOOKMARK, bookmark =
                    bundleOf(
                        "bookmark_url" to bookmark.url,
                        "bookmark_title" to bookmark.title,
                        "bookmark_icon_url" to bookmark.image
                    ))
                findNavController().navigate(action)
            }

            actionShareBookmark { intent -> startActivity(intent)}

            actionOpenPreviewBookmark { intent -> startActivity(intent)}
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
