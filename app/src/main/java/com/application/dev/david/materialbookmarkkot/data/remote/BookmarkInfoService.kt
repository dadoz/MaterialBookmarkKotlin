import com.application.dev.david.materialbookmarkkot.BuildConfig
import com.application.dev.david.materialbookmarkkot.models.BookmarkInfo
import io.reactivex.Observable
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import android.R.attr.password
import okhttp3.Credentials


interface BookmarkInfoService {

    @GET("/")
    fun retrieveBookmarkInfo(@Query("url") url: String): Observable<BookmarkInfo>


    companion object {
        fun create(): BookmarkInfoService {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC

            val authToken = Credentials.basic("da-doz@hotmail.it", "INuNPW7T5eZdYe6EFyw8")
            val basicAuthInterceptor = AuthenticationInterceptor(authToken)

            val loggerClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(basicAuthInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(loggerClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.BOOKMARK_INFO_URL)
                .build()

            return retrofit.create(BookmarkInfoService::class.java)
        }
    }
}

class AuthenticationInterceptor(private val authToken: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val builder = original.newBuilder()
            .header("Authorization", authToken)

        val request = builder.build()
        return chain.proceed(request)
    }
}
