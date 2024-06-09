package com.example.flo.Indicator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentBannerBinding
import com.example.flo.databinding.FragmentIndicatorBinding

class IndicatorFragment(val imgRes : Int): Fragment() {

    lateinit var binding : FragmentIndicatorBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIndicatorBinding.inflate(inflater,container,false)

        binding.indicatorImageIv.setImageResource(imgRes)
        return binding.root
    }
}
