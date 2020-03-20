package com.application.dev.david.materialbookmarkkot.models

import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.ContextualSerialization
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
        @ContextualSerialization
        var timestamp: Date?
) {
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
                .into(imageView)
}
