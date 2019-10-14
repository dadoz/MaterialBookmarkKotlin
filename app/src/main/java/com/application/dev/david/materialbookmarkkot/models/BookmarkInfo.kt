package com.application.dev.david.materialbookmarkkot.models
import com.google.gson.annotations.SerializedName

data class BookmarkInfo (

	@SerializedName("meta") val meta : Meta,
	@SerializedName("result") val result : Result
)

data class Meta (

	@SerializedName("site") val site : Site,
	@SerializedName("title") val title : String,
	@SerializedName("image") var image : String,
	@SerializedName("description") val description : String
)

data class Result (

	@SerializedName("status") val status : String
)

data class Site (

	@SerializedName("theme_color") val theme_color : String,
	@SerializedName("name") val name : String,
	@SerializedName("manifest") val manifest : String,
	@SerializedName("logo") val logo : String,
	@SerializedName("favicon") val favicon : String,
	@SerializedName("canonical") val canonical : String
)