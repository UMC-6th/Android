package com.example.flo

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    lateinit var song: Song
    lateinit var timer:Timer
    private var mediaPlayer: MediaPlayer? = null //activity가 소멸될 때 미디어플레이어를 해제 시켜줘야해서 null사용
    private var gson: Gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)  //선언
        binding = ActivitySongBinding.inflate(layoutInflater)   //초기화
        setContentView(binding.root)
        if(intent.hasExtra("title") &&intent.hasExtra("singer")){ //앨범 제목 표시
            binding.songMusicTitleTv.text=intent.getStringExtra("title")
            binding.songSingerNameTv.text=intent.getStringExtra("singer")
        }
        //종료하면서 데이터 넘겨주기
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
        initSong()
        setPlayer(song)
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
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
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
    private fun setPlayerStatus(isPlaying : Boolean) {
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.songMiniplayerIv.visibility= View.GONE
            binding.songPauseIv.visibility=View.VISIBLE
            mediaPlayer?.start()
        }
        else {
            binding.songMiniplayerIv.visibility=View.VISIBLE
            binding.songPauseIv.visibility=View.GONE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }
    }

    private fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second",0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying",false),
                intent.getStringExtra("music")!!
            )
        }
        startTimer()
    }

    private fun setPlayer(song:Song){
        binding.songMusicTitleTv.text=intent.getStringExtra("title")!!
        binding.songSingerNameTv.text=intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)
        val music = resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer = MediaPlayer.create(this,music)
        setPlayerStatus(song.isPlaying)
    }

    private fun startTimer(){
        timer = Timer(song.playTime,song.isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int,var isPlaying: Boolean=true):Thread(){

        private var second : Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try {
                while (true) {

                    if (second >= playTime) {
                        break
                    }

                    if (isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills / playTime) * 100).toInt()
                        }

                        if (mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.songStartTimeTv.text =
                                    String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            }catch(e:InterruptedException){
                Log.d("Song", "쓰레드가 죽었습니다. ${e.message}")
            }
        }
    }
    // 사용자가 포커스를 잃었을 때 음악이 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        song.second = ((binding.songProgressSb.progress * song.playTime)/100)/1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE) //SharedPreference란 내부 저장소에 데이터를 저장할 수 있도록 함. 앱이 종료되었다가 다시 실행되도 저장된 값을 다시 사용할 수 있음
        val editor = sharedPreferences.edit() //에디터
         //제이슨 포멧으로 한 번에 넣어줌. 제이슨이란 데이터 표현 표준. 보통 자바 객체를 다른 곳으로 전달할 때 제이슨 포멧으로 보냄
         //song 객체를 제이슨으로 변환 시키기 위해 지슨을 사용. 자바 객체를 제이슨으로, 제이슨을 자바 객체로 변환 시켜줌
        val songJson = gson.toJson(song)
        editor.putString("songData", songJson)

        editor.apply() //apply까지 해야 실제 내부 저장소에 저장이 됨
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() //미디어플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null //미디어 플레이어 해제
    }

}