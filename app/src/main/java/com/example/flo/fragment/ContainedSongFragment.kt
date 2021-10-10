package com.example.flo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentContainedSongBinding

class ContainedSongFragment : Fragment() {

    lateinit var binding : FragmentContainedSongBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentContainedSongBinding.inflate(inflater, container, false)

        return binding.root
    }
}