package com.example.flo.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.fragment.LockerMusicFilesFragment
import com.example.flo.fragment.LockerSavedAlbumFragment
import com.example.flo.fragment.LockerSavedMusicFragment

class LockerVpAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> LockerSavedMusicFragment()
            1 -> LockerMusicFilesFragment()
            else -> LockerSavedAlbumFragment()
        }
    }
}