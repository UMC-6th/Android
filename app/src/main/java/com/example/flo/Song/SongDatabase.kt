package com.example.flo.Song

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flo.Album.Album
import com.example.flo.Album.AlbumDao
import com.example.flo.Like
import com.example.flo.User.User
import com.example.flo.User.UserDao

@Database(entities = [Song::class, User::class, Like::class, Album::class], version = 1)
abstract class SongDatabase: RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun albumDao(): AlbumDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    SongDatabase::class.java,
                    "song-database"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                    .also { instance = it }
            }
        }
    }
}
