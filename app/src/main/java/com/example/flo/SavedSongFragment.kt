package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSavedsongBinding

class SavedSongFragment : Fragment() {

    private var _binding: FragmentSavedsongBinding? = null
    private val binding get() = _binding!!

    private var songDatas = ArrayList<Song>()
    private lateinit var savedSongRVAdapter: SavedSongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedsongBinding.inflate(inflater, container, false)

        initSongData()
        setupRecyclerView()

        return binding.root
    }

    private fun initSongData() {
        songDatas.apply {
            add(Song("Love War (feat.BE'O)", "YENA(최예나)", coverImg = R.drawable.img_album_lovewar))
            add(Song("별 떨어진다 I Do", "D.O.", coverImg = R.drawable.img_album_ido))
            add(Song("mars", "도경수", coverImg = R.drawable.img_album_mars))
            add(Song("SPOT! (feat.JENNI)", "ZICO", coverImg = R.drawable.img_album_spot))
            add(Song("우산", "NCT 127", coverImg = R.drawable.img_album_umbrella))
            add(Song("Countdown (BELLE Solo)", "KISS OF LIFE", coverImg = R.drawable.img_album_countdown))
            add(Song("BFF", "TWS", coverImg = R.drawable.img_album_bff))
        }

        savedSongRVAdapter = SavedSongRVAdapter(songDatas)
    }

    private fun setupRecyclerView() {
        binding.lockerSaveSongRecyclerView.adapter = savedSongRVAdapter
        binding.lockerSaveSongRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        savedSongRVAdapter.setMyItemClickListener(object : SavedSongRVAdapter.MyItemClickListener {
            override fun onItemClick(song: Song) {
                // 아이템 클릭 시 처리할 내용
            }

            override fun onRemoveSong(position: Int) {
                savedSongRVAdapter.removeItem(position)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
