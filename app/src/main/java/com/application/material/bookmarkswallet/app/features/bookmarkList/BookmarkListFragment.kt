package com.application.material.bookmarkswallet.app.features.bookmarkList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.application.BookmarkApplication
import com.application.material.bookmarkswallet.app.databinding.FragmentBookmarkListBinding
import com.application.material.bookmarkswallet.app.features.bookmarkList.adapter.BookmarkListPagerAdapter
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_DEFAULT_VIEW
import com.application.material.bookmarkswallet.app.models.BookmarkFilter.StarFilterTypeEnum.IS_STAR_VIEW
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BookmarkListFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentBookmarkListBinding

    //TODO i'm not using in purpose ---- pattern but application
    private val bookmarkFilters by lazy {
        (activity?.application as BookmarkApplication).bookmarkFilters
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentBookmarkListBinding.inflate(inflater, container, false)
        .also {
            requireActivity().addMenuProvider(this@BookmarkListFragment, viewLifecycleOwner)
        }
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

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_bookmark_list, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        TODO("Not yet implemented")
    }

    /**
     *
     */
    private fun onInitSearchView() {
//        mbMaterialSearchVIewId.addListener(MaterialSearchView.SearchViewSearchListener {
//            bookmarkViewModel.searchBookmarkByTitle(bookmarkFilter= bookmarkFilters, query= it)
//        })
    }

    override fun onResume() {
        super.onResume()
        (binding.mbMaterialBookmarkViewPagerId.adapter as BookmarkListPagerAdapter).initFragmentList()
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
        activity
            ?.let {
                binding.mbMaterialBookmarkViewPagerId
                    .apply {
                        isUserInputEnabled = false
                        adapter =
                            BookmarkListPagerAdapter(it)
                    }
            }

        binding.mbBookmarkAddNewButtonId.setOnClickListener {
            bookmarkFilters.starFilterType = IS_DEFAULT_VIEW
            findNavController().navigate(R.id.searchBookmarkFragment)
        }
    }

}
