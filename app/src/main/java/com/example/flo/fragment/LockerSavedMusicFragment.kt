package com.example.flo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.R
import com.example.flo.adapter.LockerSavedMusicRvAdapter
import com.example.flo.data.HomeAlbum
import com.example.flo.databinding.FragmentLockerSavedMusicBinding

class LockerSavedMusicFragment : Fragment() {
    private var savedAlbumList = arrayListOf<HomeAlbum>()
    lateinit var binding : FragmentLockerSavedMusicBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentLockerSavedMusicBinding.inflate(inflater, container, false)

        savedAlbumList.clear()
        savedAlbumList.apply {
            add(HomeAlbum("LILAC", "아이유(IU)", R.drawable.img_album_exp2))
            add(HomeAlbum("strawberry moon", "아이유(IU)", R.drawable.img_album_exp3))
            add(HomeAlbum("Savage", "에스파(asepa)", R.drawable.img_album_exp4))
            add(HomeAlbum("Weekend", "태연(TAEYEON)", R.drawable.img_album_exp5))
        }

        val savedMusicAdapter = LockerSavedMusicRvAdapter(savedAlbumList)
        binding.lockerSavedMusicRv.apply {
            adapter = savedMusicAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        return binding.root
    }
}