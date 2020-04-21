package com.application.dev.david.materialbookmarkkot.modules.bookmarkList

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.annotation.Px
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ScrollState
import com.application.dev.david.materialbookmarkkot.OnFragmentInteractionListener
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.application.BookmarkApplication
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.StarFilterTypeEnum.IS_DEFAULT_VIEW
import com.application.dev.david.materialbookmarkkot.models.BookmarkFilter.StarFilterTypeEnum.IS_STAR_VIEW
import com.application.dev.david.materialbookmarkkot.modules.bookmarkList.pager.BookmarkListPageFragment
import com.application.dev.david.materialbookmarkkot.modules.bookmarkList.pager.BookmarkListPagerAdapter
import kotlinx.android.synthetic.main.fragment_bookmark_list.*

class BookmarkListFragment : Fragment()  {
    private var listener: OnFragmentInteractionListener? = null
    //TODO i'm not using in purpose ---- pattern but application
    private val bookmarkFilters by lazy {
        (activity?.application as BookmarkApplication).bookmarkFilters
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bookmark_list, container, false)
    }

    /**
     * init view app
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInitSearchView()
        onInitAppBarMenu()
        initView()
    }

    /**
     *
     */
    private fun onInitSearchView() {
//        mbMaterialSearchVIewId.addListener(MaterialSearchView.SearchViewSearchListener {
//            bookmarkViewModel.searchBookmarkByTitle(bookmarkFilter= bookmarkFilters, query= it)
//        })

    }
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_bookmark_list, menu)
    }


    /**
     *
     */
    private fun onInitAppBarMenu() {
        mbBookmarkBottomBarLayoutId.apply {
            replaceMenu(R.menu.menu_bookmark_list)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.mbBookmarkHeaderHomeFilterIconId -> {
                        bookmarkFilters.starFilterType = IS_DEFAULT_VIEW
                        mbMaterialBookmarkViewPagerId.currentItem = 0
                    }
                    R.id.mbBookmarkHeaderStarFilterIconId -> {
                        bookmarkFilters.starFilterType = IS_STAR_VIEW
                        mbMaterialBookmarkViewPagerId.currentItem = 1
                    }
                }
                true
            }
        }
    }

    fun initView() {
        activity?.let {
            mbMaterialBookmarkViewPagerId.adapter =
                BookmarkListPagerAdapter(it)
        }

        mbMaterialBookmarkViewPagerId.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float,
                @Px positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(@ScrollState state: Int) {}
        })
        mbBookmarkAddNewButtonId.setOnClickListener {
            bookmarkFilters.starFilterType = IS_DEFAULT_VIEW
            findNavController().navigate(R.id.searchBookmarkFragment)
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