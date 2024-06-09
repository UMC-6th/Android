package com.example.flo.Locker

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.SavedAlbumFragment
import com.example.flo.SavedSong.SavedSongFragment
import com.example.flo.SongFileFragment

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