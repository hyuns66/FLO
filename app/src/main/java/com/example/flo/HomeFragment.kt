package com.example.flo

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
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

        binding.homeTodayAlbum1Layout.setOnClickListener{
            //프래그먼트로 넘겨줄 데이터 렌더링
            val title = binding.homeTodayAlbum1TitleTv.text
            val artist = binding.homeTodayAlbum1ArtistTv.text

            //이미지의 경우 drawable 데이터를 비트맵 데이터로 전환
            val drawable = binding.homeTodayAlbum1Iv.drawable as BitmapDrawable
            val bitmap = drawable.bitmap

            val songData = Song(title.toString(), artist.toString(), bitmap)

            setFragmentResult("requestKey", bundleOf("bundleKey" to songData))

            (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, AlbumInfoFragment())
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
        }

        return binding.root
    }


}