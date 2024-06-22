package com.application.material.bookmarkswallet.app.network.models

import com.google.errorprone.annotations.Keep

@Keep
sealed class Response<out R> {
    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val exception: Throwable) : Response<Nothing>()
}