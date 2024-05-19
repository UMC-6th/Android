package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    private var albumDatas = ArrayList<Album>()

    private val information = arrayListOf("저장한 곡", "음악파일")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerVPAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter

        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()

        albumDatas.apply {
            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_1))
            add(Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3))
            add(Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4))
            add(Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5))
            add(Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6))
        }

        // 더미데이터랑 Adapter 연결
        val albumLockerRVAdapter = AlbumLockerRVAdapter(albumDatas)


        // 리사이클러뷰에 어댑터를 연결
        binding.lockerSavedSongRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        // 리스너 설정
        albumLockerRVAdapter.setMyItemClickListener(object : AlbumLockerRVAdapter.MyItemClickListener {

            fun onItemClick(album: Album){
                changeSongfragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumLockerRVAdapter.removeItem(position)
            }
        })

        return binding.root
    }

    private fun changeSongfragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()

    }
}
