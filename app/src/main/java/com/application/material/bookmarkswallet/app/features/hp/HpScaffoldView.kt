package com.application.material.bookmarkswallet.app.features.hp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.application.material.bookmarkswallet.app.features.hp.components.MbNavigationBar
import com.application.material.bookmarkswallet.app.navigation.HomeNavHost
import com.application.material.bookmarkswallet.app.navigation.NavRoute
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import com.application.material.bookmarkswallet.app.ui.style.mbNavBarBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HpScaffoldView() {
    val context = LocalContext.current
    val navController = rememberNavController()

    //pull to refresh config
    val pullToRefreshState = rememberPullToRefreshState()
    val isLoading = remember { mutableStateOf(value = false) }

    //nav item selected state
    val navItemSelectedState = remember { mutableStateOf(value = NavRoute.BookmarkList) }

    LaunchedEffect(key1 = isLoading.value) {
        if (isLoading.value) {
            launch(Dispatchers.Main) {
                delay(2000L)
                isLoading.value = false
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
//            .windowInsetsPadding(
//                insets = WindowInsets.systemBars
//            ),
        topBar = {
            when (navItemSelectedState.value) {
                NavRoute.BookmarkList -> {}

                else -> {}
            }
        },
        bottomBar = {
            MbNavigationBar(
                modifier = Modifier,
                containerColor = mbNavBarBackground(),
                navItemSelectedState = navItemSelectedState
            ) {
                navController.navigate(route = it.navRoute.route) {
                    //change selected state item
                    navItemSelectedState.value = it.navRoute
                    //popup item from controller
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    restoreState = true
                    launchSingleTop = true
                }
            }
        },
        floatingActionButton = { },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.End,
    ) { innerPadding ->
        //todo move in a component
        PullToRefreshBox(
            modifier = Modifier
                .padding(
                    paddingValues = innerPadding
                )
                .background(
                    color = MbColor.Yellow
//                        brush = homeBackgroundBrushColor()
                )
                .fillMaxSize(),
            isRefreshing = isLoading.value,
            state = pullToRefreshState,
            onRefresh = {
                isLoading.value = true
                //loading
                Toast.makeText(context, "refresh", Toast.LENGTH_SHORT).show()
            },
            indicator = {
                Indicator(
                    modifier = Modifier
                        .padding(
                            top = Dimen.indicatorPaddingTop
                        )
                        .align(
                            alignment = Alignment.TopCenter
                        ),
                    state = pullToRefreshState,
                    isRefreshing = isLoading.value,
                    containerColor = MbColor.DarkMustardYellow,
                    color = MbColor.White,
                )
            }
        ) {
            //pull to refresh content
            HomeNavHost(
                modifier = Modifier
                    .clipToBounds()
                    .fillMaxSize(),
                navController = navController
            )
        }
    }
}
