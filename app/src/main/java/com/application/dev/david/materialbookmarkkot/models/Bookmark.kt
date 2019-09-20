package com.application.dev.david.materialbookmarkkot.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
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
        var timestamp: Date?
) {
        companion object {
                fun getId(url: String): String {
                        return UUID.fromString(url).toString()
                }
        }

}

