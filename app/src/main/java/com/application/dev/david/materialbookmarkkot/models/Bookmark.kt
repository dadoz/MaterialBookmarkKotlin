package com.application.dev.david.materialbookmarkkot.models

import com.google.gson.annotations.SerializedName
import java.util.*


data class Bookmark (@SerializedName("site_name")
        var siteName: String?,
        @SerializedName("title")
        var title: String?,
        @SerializedName("image")
        var image: String?,
        @SerializedName("app_id")
        var appId: String?,
        @SerializedName("url")
        var url: String?,
        @SerializedName("timestamp")
        var timestamp: Date?)