package com.example.flo.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.adapter.LockerVpAdapter
import com.example.flo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    private val tabItems = arrayListOf("저장한 곡", "음악파일")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val pagerAdapter = LockerVpAdapter(this)
        binding.lockerVp.adapter = pagerAdapter
        binding.lockerVp.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        TabLayoutMediator(binding.lockerTabLayout, binding.lockerVp){
            tab, position -> tab.text = tabItems[position]
        }.attach()

        return binding.root
    }


}