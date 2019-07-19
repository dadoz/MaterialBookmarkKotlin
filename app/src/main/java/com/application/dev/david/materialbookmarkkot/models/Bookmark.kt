package com.application.dev.david.materialbookmarkkot.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "mb_bookmark")
data class Bookmark (@SerializedName("site_name")
        var siteName: String?,
        @SerializedName("title")
        var title: String?,
        @SerializedName("image")
        var image: String?,
        @SerializedName("app_id")
        var appId: String?,
        @PrimaryKey
        @NonNull
        @SerializedName("url")
        var url: String,
        @SerializedName("timestamp")
        var timestamp: Date?)