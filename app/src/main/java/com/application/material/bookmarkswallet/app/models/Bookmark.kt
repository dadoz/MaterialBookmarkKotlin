package com.application.material.bookmarkswallet.app.models

import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.application.material.bookmarkswallet.app.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "mb_bookmark")
data class Bookmark (
        //TODO useless
        @SerializedName("site_name")
        var siteName: String?,
        @SerializedName("title")
        var title: String?,
        //TODO useless
        @SerializedName("image")
        var image: String?,
        //TODO mv to uniqueID
        @SerializedName("app_id")
        var appId: String?,
        @PrimaryKey
        @NonNull
        @SerializedName("url")
        var url: String,
        @SerializedName("timestamp")
//        @ContextualSerialization
        var timestamp: Date?,
        @SerializedName("is_star")
//        @ContextualSerialization
        var isStar: Boolean = false
): BookmarkType {
        companion object {
                fun getId(url: String): String {
                        return UUID.randomUUID().toString()
                }
        }
}

@BindingAdapter("iconSrc")
fun setImageViewResource(imageView: ImageView, url: String?) {
        Glide.with(imageView.context)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.ic_bookmark_light)
                .into(imageView)
}

@BindingAdapter("iconSquaredSrc")
fun setImageViewSquaredResource(imageView: ImageView, url: String?) {
        Glide.with(imageView.context)
                .load(url)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(32)))
                .placeholder(R.drawable.ic_bookmark_light)
                .into(imageView)
}
