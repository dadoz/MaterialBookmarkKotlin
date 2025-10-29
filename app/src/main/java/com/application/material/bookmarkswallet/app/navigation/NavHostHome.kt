package com.application.material.bookmarkswallet.app.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.application.material.bookmarkswallet.app.features.bookmarkList.BookmarkListView
import com.application.material.bookmarkswallet.app.features.settings.SettingsView

enum class NavRoute(val route: String) {
    BookmarkList(route = "bookmark_list"),
    Settings(route = "settings")
}

@Composable
fun HomeNavHost(
    modifier: Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.BookmarkList.route,
        modifier = modifier
            .fillMaxSize()
    ) {
        //home screen
        composable(route = NavRoute.BookmarkList.route) {
            BookmarkListView()
        }

        composable(route = NavRoute.Settings.route) {
            SettingsView(
                modifier = Modifier
            )
        }
    }
}
