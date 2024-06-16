package com.example.flo.ui.main.album

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Like

@Database(entities = [Album::class, Like::class], version = 1)
abstract class AlbumDatabase: RoomDatabase() {
    abstract fun albumDao(): AlbumDao

    companion object{
        private var instance: AlbumDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AlbumDatabase? {
            if (instance ==null){
                synchronized(AlbumDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AlbumDatabase::class.java,
                        "song-database" //다른 데이터 베이스랑 이름겹치면 꼬임
                    ).allowMainThreadQueries().build()
                }
            }
            return instance
        }
    }
}