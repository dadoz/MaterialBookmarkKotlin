package com.application.material.bookmarkswallet.app.models

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.room.Entity
import androidx.room.PrimaryKey
import coil.load
import coil.transform.CircleCropTransformation
import com.application.material.bookmarkswallet.app.R
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
@Entity(tableName = "mb_bookmark")
data class Bookmark (
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
//        @ContextualSerialization
        var timestamp: Date?,
        @SerializedName("is_star")
//        @ContextualSerialization
        var isLike: Boolean = false
): BookmarkType {
        companion object {
                fun getId(url: String): String {
                        return UUID.randomUUID().toString()
                }
        }
}

@BindingAdapter("iconSrc")
fun setImageViewResource(imageView: ImageView, url: String?) {
        imageView.load(url) {
                crossfade(true)
                placeholder(R.drawable.ic_bookmark_light)
                error(R.drawable.ic_bookmark_light)
                transformations(CircleCropTransformation())
        }
}

@BindingAdapter("iconSquaredSrc")
fun setImageViewSquaredResource(imageView: ImageView, url: String?) {
        imageView.load(url) {
                crossfade(true)
                placeholder(R.drawable.ic_bookmark_light)
                error(R.drawable.ic_bookmark_light)
//                        .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(32)))
                transformations(CircleCropTransformation())
        }
}

@JsonClass(generateAdapter = true)
data class BookmarkSimple (
        @SerializedName("title")
        var title: String?,
        @SerializedName("icon")
        var icon: String?,
        @SerializedName("url")
        var url: String,
        @SerializedName("description")
        var description: String
)
