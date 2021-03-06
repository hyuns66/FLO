package com.example.flo.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.fragment.AlbumInfoContainedSongFragment
import com.example.flo.fragment.AlbumInfoMoreInfoFragment
import com.example.flo.fragment.AlbumInfoMusicVideosFragment

class AlbumInfoVpAdapter(fragment : Fragment, title : String) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3
    val mTitle = title

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> AlbumInfoContainedSongFragment(mTitle)

            1 -> AlbumInfoMoreInfoFragment()

            else -> AlbumInfoMusicVideosFragment()
        }
    }
}