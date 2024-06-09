package com.example.flo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class SearchResult(
    val items: List<SearchItem>
)

data class VideoId(
    val videoId: String
)

data class SearchItem(
    val id: VideoId,
    val snippet: Snippet
)

data class Snippet(
    val title: String,
    val thumbnails: Thumbnails,
    val channelTitle: String, // channelTitle 추가
    val channelId: String // channelId 추가
)


data class Thumbnails(
    val default: Thumbnail
)

data class Thumbnail(
    val url: String
)

interface YouTubeService {
    @GET("search")
    suspend fun searchVideos(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("order") order: String,
        @Query("type") type: String,
        @Query("fields") fields: String,
        @Query("maxResults") maxResults: Long
    ): SearchResult
}

object YouTubeApi {
    private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val youTubeService: YouTubeService = retrofit.create(YouTubeService::class.java)
}
