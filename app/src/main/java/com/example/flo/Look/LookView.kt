package com.example.flo.Look

import com.example.flo.Song.FloChartResult

interface LookView {
    fun onGetSongLoading()
    fun onGetSongSuccess(code:Int, result: FloChartResult)
    fun onGetSongFailure(code: Int, message: String)
}