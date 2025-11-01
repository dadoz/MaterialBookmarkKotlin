package com.application.material.bookmarkswallet.app.features.bookmarkList.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.application.material.bookmarkswallet.app.utils.convert
import com.application.material.bookmarkswallet.app.utils.formatZonedDateTime
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date
import java.util.UUID


@JsonClass(generateAdapter = true)
@Entity(tableName = "mb_bookmark")
data class Bookmark(
    @SerializedName("site_name")
    var siteName: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("image")
    var iconUrl: String?,
    @SerializedName("app_id")
    var appId: String?,
    @PrimaryKey
    @SerializedName("url")
    var url: String,
    @SerializedName("timestamp")
    var timestamp: Date?,
    @SerializedName("is_star")
    var isLike: Boolean = false
) : BookmarkType {
}

fun getBookmarkId(url: String): String = UUID.randomUUID().toString()

fun Bookmark.getLocalDate(): String? = timestamp?.convert()
    .toString()
    .formatZonedDateTime()

@JsonClass(generateAdapter = true)
data class BookmarkSimple(
    @param:Json(name = "site_name")
    var siteName: String?,
    @param:Json(name = "title")
    var title: String?,
    @param:Json(name = "image")
    var icon: String?,
    @param:Json(name = "url")
    var url: String,
    @param:Json(name = "description")
    var description: String
)
