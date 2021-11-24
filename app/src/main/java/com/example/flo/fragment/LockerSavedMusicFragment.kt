package com.example.flo.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.adapter.LockerSavedMusicRvAdapter
import com.example.flo.data.Song
import com.example.flo.data.SongDB
import com.example.flo.databinding.FragmentLockerSavedMusicBinding

class LockerSavedMusicFragment : Fragment() {

    var savedSongs = arrayListOf<Song>()
    lateinit var binding : FragmentLockerSavedMusicBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentLockerSavedMusicBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        Log.d("state", "LockerSavedMusic onStart()")

        super.onStart()
    }

    override fun onResume() {
        Log.d("state", "LockerSavedMusic onResume()")

        val songDB = SongDB.getInstance(requireContext())!!
        savedSongs = songDB.SongDao().getIsLikedSongs(true) as ArrayList<Song>

        val savedMusicAdapter = LockerSavedMusicRvAdapter()
        binding.lockerSavedMusicRv.apply {
            savedMusicAdapter.addSongs(savedSongs)
            adapter = savedMusicAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        savedMusicAdapter.setItemClickListener(object : LockerSavedMusicRvAdapter.ItemClickListener{
            override fun removeSong(music : String) {
                songDB.SongDao().updateIsLikeByMusic(false, music)
            }
        })

        super.onResume()
    }
}