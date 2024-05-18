package com.application.material.bookmarkswallet.app.network

import com.application.material.bookmarkswallet.app.BuildConfig
import com.google.common.net.HttpHeaders.AUTHORIZATION
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

//TODO please forgive me and say me sorry.....i had no time to add a real module with di, but this application come from the past, means from the jurassic world aha
class NetworkModule {
    //credentials
    private val authToken =
        Credentials.basic(BuildConfig.API_URLMETA_USER, BuildConfig.API_URLMETA_PWD)
    private val basicAuthInterceptor = AuthenticationInterceptor(authToken)

    //set logging interceptor
    private val logging = HttpLoggingInterceptor()
        .apply {
            this.level = BODY
        }
    private val loggerClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(basicAuthInterceptor)
        .build()

    fun getRetrofitClient(): Retrofit = Retrofit.Builder()
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(loggerClient)
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.JSONLINK_BASE_URL)
        .build()
}

//todo move smwhere
class AuthenticationInterceptor(private val authToken: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.request()
        .newBuilder()
        .header(AUTHORIZATION, authToken)
        .build().let {
            chain.proceed(it)
        }
}
