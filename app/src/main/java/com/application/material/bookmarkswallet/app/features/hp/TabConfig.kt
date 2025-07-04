package com.application.material.bookmarkswallet.app.features.hp

import android.content.Context
import androidx.annotation.Keep
import com.application.material.bookmarkswallet.app.R

fun getTabMenuItemList(context: Context) = listOf<TabItem>(
    TabItem(
        label = context.getString(R.string.menu_tab_bookmark_list),
        icon = R.drawable.ic_tab_bookmark,
        navRoute = NavRoute.BookmarkList
    ),
    TabItem(
        label = context.getString(R.string.menu_tab_settings),
        icon = R.drawable.ic_tab_settings,
        navRoute = NavRoute.Settings
    )
)

@Keep
data class TabItem(
    val label: String,
    val icon: Int,
    val navRoute: NavRoute
)
