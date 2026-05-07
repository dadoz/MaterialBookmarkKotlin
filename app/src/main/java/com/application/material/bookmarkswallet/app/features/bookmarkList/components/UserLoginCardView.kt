package com.application.material.bookmarkswallet.app.features.bookmarkList.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.User
import com.application.material.bookmarkswallet.app.ui.MaterialBookmarkMaterialTheme
import com.application.material.bookmarkswallet.app.ui.components.MbCardView
import com.application.material.bookmarkswallet.app.ui.components.MbPrimaryButton
import com.application.material.bookmarkswallet.app.ui.style.Dimen
import com.application.material.bookmarkswallet.app.ui.style.mbCardRoundedCornerShape
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextSmallStyle
import com.application.material.bookmarkswallet.app.ui.style.mbSubtitleTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbTitleMediumBoldYellowLightDarkTextStyle
import com.application.material.bookmarkswallet.app.ui.style.mbWhiteDarkColor
import com.application.material.bookmarkswallet.app.ui.style.mbYellowLemonLightMustardDarkColor
import com.application.material.bookmarkswallet.app.utils.EMPTY
import com.application.material.bookmarkswallet.app.utils.GUEST
import com.application.material.bookmarkswallet.app.utils.NINETY_F
import com.application.material.bookmarkswallet.app.utils.NO_TIMESTAMP
import com.application.material.bookmarkswallet.app.utils.ONE
import com.application.material.bookmarkswallet.app.utils.ONEF
import com.application.material.bookmarkswallet.app.utils.TWOHUNDRED_SEVENTY_F

@Composable
fun UserLoginCardView(
    modifier: Modifier = Modifier,
    user: User? = null,
    isCollapsed: Boolean = true,
    onOpenAction: ((User) -> Unit)? = null,
) {
//    val activity = LocalActivity.current
//    val providers = arrayListOf(
//        AuthUI.IdpConfig.GoogleBuilder().build()
//    )
//    // See: https://developer.android.com/training/basics/intents/result
//    val signInLauncher = (activity as ComponentActivity).registerForActivityResult(
//        FirebaseAuthUIActivityResultContract(),
//    ) { result ->
//        val response = result.idpResponse
//        if (result.resultCode == RESULT_OK) {
//            // Successfully signed in
//            val user = FirebaseAuth.getInstance().currentUser
//            // ...
//        } else {
//            // Sign in failed. If response is null the user canceled the
//            // sign-in flow using the back button. Otherwise check
//            // response.getError().getErrorCode() and handle the error.
//            // ...
//        }
//    }

    val isCollapsed = remember {
        mutableStateOf(
            value = isCollapsed
        )
    }

    //fallbackIcon
    val fallbackIcon = rememberDrawablePainterWithColor(
        res = R.drawable.ic_user,
        colorRes = R.color.colorAccent
    )

    MbCardView(
        modifier = modifier
            .wrapContentHeight()
            .wrapContentWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        all = Dimen.paddingMedium16dp
                    )
                    .wrapContentWidth()
                    .clickable {
                        user
                            ?.let {
                                onOpenAction?.invoke(user)
                            }
                    },
                horizontalArrangement = Arrangement.Center
            ) {
                //image icon
                AsyncImage(
                    model = user?.photoUrl,
                    error = fallbackIcon,
                    placeholder = fallbackIcon,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(
                        color = mbYellowLemonLightMustardDarkColor()
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .align(
                            alignment = Alignment.CenterVertically
                        )
                        .size(size = 38.dp)
                        .clip(
                            shape = mbCardRoundedCornerShape()
                        )
                )

                //Name
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(
                            weight = ONEF
                        )
                        .align(
                            alignment = Alignment.CenterVertically
                        )
                        .padding(
                            start = Dimen.paddingSmall8dp
                        ),
                    style = mbTitleMediumBoldYellowLightDarkTextStyle(),
                    maxLines = 2,
                    text = user?.name
                        ?.takeIf {
                            it.isNotEmpty()
                        }
                        ?: GUEST
                )

                //edit
                Icon(
                    modifier = Modifier,
                    painter = painterResource(
                        id = R.drawable.ic_edit_dark
                    ),
                    contentDescription = "item",
                    tint = mbYellowLemonLightMustardDarkColor()
                )
                //icon collapse
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_arrow_right_dark
                    ),
                    contentDescription = EMPTY,
                    modifier = Modifier
                        .align(
                            alignment = Alignment.CenterVertically
                        )
                        .size(
                            size = Dimen.size20dp
                        )
                        .rotate(
                            degrees = when {
                                isCollapsed.value.not() -> TWOHUNDRED_SEVENTY_F

                                else -> NINETY_F
                            }
                        )
                        .clickable {
                            isCollapsed.value = isCollapsed.value.not()
                        },
                    tint = mbYellowLemonLightMustardDarkColor()
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier,
            visible = isCollapsed.value.not()
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        top = Dimen.paddingMedium16dp
                    )
                    .padding(
                        horizontal = Dimen.paddingMedium16dp
                    )
            ) {
                //userId
                Text(
                    modifier = Modifier
                        .padding(
                            top = Dimen.paddingSmall8dp
                        ),
                    style = mbSubtitleTextStyle(),
                    maxLines = ONE,
                    text = stringResource(id = R.string.userid_login)
                )
                //userId
                Text(
                    modifier = Modifier
                        .padding(
                            top = Dimen.paddingSmall8dp
                        ),
                    style = mbSubtitleTextSmallStyle(),
                    maxLines = ONE,
                    text = user?.uid ?: "NO_USER_ID"
                )
                MbPrimaryButton(
                    modifier = Modifier
                        .align(
                            alignment = Alignment.CenterHorizontally
                        )
                        .padding(
                            top = Dimen.paddingMedium16dp
                        ),
                    text = stringResource(id = R.string.connect_google_user_label),
                    onClickAction = {
                        // Create and launch sign-in intent
//                            val signInIntent = AuthUI.getInstance()
//                                .createSignInIntentBuilder()
//                                .setAvailableProviders(providers)
//                                .build()
//                            signInLauncher.launch(signInIntent)
                    }
                )
            }
        }
    }
}

@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun UserLoginCardViewPreview() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(color = mbWhiteDarkColor())) {
            UserLoginCardView(
                isCollapsed = false,
                user = User(
                    name = "Davide",
                    photoUrl = "https://p.kindpng.com/picc/s/727-7271359_philip-j-fry-avatar-hd-png-download.png",
                    uid = "3xfcd11120334cdeffacl123eeeddd11222244",
                    email = "blallal@gmail.com"
                ),
                onOpenAction = {}
            )
        }
    }
}

@Composable
@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun UserLoginCardViewPreview2() {
    MaterialBookmarkMaterialTheme {
        Box(modifier = Modifier.background(color = mbWhiteDarkColor())) {
            UserLoginCardView(
                isCollapsed = false,
                onOpenAction = {}
            )
        }
    }
}