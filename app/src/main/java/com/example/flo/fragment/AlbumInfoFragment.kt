package com.example.flo.fragment

import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.R
import com.example.flo.adapter.AlbumInfoVpAdapter
import com.example.flo.data.HomeAlbum
import com.example.flo.data.Song
import com.example.flo.databinding.FragmentAlbumInfoBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumInfoFragment : Fragment() {

    val gson : Gson = Gson()
    val tabItems : ArrayList<String> = arrayListOf("수록곡", "상세정보", "영상")
    lateinit var binding : FragmentAlbumInfoBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var isfavorite : Boolean = false
        binding = FragmentAlbumInfoBinding.inflate(inflater, container, false)

        //데이터 렌더링
        val albumData = arguments?.getString("albumData")
        val homeAlbum = gson.fromJson(albumData, HomeAlbum::class.java)

        // 뷰 초기화
        binding.albumInfoMainTitleTv.text = homeAlbum?.title
        binding.albumInfoMainArtistTv.text = homeAlbum?.artist
        binding.albumInfoMainAlbumIv.setImageResource(homeAlbum?.coverImg!!)

        binding.albumInfoMainTitleTv.isSelected = true

        //터치 이벤트를 위해 레이아웃 최상단에 위치시키는 작업
        binding.albumInfoIcBackIv.bringToFront()
        binding.albumInfoIcFavorite.bringToFront()
        binding.albumInfoIcMoreIv.bringToFront()

        //뒤로가기 버튼
        binding.albumInfoIcBackIv.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        //하트버튼
        binding.albumInfoIcFavorite.setOnClickListener{
            setFavoriteBtn(isfavorite)
            isfavorite = setFavoriteBtn(isfavorite)
        }

        val pagerAdapter = AlbumInfoVpAdapter(this)

        binding.albumInfoTabVp.apply {
            adapter = pagerAdapter
            overScrollMode = ViewPager2.OVER_SCROLL_NEVER
        }

        TabLayoutMediator(binding.albumInfoTabLayout, binding.albumInfoTabVp){
            tab, position -> tab.text = tabItems[position]
        }.attach()

        return binding.root
    }

    fun setFavoriteBtn(isfavorite : Boolean) : Boolean {
        if (isfavorite == true) {
            binding.albumInfoIcFavoriteOffIv.visibility = View.VISIBLE
            binding.albumInfoIcFavoriteOnIv.visibility = View.GONE
            return false
        } else {
            binding.albumInfoIcFavoriteOffIv.visibility = View.GONE
            binding.albumInfoIcFavoriteOnIv.visibility = View.VISIBLE
            return true
        }
    }

    fun setImageView(imgRes : Int) {
        binding.albumInfoMainAlbumIv.setImageResource(imgRes)
    }

    fun setTextView(type : String, text : String){
        when (type) {
            "title" -> {
                binding.albumInfoMainTitleTv.text = text
            }
            "artist" -> {
                binding.albumInfoMainArtistTv.text = text
            }
        }
    }
}