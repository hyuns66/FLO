package com.example.flo.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.fragment.AlbumInfoContainedSongFragment
import com.example.flo.fragment.AlbumInfoMoreInfoFragment
import com.example.flo.fragment.AlbumInfoMusicVideosFragment

class AlbumInfoVpAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> AlbumInfoContainedSongFragment()

            1 -> AlbumInfoMoreInfoFragment()

            else -> AlbumInfoMusicVideosFragment()
        }
    }
}