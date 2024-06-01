package com.example.flo

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.flo.Album.Album
import com.example.flo.Locker.LockerFragment
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.example.flo.Look.LookFragment
import com.example.flo.Song.Song
import com.example.flo.Song.SongActivity
import com.example.flo.Song.SongDatabase
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.common.util.Utility


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var song: Song = Song()
    private var gson: Gson = Gson()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //해시키값 구하기 (Terminal 보다 이게 더 정확함)
        Log.d(TAG, "keyhash : ${Utility.getKeyHash(this)}")

        inputDummySongs()
        inputDummyAlbums()
        initBottomNavigation()

        Log.d("Song", "제목 : ${song.title} / 가수 : ${song.singer}")

        binding.mainPlayerCl.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        Log.d("MAIN/JWT_TO_SERVER", getJwt().toString())

    }

    private fun getJwt(): String? {
        val spf = this.getSharedPreferences("auth2", AppCompatActivity.MODE_PRIVATE)
        return spf.getString("jwt", "")
    }

    override fun onStart() {
        super.onStart()
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!

        song = if (songId == 0) {
            songDB.songDao().getSong(1)
        } else {
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", song.id.toString())
        setMiniPlayer(song)
    }

    private fun initBottomNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    true
                }
                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    true
                }
                else -> false
            }
        }
    }


    private fun setMiniPlayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainMiniplayerProgressSb.progress = (song.second * 100000) / song.playTime
    }

    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return

        songDB.songDao().insert(Song(1, "BFF", "TWS", 0, 167, false, "music_bff", R.drawable.img_album_bff, false))
        songDB.songDao().insert(Song(2, "Bubble Gum", "NewJeans", 0, 201, false, "music_bubblegum", R.drawable.img_album_howsweet, false))
        songDB.songDao().insert(Song(3, "CountDown (BELLE Solo)", "KISS OF LIFE", 0, 172, false, "music_countdown", R.drawable.img_album_countdown, false))
        songDB.songDao().insert(Song(4, "How Sweet", "NewJeans", 0, 220, false, "music_howsweet", R.drawable.img_album_howsweet, false))
        songDB.songDao().insert(Song(5, "별 떨어진다 I Do", "D.O.", 0, 164, false, "music_ido", R.drawable.img_album_ido, false))
        songDB.songDao().insert(Song(6, "Love War (feat. BE'O)", "YENA (최예나)", 0, 189, false, "music_lovewar", R.drawable.img_album_lovewar, false))
        songDB.songDao().insert(Song(7, "Mars", "도경수", 0, 188, false, "music_mars", R.drawable.img_album_mars, false))
        songDB.songDao().insert(Song(8, "SPOT! (feat.JENNIE)", "ZICO", 0, 168, false, "music_spot", R.drawable.img_album_spot, false))
        songDB.songDao().insert(Song(9, "Supernova", "aespa", 0, 179, false, "music_supernova", R.drawable.img_album_supernova, false))

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }

    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if (albums.isNotEmpty()) return

        songDB.albumDao().insert(Album(1, "Armageddon - The 1st Album", "AESPA", R.drawable.img_album_armageddon))
        songDB.albumDao().insert(Album(2, "KISS OF LIFE", "KISS OF LIFE", R.drawable.img_album_countdown))
        songDB.albumDao().insert(Album(3, "성장 - THE 3RD MINI ALBUM", "도경수", R.drawable.img_album_mars))
        songDB.albumDao().insert(Album(4, "How Sweet", "뉴진스 (Newjeans", R.drawable.img_album_howsweet))
        songDB.albumDao().insert(Album(5, "TWS : 1st Mini Album 'Sparkling Blue'", "TWS", R.drawable.img_album_bff))

        val _albums = songDB.albumDao().getAlbums()
        Log.d("DB data", _albums.toString())
    }
}
