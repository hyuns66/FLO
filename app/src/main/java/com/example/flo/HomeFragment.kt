package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeBackgroundIv.setOnClickListener{
            parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, AlbumInfoFragment())
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
        }

        return binding.root
    }


}