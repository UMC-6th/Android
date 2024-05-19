package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSavedsongBinding

class SavedSongFragment:Fragment() {

    lateinit var binding: FragmentSavedsongBinding
    private var songDatas = ArrayList<Song>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedsongBinding.inflate(inflater,container,false)

        songDatas.apply {
            add(Song("Love War (feat.BE'O)", "YENA(최예나)", R.drawable.img_album_lovewar))
            add(Song("별 떨어진다 I Do", "D.O.", R.drawable.img_album_ido))
            add(Song("mars", "도경수", R.drawable.img_album_mars))
            add(Song("SPOT! (feat.JENNI)", "ZICO", R.drawable.img_album_spot))
            add(Song("우산", "NCT 127", R.drawable.img_album_umbrella))
            add(Song("Countdown (BELLE Solo)", "KISS OF LIFE", R.drawable.img_album_countdown))
            add(Song("BFF", "TWS", R.drawable.img_album_bff))
        }

        val savedSongRVAdapter = SavedSongRVAdapter(songDatas)
        binding.lockerSaveSongRecyclerView.adapter = savedSongRVAdapter
        binding.lockerSaveSongRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        return binding.root
    }


}