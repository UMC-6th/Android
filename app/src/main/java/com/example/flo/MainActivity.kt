package com.example.flo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var song: Song = Song()
    private var gson: Gson = Gson()

    private val getResultText = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->
        if(result.resultCode== Activity.RESULT_OK){
            val returnString = result.data?.getStringExtra("songsong")
            Toast.makeText(applicationContext, returnString, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()
        initBottomNavigation()

        Log.d("Song", "제목 : ${song.title} / 가수 : ${song.singer}")

        binding.mainPlayerCl.setOnClickListener{
            val editor = getSharedPreferences(  "song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun setMiniPlayer(song:Song){
        binding.mainMiniplayerTitileTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainMiniplayerProgressSb.progress = (song.second*100000)/song.playTime
    }

    override fun onStart() {
        super.onStart()
/*        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData", null)

        song = if(songJson == null){
            Song("라일락", "아이유(IU)", 0, 60, false, "music_lilac")
        }else {
            gson.fromJson(songJson, Song::class.java)
        }*/
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!

        song = if(songId == 0){
            songDB.songDao().getSongs(1)
        }else{
            songDB.songDao().getSongs(songId)
        }

        Log.d("song ID", song.id.toString())
        setMiniPlayer(song)
    }

    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return

        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_flu",
                R.drawable.img_album_exp2,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Next Level",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "music_next",
                R.drawable.img_album_exp3,
                false,
            )
        )


        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "방탄소년단 (BTS)",
                0,
                230,
                false,
                "music_boy",
                R.drawable.img_album_exp4,
                false,
            )
        )


        songDB.songDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "music_bboom",
                R.drawable.img_album_exp5,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "BFF",
                "TWS",
                0,
                167,
                false,
                "music_bff",
                R.drawable.img_album_bff,
                false,
            )
        )


        songDB.songDao().insert(
            Song(
                "Bubble Gum",
                "NewJeans",
                0,
                201,
                false,
                "music_bubblegum",
                R.drawable.img_album_howsweet,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "CountDown (BELLE Solo)",
                "KISS OF LIFE",
                0,
                172,
                false,
                "music_countdown",
                R.drawable.img_album_countdown,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "How Sweet",
                "NewJeans",
                0,
                220,
                false,
                "music_howsweet",
                R.drawable.img_album_howsweet,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "별 떨어진다 I Do",
                "D.O.",
                0,
                164,
                false,
                "music_ido",
                R.drawable.img_album_ido,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Love War (feat. BE'O)",
                "YENA (최예나)",
                0,
                189,
                false,
                "music_lovewar",
                R.drawable.img_album_lovewar,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Mars",
                "도경수",
                0,
                188,
                false,
                "music_mars",
                R.drawable.img_album_mars,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "SPOT! (feat.JENNIE)",
                "ZICO",
                0,
                168,
                false,
                "music_spot",
                R.drawable.img_album_spot,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Supernova",
                "aespa",
                0,
                179,
                false,
                "music_supernova",
                R.drawable.img_album_supernova,
                false,
            )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }
}