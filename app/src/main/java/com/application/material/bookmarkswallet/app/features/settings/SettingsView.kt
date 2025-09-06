package com.application.material.bookmarkswallet.app.features.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.USER_MOCK
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.MBExtendedFab
import com.application.material.bookmarkswallet.app.features.bookmarkList.components.UserLoginCardView
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleHExtraBigBoldTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleMediumBoldTextStyle
import com.application.material.bookmarkswallet.app.utils.EMPTY

@Composable
fun SettingsView() {
    val user by remember {
        mutableStateOf(
            value = USER_MOCK
        )
    }
    Column(
        modifier = Modifier
            .verticalScroll(state = rememberScrollState())
            .padding(all = Dimen.paddingMedium16dp),
        verticalArrangement = Arrangement.spacedBy(Dimen.paddingMedium16dp)
    ) {
        Row(
            modifier = Modifier
                .padding(top = Dimen.paddingMedium16dp,
                    bottom = Dimen.paddingMedium16dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                style = mbTitleHExtraBigBoldTextStyle(),
                text = stringResource(R.string.settings),
            )
        }

        UserLoginCardView(
            modifier = Modifier
                .fillMaxWidth(),
            user = user
        )

        MbCardView(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(all = Dimen.paddingMedium16dp),
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
                    text = "Rate on Playstore"
                )
                //title
                Text(
                    modifier = Modifier
                        .align(
                            alignment = Alignment.Start
                        )
                        .padding(bottom = Dimen.paddingSmall8dp),
                    style = mbSubtitleTextStyle(),
                    maxLines = 2,
                    text = "rate us and leave a feedback"
                )
            }

            //row 2 - create a component please
            Column(
                modifier = Modifier
                    .padding(all = Dimen.paddingMedium16dp),
            ) {
                //title
                Text(
                    modifier = Modifier
                        .align(
                            alignment = Alignment.Start
                        )
                        .padding(bottom = Dimen.paddingSmall8dp),
                    style = mbTitleMediumBoldTextStyle(),
                    maxLines = 2,
                    text = "Contacts"
                )
                //title
                Text(
                    modifier = Modifier
                        .align(
                            alignment = Alignment.Start
                        )
                        .padding(bottom = Dimen.paddingSmall8dp),
                    style = mbSubtitleTextStyle(),
                    maxLines = 2,
                    text = "check to send us an email"
                )
            }

            //row 2 - create a component please
            Column(
                modifier = Modifier
                    .padding(all = Dimen.paddingMedium16dp),
            ) {
                //title
                Text(
                    modifier = Modifier
                        .align(
                            alignment = Alignment.Start
                        )
                        .padding(bottom = Dimen.paddingSmall8dp),
                    style = mbTitleMediumBoldTextStyle(),
                    maxLines = 2,
                    text = "App Version"
                )
                //title
                Text(
                    modifier = Modifier
                        .align(
                            alignment = Alignment.Start
                        )
                        .padding(bottom = Dimen.paddingSmall8dp),
                    style = mbSubtitleTextStyle(),
                    maxLines = 2,
                    text = "2.2.0"
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //title
            Text(
                modifier = Modifier
                    .weight(2F)
                    .padding(bottom = Dimen.paddingSmall8dp),
                style = mbSubtitleTextStyle(),
                text = "Just donate a coffee for the developer support",
            )
            MBExtendedFab(
                modifier = Modifier
                    .weight(1F)
                    .padding(top = Dimen.paddingMedium16dp),
                title = "Donate",
                iconRes = R.drawable.ic_tab_user_dark,
                onClickAction = {
                }
            )
        }

        //title
        Text(
            modifier = Modifier
                .align(
                    alignment = Alignment.Start
                )
                .padding(bottom = Dimen.paddingSmall8dp),
            style = mbSubtitleTextStyle(),
            text = "Something more here...\n" +
                    "Thanks for illustration in app due to a dedicated work."
        )
        Image(
            modifier = Modifier
                .size(200.dp)
                .padding(vertical = Dimen.paddingMedium16dp)
                .align(alignment = Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.ic_fox_illustration),
            contentDescription = EMPTY
        )

    }
}
