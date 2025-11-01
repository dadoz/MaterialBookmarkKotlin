package com.application.material.bookmarkswallet.app.features.bookmarkList.model

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookmarkIconInfo(
    @SerializedName("name")
    val name: String,
    @SerializedName("domain")
    val domain: String,
    @SerializedName("logo_url")
    val logoUrl: String
)
