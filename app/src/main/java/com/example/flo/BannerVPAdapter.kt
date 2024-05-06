package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(fragment: Fragment) :FragmentStateAdapter(fragment) {

    private val fragmentlist : ArrayList<Fragment> = ArrayList()
    override fun getItemCount(): Int = fragmentlist.size //전달 데이터 개수
    override fun createFragment(position: Int): Fragment = fragmentlist[position] //fragment 생성 함수
    fun addFragment(fragment: Fragment) {
        fragmentlist.add(fragment) //fragmentlist에 인자값으로 받은 fragment를 추가
        notifyItemInserted(fragmentlist.size-1) //list안에 새로운 값이 추가됐을 때 viewpager에게 새로운 값이 추가된 것을 알려줌
    }
}