package com.application.material.bookmarkswallet.app.models

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

//data class BookmarkInfo(
//
////	@SerializedName("meta") val meta: Meta?,
////	@SerializedName("result") val result: Result?,
//	@SerializedName("title") val title: String,
//	@SerializedName("description") val description: String,
//	@SerializedName("images") val images: Any,
//	@SerializedName("name") val name: String,
//	@SerializedName("favicon") var favicon: String,
//	@SerializedName("duration") val duration: String,
//	@SerializedName("domain") val domain: String,
//	@SerializedName("url") val url: String,
//)
@JsonClass(generateAdapter = true)
data class BookmarkIconInfo(
    @SerializedName("name")
    val name: String,
    @SerializedName("domain")
    val domain: String,
    @SerializedName("logo_url")
    var favicon: String //@TODO replace to val
)

data class Meta(

    @SerializedName("site") val site: Site,
    @SerializedName("title") val title: String,
    @SerializedName("image") var image: String,
    @SerializedName("description") val description: String
)

data class Result(

    @SerializedName("status") val status: String
)

data class Site(

    @SerializedName("theme_color") val theme_color: String,
    @SerializedName("name") val name: String,
    @SerializedName("manifest") val manifest: String,
    @SerializedName("logo") val logo: String,
    @SerializedName("favicon") val favicon: String,
    @SerializedName("canonical") val canonical: String
)