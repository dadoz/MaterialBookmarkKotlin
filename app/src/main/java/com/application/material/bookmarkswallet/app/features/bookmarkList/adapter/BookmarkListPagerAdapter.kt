package com.application.material.bookmarkswallet.app.features.bookmarkList.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.application.material.bookmarkswallet.app.features.bookmarkList.pager.BookmarkListPageFragment

const val MB_VIEWPAGER_TYPE = "MB_VIEWPAGER_TYPE"

class BookmarkListPagerAdapter(
    fa: FragmentActivity,
    private val bookmarkAddButtonVisibleCallback: (hasToShow: Boolean) -> Unit
) :
    FragmentStateAdapter(fa) {

    private var fragList: MutableList<Fragment> = mutableListOf()

    override fun getItemCount(): Int = fragList.size

    override fun onBindViewHolder(
        holder: FragmentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun createFragment(position: Int): Fragment = fragList[position]

    fun initFragmentList() {
        fragList =
            mutableListOf(BookmarkListPageFragment(bookmarkAddButtonVisibleCallback = bookmarkAddButtonVisibleCallback)
                .apply {
                    arguments = Bundle()
                        .apply { putInt(MB_VIEWPAGER_TYPE, 0) }
                })
    }
}