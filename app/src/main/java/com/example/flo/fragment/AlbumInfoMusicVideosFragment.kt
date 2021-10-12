package com.example.flo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumInfoMusicVideosBinding

class AlbumInfoMusicVideosFragment : Fragment(){

    lateinit var binding : FragmentAlbumInfoMusicVideosBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentAlbumInfoMusicVideosBinding.inflate(inflater, container, false)

        return binding.root
    }
}