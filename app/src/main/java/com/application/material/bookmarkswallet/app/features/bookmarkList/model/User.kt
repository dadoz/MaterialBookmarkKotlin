package com.application.material.bookmarkswallet.app.features.bookmarkList.model

import androidx.annotation.Keep

@Keep
data class User(
    val name: String,
    val surname: String,
    val username: String,
    val profilePictureUrl: String? = null,
    val age: Int
)