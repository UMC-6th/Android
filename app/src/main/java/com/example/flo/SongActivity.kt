package com.example.flo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() { // : 은 상속받는다는 뜻  //액티비티에서 안드로이드 기능을 할 수 있게 해줌.

    lateinit var binding: ActivitySongBinding//전방 선언. 나중에 초기화를 해준다는 뜻

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater) // xml에 표기된 레이아웃들을 메모리에 객체화 시키는 행동~
        setContentView(binding.root)

        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.musicSingerNameTv.text = intent.getStringExtra("singer")
        }

        binding.songDownIb.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("flomusic", binding.songMusicTitleTv.text.toString())
            }
            setResult(Activity.RESULT_OK, intent)
            if (!isFinishing) finish()
        }
        initplayPauseBtn()
    }

    // VISIBLE과 GONE으로 pause버튼과 miniplayer버튼의 나타남과 사라짐을 나타냄.
    // activity_song.xml로 구현한 화면에서 해당 부분 누르면 바뀜. - 자세한 건 영상 참고.
    private fun initplayPauseBtn() {
        with(binding) {
            musicMiniplayerIv.setOnClickListener {
                musicMiniplayerIv.visibility = View.GONE
                musicMvpauseIv.visibility = View.VISIBLE
            }
            musicMvpauseIv.setOnClickListener {
                musicMiniplayerIv.visibility = View.VISIBLE
                musicMvpauseIv.visibility = View.GONE
            }
        }
    }
}