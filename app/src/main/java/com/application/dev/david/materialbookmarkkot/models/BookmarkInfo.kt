package com.application.dev.david.materialbookmarkkot.models

data class BookmarkInfo(
    val description: String,
    val icons: List<Icon>,
    val og: Og,
    val title: String
)

data class Icon(
    val href: String,
    val sizes: List<Size>,
    val type: String
)

data class Size(
    val height: Int,
    val width: Int
)

data class Og(
    val image: String,
    val site_name: String,
    val title: String,
    val url: String
)