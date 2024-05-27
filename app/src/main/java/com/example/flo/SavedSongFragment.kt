package com.example.flo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSavedSongBinding

class SavedSongFragment : Fragment() {

    private var _binding: FragmentSavedSongBinding? = null
    private val binding get() = _binding!!
    private val MusicItemAdapter = SavedSongRVAdapter()

    lateinit var songDB: SongDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedSongBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!

        binding.lockerSavedSongRecyclerView.adapter= MusicItemAdapter
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.lockerSavedSongRecyclerView.layoutManager= manager

        MusicItemAdapter.setSavedSongClickListener(object: SavedSongRVAdapter.SavedSongClickListener{
            override fun onRemoveSavedSong(songId: Int) {
                MusicItemAdapter.removeSong(songId)
            }

            override fun onSaveSavedSong(songId: Int) {
                MusicItemAdapter.addSong(songId)
            }
        })


        return binding.root
    }

    interface OnSelectClickListener{
        fun onSelectClick(isSelectOn: Boolean)
    }
    private var listener: OnSelectClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // MainActivity가 OnSelectClickListener 인터페이스를 구현하고 있는지 확인하고
        // 구현하고 있다면 listener로 등록합니다.
        if (context is OnSelectClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnSelectClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        // Fragment가 Activity에서 분리될 때 listener를 초기화합니다.
        listener = null
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.lockerSavedSongRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val songRVAdapter = SavedSongRVAdapter()

        songRVAdapter.setSavedSongClickListener(object : SavedSongRVAdapter. SavedSongClickListener{
            override fun onRemoveSavedSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false, songId)
            }

            override fun onSaveSavedSong(songId: Int) {
                songDB.songDao().updateIsLikeById(true, songId)
            }
        })
        binding.lockerSavedSongRecyclerView.adapter = songRVAdapter
        songRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList<Song>)
    }
}

    private fun Dummy() : ArrayList<Album>{
        val dummy1 = Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp)
        val dummy2 = Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2)
        val dummy3 = Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3)
        val dummy4 = Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4)
        val dummy5 = Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5)
        val dummy6 = Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6)

        val arr = ArrayList<Album>()
        arr.add(dummy1)
        arr.add(dummy2)
        arr.add(dummy3)
        arr.add(dummy4)
        arr.add(dummy5)
        arr.add(dummy6)

        return arr
    }

