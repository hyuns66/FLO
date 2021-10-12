package com.example.flo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.R
import com.example.flo.databinding.FragmentAlbumInfoContainedSongBinding

class AlbumInfoContainedSongFragment : Fragment() {

    lateinit var binding : FragmentAlbumInfoContainedSongBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAlbumInfoContainedSongBinding.inflate(inflater, container, false)

          val fragment = parentFragment as AlbumInfoFragment

        // 내 취향 MIX 버튼
        binding.albumInfoFavoriteMix.setOnClickListener{
            if(binding.albumInfoFavoriteMixBtnOffIv.visibility == View.VISIBLE){
                binding.albumInfoFavoriteMixBtnOnIv.visibility = View.VISIBLE
                binding.albumInfoFavoriteMixBtnOffIv.visibility = View.GONE
                fragment.setTextView("title", "Love The Way You Lie (피처링: Rihanna(리아나))")
                fragment.setTextView("artist", "Eminem(에미넴)")
                fragment.setImageView(R.drawable.eminem_favorite_mix)
            } else {
                binding.albumInfoFavoriteMixBtnOnIv.visibility = View.GONE
                binding.albumInfoFavoriteMixBtnOffIv.visibility = View.VISIBLE
                fragment.setTextView("title", "LILAC")
                fragment.setTextView("artist", "아이유(IU)")
                fragment.setImageView(R.drawable.img_album_exp2)
            }
        }

        return binding.root
    }
}