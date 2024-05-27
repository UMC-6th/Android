package com.example.flo

import SongDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentLockerSavedsongBinding

class SavedSongFragment : Fragment() {
    lateinit var binding: FragmentLockerSavedsongBinding
    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedsongBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }
    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.lockerSavedSongRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val likedSongs = songDB.songDao().getLikedSongs(true) as ArrayList<Song>
        val songRVAdapter = SavedSongRVAdapter(likedSongs)  // 어댑터에 데이터 전달

        songRVAdapter.setMyItemClickListener(object : SavedSongRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false,songId)
        }})

        binding.lockerSavedSongRecyclerView.adapter = songRVAdapter
        songRVAdapter.addSongs(likedSongs.filter { it.isLike } as ArrayList<Song>)

    }
}
