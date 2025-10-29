package com.application.material.bookmarkswallet.app.features.hp.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.application.material.bookmarkswallet.app.features.hp.configurator.TabItem
import com.application.material.bookmarkswallet.app.features.hp.configurator.getTabMenuItemList
import com.application.material.bookmarkswallet.app.navigation.NavRoute
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbNavBarBackground
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleLightYellowTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTabIconColor
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonLightMustardDarkColor

@Composable
fun MbNavigationBar(
    modifier: Modifier,
    containerColor: Color = mbNavBarBackground(),
    navItemSelectedState: MutableState<NavRoute>,
    onNavItemClickAction: (TabItem) -> Unit,
) {
    val context = LocalContext.current

    NavigationBar(
        modifier = modifier,
        containerColor = containerColor
    ) {
        getTabMenuItemList(
            context = context
        )
            .forEach {
                //check is selected
                val isSelected = it.navRoute == navItemSelectedState.value

                NavigationBarItem(
                    modifier = Modifier,
                    colors = NavigationBarItemDefaults
                        .colors(
                            indicatorColor = mbYellowLemonLightMustardDarkColor()
                        ),
                    icon = {
                        Icon(
                            modifier = Modifier
                                .size(size = Dimen.sizeMedium24dp),
                            painter = painterResource(
                                id = it.icon
                            ),
                            contentDescription = "item",
                            tint = mbTabIconColor(
                                isSelected = isSelected
                            )
                        )
                    },
                    label = {
                        Text(
                            text = it.label,
                            style = mbSubtitleLightYellowTextStyle()
                        )
                    },
                    selected = isSelected,
                    onClick = {
                        onNavItemClickAction.invoke(it)
                    }
                )
            }
    }
}