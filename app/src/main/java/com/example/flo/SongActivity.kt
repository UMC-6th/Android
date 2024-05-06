package com.example.flo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)  //선언
        binding = ActivitySongBinding.inflate(layoutInflater)   //초기화
        setContentView(binding.root)
        if(intent.hasExtra("title") &&intent.hasExtra("singer")){
            binding.songMusicTitleTv.text=intent.getStringExtra("title")
            binding.songSingerNameTv.text=intent.getStringExtra("singer")
        }
        binding.songDownIb.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java).apply{
                putExtra("songsong", binding.songMusicTitleTv.text.toString())
            }
            setResult(Activity.RESULT_OK, intent)
            if(!isFinishing) finish()
        }

        initPlayPauseBtn()
        initRepeatBtn()
        initRandomBtn()
    }
    private fun initRepeatBtn(){
        with(binding){
            songRepeatOffIv.setOnClickListener {
                songRepeatOffIv.visibility=View.GONE
                songRepeatOnIv.visibility=View.VISIBLE
            }
            songRepeatOnIv.setOnClickListener {
                songRepeatOnIv.visibility=View.GONE
                songRepeatOffIv.visibility=View.VISIBLE
            }
        }
    }
    private fun initPlayPauseBtn(){
        with(binding){
            songMiniplayerIv.setOnClickListener{
                songMiniplayerIv.visibility= View.GONE
                songPauseIv.visibility= View.VISIBLE
            }
            songPauseIv.setOnClickListener{
                songMiniplayerIv.visibility= View.VISIBLE
                songPauseIv.visibility= View.GONE
            }
        }

        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(true)
        }
        if(intent.hasExtra("title")&&intent.hasExtra("singer")){
            binding.songMusicTitleTv.text=intent.getStringExtra("title")
            binding.songSingerNameTv.text=intent.getStringExtra("singer")
        }
    }
    private fun initRandomBtn(){
        with(binding){
            songRandomOffIv.setOnClickListener {
                songRandomOffIv.visibility = View.GONE
                songRandomOnIv.visibility = View.VISIBLE
            }
            songRandomOnIv.setOnClickListener {
                songRandomOffIv.visibility = View.VISIBLE
                songRandomOnIv.visibility = View.GONE
            }
        }
    }
    fun setPlayerStatus(isPlaying : Boolean) {
        if(isPlaying){
            binding.songMiniplayerIv.visibility= View.VISIBLE
            binding.songPauseIv.visibility=View.GONE
        }
        else {
            binding.songMiniplayerIv.visibility=View.GONE
            binding.songPauseIv.visibility=View.VISIBLE
        }
    }
}