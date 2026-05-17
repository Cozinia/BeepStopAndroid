package com.beepstop.data.remote

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit
import com.beepstop.BuildConfig

data class WikipediaSummary(
    val title: String,
    val extract: String,
    @SerializedName("thumbnail") val thumbnail: WikipediaThumbnail?
)

data class WikipediaThumbnail(
    val source: String
)

interface WikipediaApiService {
    @GET("page/summary/{title}")
    suspend fun getSummary(@Path("title", encoded = false) title: String): WikipediaSummary
}

object WikipediaClient {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .header("User-Agent", "BeepStopApp/1.0 (Android; F1 companion app)")
                    .build()
            )
        }
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                })
            }
        }
        .build()

    val api: WikipediaApiService = Retrofit.Builder()
        .baseUrl("https://en.wikipedia.org/api/rest_v1/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WikipediaApiService::class.java)
}
