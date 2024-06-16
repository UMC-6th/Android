package com.example.flo.ui.main.locker

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.ui.main.album.SavedAlbumFragment
import com.example.flo.ui.song.SavedSongFragment
import com.example.flo.ui.song.SongFileFragment

class LockerVPAdapter(fragment:Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SavedSongFragment()
            1 -> SongFileFragment()
            else -> SavedAlbumFragment()
        }
    }
}