package com.example.flo

import SongDatabase
import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("SongActivity", "onCreate called")

        try {
            if (intent.hasExtra("title") && intent.hasExtra("singer")) {
                binding.songMusicTitleTv.text = intent.getStringExtra("title")
                binding.songSingerNameTv.text = intent.getStringExtra("singer")
            }
        } catch (e: Exception) {
            Log.e("SongActivity", "Error setting title and singer: ${e.message}")
        }

        binding.songDownIb.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("songsong", binding.songMusicTitleTv.text.toString())
            }
            setResult(Activity.RESULT_OK, intent)
            if (!isFinishing) finish()
        }

        initPlayPauseBtn()
        initRepeatBtn()
        initRandomBtn()
        initClickListner()
        initPlaylist()
        initSong()
    }

    private fun initRepeatBtn() {
        with(binding) {
            songRepeatOffIv.setOnClickListener {
                songRepeatOffIv.visibility = View.GONE
                songRepeatOnIv.visibility = View.VISIBLE
            }
            songRepeatOnIv.setOnClickListener {
                songRepeatOnIv.visibility = View.GONE
                songRepeatOffIv.visibility = View.VISIBLE
            }
        }
    }

    private fun initPlayPauseBtn() {
        with(binding) {
            songMiniplayerIv.setOnClickListener {
                songMiniplayerIv.visibility = View.GONE
                songPauseIv.visibility = View.VISIBLE
                setPlayerStatus(true)
            }
            songPauseIv.setOnClickListener {
                songMiniplayerIv.visibility = View.VISIBLE
                songPauseIv.visibility = View.GONE
                setPlayerStatus(false)
            }
        }
    }

    private fun initRandomBtn() {
        with(binding) {
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

    private fun setPlayerStatus(isPlaying: Boolean) {
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }
    }

    private fun moveSong(direct: Int) {
        if (nowPos + direct < 0) {
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size) {
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer = null

        try {
            if (songs[nowPos].playTime > 0) {
                setPlayer(songs[nowPos])
            } else {
                Toast.makeText(this, "Invalid song playtime", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("SongActivity", "Error in moveSong: ${e.message}")
        }
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            if (songs[i].id == songId) {
                return i
            }
        }
        return 0
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {

        private var second: Int = 0
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
            } catch (e: InterruptedException) {
                Log.d("SongActivity", "쓰레드가 죽었습니다. ${e.message}")
            } catch (e: Exception) {
                Log.e("SongActivity", "Error in timer thread: ${e.message}")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("SongActivity", "onPause called")
        setPlayerStatus(false)
        songs[nowPos].second =
            if (songs[nowPos].playTime > 0) ((binding.songProgressSb.progress * songs[nowPos].playTime) / 100) / 1000 else 0
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("songId", songs[nowPos].id)

        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SongActivity", "onDestroy called")
        try {
            timer.interrupt()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            Log.e("SongActivity", "Error in onDestroy: ${e.message}")
        }
    }

    private fun initPlaylist() {
        try {
            songDB = SongDatabase.getInstance(this)!!
            songs.addAll(songDB.songDao().getSongs())

            if (songs.isEmpty()) {
                Log.e("SongActivity", "플레이리스트가 비어 있습니다.")
            } else {
                Log.d("SongActivity", "플레이리스트 초기화 완료: ${songs.size}곡")
            }
        } catch (e: Exception) {
            Log.e("SongActivity", "Error initializing playlist: ${e.message}")
        }
    }

    private fun initSong() {
        if (songs.isEmpty()) {
            Log.e("SongActivity", "플레이리스트가 비어 있습니다. initSong을 실행할 수 없습니다.")
            return
        }

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        if (nowPos >= songs.size) {
            nowPos = 0
        }

        Log.d("SongActivity", "now Song ID: ${songs[nowPos].id}")

        try {
            startTimer()
            setPlayer(songs[nowPos])
        } catch (e: Exception) {
            Log.e("SongActivity", "Error in initSong: ${e.message}")
        }
    }

    private fun setPlayer(song: Song) {
        try {
            binding.songMusicTitleTv.text = song.title
            binding.songSingerNameTv.text = song.singer
            binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
            binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
            binding.songAlbumIv.setImageResource(song.coverImg ?: R.drawable.img_album_exp2)

            if (song.playTime > 0) {
                binding.songProgressSb.progress = (song.second * 1000 / song.playTime)
            } else {
                binding.songProgressSb.progress = 0
            }

            val music = resources.getIdentifier(song.music, "raw", this.packageName)
            mediaPlayer = MediaPlayer.create(this, music)

            if (song.isLike) {
                binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
            } else {
                binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
            }

            setPlayerStatus(song.isPlaying)
        } catch (e: Exception) {
            Log.e("SongActivity", "Error in setPlayer: ${e.message}")
        }
    }


    private fun initClickListner() {
        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songNextIv.setOnClickListener {
            moveSong(+1)
        }

        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }

        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }

    private fun setLike(isLike: Boolean) {
        try {
            songs[nowPos].isLike = !isLike
            songDB.songDao().updateIsLikeById(!isLike, songs[nowPos].id)

            if (!isLike) {
                binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
            } else {
                binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
            }
        } catch (e: Exception) {
            Log.e("SongActivity", "Error in setLike: ${e.message}")
        }
    }
}
