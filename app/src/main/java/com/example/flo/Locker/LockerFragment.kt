package com.example.flo.Locker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.LoginActivity
import com.example.flo.MainActivity
import com.example.flo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {

    private lateinit var binding: FragmentLockerBinding
    private lateinit var sharedPreferences: SharedPreferences

    private val information = arrayListOf("저장한 곡", "음악파일", "저장앨범")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
        sharedPreferences = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)!!

        val lockerAdapter = LockerVPAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter

        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()

        // 로그인/로그아웃 상태에 따라 텍스트 표시 변경
        initViews()

        // 로그인/로그아웃 버튼 클릭 이벤트 처리
        binding.lockerLoginTv.setOnClickListener {
            if (sharedPreferences.getString("jwt", null) == null) {
                // 로그인하지 않은 경우
                startActivity(Intent(activity, LoginActivity::class.java))
            } else {
                // 로그인한 경우
                logout()
            }
        }

        return binding.root
    }

    private fun logout() {
        val editor = sharedPreferences.edit()
        editor.remove("jwt")
        editor.apply()

        // 로그아웃 후 메인 액티비티로 이동
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish() // 로그아웃 이후 현재 액티비티를 종료합니다.
    }

    private fun initViews(){
        if (sharedPreferences.getString("jwt", null) == null){
            // 로그인하지 않은 경우
            binding.lockerLoginTv.text = "로그인"
        } else {
            // 로그인한 경우
            binding.lockerLoginTv.text = "로그아웃"
        }
    }
}
