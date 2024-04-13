package com.example.flo

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity(){ // : 은 상속받는다는 뜻  //액티비티에서 안드로이드 기능을 할 수 있게 해줌.

    lateinit var binding : ActivitySongBinding//전방 선언. 나중에 초기화를 해준다는 뜻

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater) // xml에 표기된 레이아웃들을 메모리에 객체화 시키는 행동~
        setContentView(binding.root)
        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.musicMiniplayerIv.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.musicMvpauseIv.setOnClickListener {
            setPlayerStatus(true)
        }
        if (intent.hasExtra("title") && intent.hasExtra("singer")){
            binding.songMusicTitleTv.text=intent.getStringExtra("title")
            binding.musicSingerNameTv.text=intent.getStringExtra("singer")
        }
    }


    fun setPlayerStatus(isPlaying : Boolean) {
        if(isPlaying){
            binding.musicMiniplayerIv.visibility = View.VISIBLE
            binding.musicMvpauseIv.visibility = View.GONE
        }
        else {
            binding.musicMiniplayerIv.visibility = View.GONE
            binding.musicMvpauseIv.visibility = View.VISIBLE
        }
    }
}