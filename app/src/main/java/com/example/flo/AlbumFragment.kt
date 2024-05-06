package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment :Fragment(){

    lateinit var binding : FragmentAlbumBinding

    private val information = arrayListOf("수록곡", "상세정보", "영상")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater,container,false)

        val title =arguments?.getString("albumTitle")
        val singer =arguments?.getString("albumSinger")

        binding.albumMusicTitleTv.text= title
        binding.albumSingerNameTv.text= singer

        binding.albumBackIv.setOnClickListener{
            val homeFragment = HomeFragment()
            val transaction =parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frm, homeFragment)
                .addToBackStack(null)
                .commit()
        }

        val albumAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) {
            tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }
}