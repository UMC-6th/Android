package com.example.flo.ui.main.album

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.ui.song.SongFragment

class AlbumVPAdapter(fragment:Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){      //when은 조건에 따라 다른 작업을 해줄 수 있도록 하는 문법
            0 -> SongFragment()   // 괄호 안에는 조건을 써주고 또 다른 괄호안에는 조건에 따라 하는 행동을 적어줌 (=swith문)
            1 -> DetailFragment()
            else -> VideoFragment()
        }
    }
}