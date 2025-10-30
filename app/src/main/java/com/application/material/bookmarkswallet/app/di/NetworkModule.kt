package com.application.material.bookmarkswallet.app.network

import com.application.material.bookmarkswallet.app.BuildConfig
import com.application.material.bookmarkswallet.app.data.remote.BookmarkInfoService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule @Inject constructor() {
    //credentials
    private val authToken = ""
//        Credentials.basic(BuildConfig.API_URLMETA_USER, BuildConfig.API_URLMETA_PWD)

    @Provides
    fun provideAuthenticationInterceptor(): AuthenticationInterceptor =
        AuthenticationInterceptor(authToken)

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor()
            .apply {
                this.setLevel(BODY)
            }

    @Provides
    @BaseOkHttpClient
    fun provideBaseOkHttpClient(
        interceptor: HttpLoggingInterceptor,
        authenticationInterceptor: AuthenticationInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    this.addInterceptor(interceptor)
                }
            }
            .addInterceptor(authenticationInterceptor)
            .build()

    @Provides
    @AuthOkHttpClient
    fun provideAuthOkHttpClient(
        interceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    this.addInterceptor(interceptor)
                }
            }
            .build()

//    @Provides
//    fun provideMoshi(): Moshi = Moshi.Builder()
//        .add(KotlinJsonAdapterFactory())
//        .build()

    @Provides
    @BaseRetrofit
    fun provideBaseRetrofit(
        @BaseOkHttpClient okHttpClient: OkHttpClient,
    ): Retrofit {
        //base url
        return getRetrofitByUrl(
            okHttpClient = okHttpClient
        )
    }

    @Provides
    fun provideEventDetailService(@BaseRetrofit retrofit: Retrofit): BookmarkInfoService =
        retrofit.create(BookmarkInfoService::class.java)

    private fun getRetrofitByUrl(
        url: String = BuildConfig.LOGODEV_BASE_URL,
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .build()
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseOkHttpClient

class AuthenticationInterceptor(private val authToken: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.request()
        .newBuilder()
//        .header(AUTHORIZATION, authToken)
        .build().let {
            chain.proceed(it)
        }
}
