package com.example.flo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.R
import com.example.flo.activity.MainActivity
import com.example.flo.databinding.FragmentHomeMainPannelBinding

class HomeMainPannelFragment(val imgRes : Int, val title : String) : Fragment() {
    lateinit var binding : FragmentHomeMainPannelBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeMainPannelBinding.inflate(inflater, container, false)

//        binding.homeBackgroundIv.setOnClickListener{
//            (context as MainActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.main_frm, AlbumInfoFragment())
//                .addToBackStack(null)
//                .commitAllowingStateLoss()
//        }

        binding.homeBackgroundIv.setImageResource(imgRes)
        binding.homeBackgroundTitleTv.text = title
        return binding.root
    }
}