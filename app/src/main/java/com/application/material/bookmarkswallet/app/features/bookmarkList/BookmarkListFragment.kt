package com.application.material.bookmarkswallet.app.features.bookmarkList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ScrollState
import com.application.material.bookmarkswallet.app.OnFragmentInteractionListener
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.application.BookmarkApplication
import com.application.material.bookmarkswallet.app.databinding.FragmentBookmarkListBinding
import com.application.material.bookmarkswallet.app.features.bookmarkList.pager.BookmarkListPagerAdapter
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_DEFAULT_VIEW
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_STAR_VIEW

class BookmarkListFragment : Fragment() {
    private lateinit var binding: FragmentBookmarkListBinding
    private var listener: OnFragmentInteractionListener? = null

    //TODO i'm not using in purpose ---- pattern but application
    private val bookmarkFilters by lazy {
        (activity?.application as BookmarkApplication).bookmarkFilters
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentBookmarkListBinding.inflate(inflater, container, false)
        .also {
            binding = it
        }.root

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

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_bookmark_list, menu)
    }

    /**
     *
     */
    private fun onInitAppBarMenu() {
        binding.mbBookmarkBottomBarLayoutId.apply {
            replaceMenu(R.menu.menu_bookmark_list)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.mbBookmarkHeaderHomeFilterIconId -> {
                        bookmarkFilters.starFilterType = IS_DEFAULT_VIEW
                        binding.mbMaterialBookmarkViewPagerId.currentItem = 0
                    }

                    R.id.mbBookmarkHeaderStarFilterIconId -> {
                        bookmarkFilters.starFilterType = IS_STAR_VIEW
                        binding.mbMaterialBookmarkViewPagerId.currentItem = 1
                    }
                }
                true
            }
        }
    }

    private fun initView() {
        activity?.let {
            binding.mbMaterialBookmarkViewPagerId.apply {
                isUserInputEnabled = false
                adapter = BookmarkListPagerAdapter(it) {
                    binding.mbBookmarkAddNewButtonId.visibility =
                        it.takeIf { it }?.let { View.VISIBLE } ?: View.GONE
                }
            }
        }

        binding.mbMaterialBookmarkViewPagerId.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int, positionOffset: Float,
                @Px positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(@ScrollState state: Int) {}
        })
        binding.mbBookmarkAddNewButtonId.setOnClickListener {
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
