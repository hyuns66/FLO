package com.example.flo.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.fragment.ContainedSongFragment
import com.example.flo.fragment.ExeFragment
import com.example.flo.fragment.MoreInfoFragment
import com.example.flo.fragment.MusicVideosFragment

class AlbumInfoVpAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> ContainedSongFragment()

            1 -> MoreInfoFragment()

            else -> MusicVideosFragment()
        }
    }
}