package com.application.material.bookmarkswallet.app.features.bookmarkList.model

import androidx.annotation.Keep

@Keep
data class User(
    val name: String,
    val uid: String,
    val photoUrl: String? = null,
    val email: String?
)