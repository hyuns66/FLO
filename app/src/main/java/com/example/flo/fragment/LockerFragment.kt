package com.example.flo.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.activity.LogInActivity
import com.example.flo.activity.MainActivity
import com.example.flo.adapter.LockerVpAdapter
import com.example.flo.databinding.FragmentLockerBinding
import com.example.flo.getUserIdx
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    private val tabItems = arrayListOf("저장한 곡", "음악파일", "저장한 앨범")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
        initView()

        val pagerAdapter = LockerVpAdapter(this)
        binding.lockerVp.adapter = pagerAdapter
        binding.lockerVp.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        TabLayoutMediator(binding.lockerTabLayout, binding.lockerVp){
            tab, position -> tab.text = tabItems[position]
        }.attach()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    private fun initView(){
        val userIdx = getUserIdx(requireContext())

        if(userIdx == 0){
            binding.lockerLoginTv.text = "로그인"
            binding.lockerNameTv.visibility = View.GONE

            binding.lockerLoginTv.setOnClickListener {
                val intent = Intent(activity, LogInActivity::class.java)
                startActivity(intent)
            }
        } else {
            binding.lockerLoginTv.text = "로그아웃"
            binding.lockerNameTv.visibility = View.VISIBLE

            binding.lockerLoginTv.setOnClickListener {
                binding.lockerLoginTv.text = "로그인"
                binding.lockerNameTv.visibility = View.GONE
                logOut()
            }
        }
    }

    private fun logOut() {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()

        editor.remove("jwt")
        editor.apply()

        binding.lockerLoginTv.setOnClickListener {
            val intent = Intent(activity, LogInActivity::class.java)
            startActivity(intent)
        }
    }

}