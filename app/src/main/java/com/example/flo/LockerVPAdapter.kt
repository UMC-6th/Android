package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    // ViewPager2에서 표시할 프래그먼트의 수를 반환
    override fun getItemCount(): Int {
        return 2 // 저장한 곡과 음악파일, 두 개의 탭
    }

    // 각 포지션에 맞는 프래그먼트를 반환
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SavedSongFragment() // 첫 번째 탭: 저장한 곡
            1 -> MusicFileFragment() // 두 번째 탭: 음악 파일
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
