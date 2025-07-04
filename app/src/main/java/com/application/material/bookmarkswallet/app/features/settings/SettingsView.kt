package com.application.material.bookmarkswallet.app.features.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.USER_MOCK
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.UserLoginCardView
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.User
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbTitleBoldTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleMediumBoldTextStyle
val USER_MOCK = User(
    name = "davide",
    surname = "bllalal",
    username = "blla",
    age = 40
)

@Composable
fun SettingsView() {
    val user by remember {
        mutableStateOf(
            value = USER_MOCK
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(all = Dimen.paddingMedium16dp),
            verticalArrangement = Arrangement.spacedBy(Dimen.paddingMedium16dp)
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier
                        .padding(end = Dimen.paddingMedium16dp),
                    style = mbTitleBoldTextStyle(),
                    text = stringResource(R.string.settings),
                )
                UserLoginCardView(
                    modifier = Modifier,
                    user = user
                )
            }

            MbCardView(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                //title
                Text(
                    modifier = Modifier
                        .align(
                            alignment = Alignment.CenterHorizontally
                        )
                        .padding(bottom = Dimen.paddingSmall8dp),
                    style = mbTitleMediumBoldTextStyle(),
                    maxLines = 2,
                    text = "Setting 1"
                )
            }

            MbCardView(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                //title
                Text(
                    modifier = Modifier
                        .align(
                            alignment = Alignment.CenterHorizontally
                        )
                        .padding(bottom = Dimen.paddingSmall8dp),
                    style = mbTitleMediumBoldTextStyle(),
                    maxLines = 2,
                    text = "Setting 2"
                )
            }
        }
    }
}