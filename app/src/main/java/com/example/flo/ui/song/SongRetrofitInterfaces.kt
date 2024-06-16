package com.example.flo.ui.song

import retrofit2.http.GET
import retrofit2.Call

interface SongRetrofitInterfaces {
    @GET("/songs")
    fun getSongs(): Call<SongResponse>
}