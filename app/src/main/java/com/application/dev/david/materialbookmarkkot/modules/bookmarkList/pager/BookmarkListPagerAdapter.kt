package com.application.dev.david.materialbookmarkkot.modules.bookmarkList.pager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.dev.david.materialbookmarkkot.modules.searchBookmark.SearchBookmarkFragment

class BookmarkListPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private val NUM_PAGES = 2
    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> {
                BookmarkListPageFragment().apply {
                    arguments = Bundle().apply { putInt("MB_VIEWPAGER_TYPE", 0) }
                }
            }
            else -> {
                BookmarkListPageFragment().apply {
                    arguments = Bundle().apply { putInt("MB_VIEWPAGER_TYPE", 1) }
                }
            }
        }
}