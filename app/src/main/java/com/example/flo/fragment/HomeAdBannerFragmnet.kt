package com.example.flo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentHomeAdBannerBinding

class HomeAdBannerFragmnet(val ImgRes : Int) : Fragment() {
    lateinit var binding : FragmentHomeAdBannerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeAdBannerBinding.inflate(inflater, container, false)

        binding.homeAdBannerIv.setImageResource(ImgRes)
        return binding.root
    }
}