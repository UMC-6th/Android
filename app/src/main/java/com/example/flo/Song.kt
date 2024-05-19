package com.example.flo

data class Song(
    var title: String?= "",
    var singer: String?= "",
    var second: Int = 0,
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    var music: String = "",
    var coverImg: Int? = null,
    var isLike: Boolean = false,
    var songs: ArrayList<Song>? = null
)