package com.application.material.bookmarkswallet.app.features.bookmarkList.pager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

const val MB_VIEWPAGER_TYPE = "MB_VIEWPAGER_TYPE"

class BookmarkListPagerAdapter(
    fa: FragmentActivity,
    private val bookmarkAddButtonVisibleCallback: (hasToShow: Boolean) -> Unit
) :
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment =
        BookmarkListPageFragment(bookmarkAddButtonVisibleCallback = bookmarkAddButtonVisibleCallback)
            .apply {
                arguments = Bundle()
                    .apply { putInt(MB_VIEWPAGER_TYPE, position) }
            }
}