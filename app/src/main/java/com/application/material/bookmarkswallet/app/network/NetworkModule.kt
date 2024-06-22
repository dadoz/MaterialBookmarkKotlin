package com.application.material.bookmarkswallet.app.network

import com.application.material.bookmarkswallet.app.BuildConfig
import com.application.material.bookmarkswallet.app.data.remote.BookmarkInfoService
import com.google.common.net.HttpHeaders.AUTHORIZATION
import com.squareup.moshi.Types
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule @Inject constructor() {
    //credentials
    private val authToken =
        Credentials.basic(BuildConfig.API_URLMETA_USER, BuildConfig.API_URLMETA_PWD)

//    private val basicAuthInterceptor = AuthenticationInterceptor(authToken)
//    //set logging interceptor
//    private val logging = HttpLoggingInterceptor()
//        .apply {
//            this.level = BODY
//        }
//    private val loggerClient = OkHttpClient.Builder()
//        .addInterceptor(logging)
//        .addInterceptor(basicAuthInterceptor)
//        .build()

//    fun getRetrofitClient(): Retrofit = Retrofit.Builder()
//        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
//        .client(loggerClient)
//        .addConverterFactory(GsonConverterFactory.create())
//        .baseUrl(BuildConfig.JSONLINK_BASE_URL)
//        .build()

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

    private fun createRequest(chain: Interceptor.Chain, oAuthToken: String) =
        chain.request().newBuilder()
            .addHeader(name = "Authorization", value = oAuthToken).build()

    private fun getRetrofitByUrl(
        url: String = BuildConfig.JSONLINK_BASE_URL,
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .build()
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseOkHttpClient

fun <T> getType(
    classType: Class<T>
): Type {
    return Types.newParameterizedType(List::class.java, classType)
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
