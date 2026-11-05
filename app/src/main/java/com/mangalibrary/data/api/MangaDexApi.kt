package com.mangalibrary.data.api

import com.mangalibrary.data.model.ChapterDataDto
import com.mangalibrary.data.model.MangaDataDto
import com.mangalibrary.data.model.MangaDexListResponse
import com.mangalibrary.data.model.MangaDexSingleResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MangaDexApi {

    @GET("manga")
    suspend fun searchManga(
        @Query("title") title: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("includes[]") includes: List<String> = listOf("cover_art", "author", "artist"),
        @Query("contentRating[]") contentRating: List<String> = listOf("safe", "suggestive"),
        @Query("order[relevance]") orderRelevance: String? = "desc"
    ): MangaDexListResponse<MangaDataDto>

    @GET("manga/{id}")
    suspend fun getMangaDetails(
        @Path("id") id: String,
        @Query("includes[]") includes: List<String> = listOf("cover_art", "author", "artist")
    ): MangaDexSingleResponse<MangaDataDto>

    @GET("manga/{id}/feed")
    suspend fun getMangaChapters(
        @Path("id") id: String,
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0,
        @Query("translatedLanguage[]") languages: List<String> = listOf("es", "es-la", "en"),
        @Query("order[chapter]") orderChapter: String = "asc"
    ): MangaDexListResponse<ChapterDataDto>

    companion object {
        private const val BASE_URL = "https://api.mangadex.org/"
        const val COVER_BASE_URL = "https://uploads.mangadex.org/covers"

        fun create(): MangaDexApi {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MangaDexApi::class.java)
        }
    }
}
