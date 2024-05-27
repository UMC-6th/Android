package com.example.flo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var title: String? = "",
    val singer: String,
    var coverImg: Int? = null
)