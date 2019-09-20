import com.application.dev.david.materialbookmarkkot.models.BookmarkInfo
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface BookmarkInfoService {

    @GET("https://getmeta.info/json?")
    fun retrieveBookmarkInfo(@Query("url") url: String): Observable<BookmarkInfo>


    companion object {
        fun create(): BookmarkInfoService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://en.wikipedia.org/w/")
                .build()

            return retrofit.create(BookmarkInfoService::class.java)
        }
    }
}
