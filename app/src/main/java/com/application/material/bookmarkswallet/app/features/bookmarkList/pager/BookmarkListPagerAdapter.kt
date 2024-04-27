package com.application.material.bookmarkswallet.app.features.bookmarkList.pager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class BookmarkListPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private val NUM_PAGES = 1
    lateinit var currentFragment: Fragment

    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                currentFragment = BookmarkListPageFragment().apply {
                    arguments = Bundle().apply { putInt("MB_VIEWPAGER_TYPE", 0) }
                }
            }
            else -> {
                currentFragment = BookmarkListPageFragment().apply {
                    arguments = Bundle().apply { putInt("MB_VIEWPAGER_TYPE", 1) }
                }
            }
        }
        return currentFragment
    }
}