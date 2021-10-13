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
import com.example.flo.data.Song
import com.example.flo.databinding.FragmentAlbumInfoBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumInfoFragment : Fragment() {

    val tab_items : ArrayList<String> = arrayListOf("수록곡", "상세정보", "영상")

    lateinit var binding : FragmentAlbumInfoBinding
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var isfavorite : Boolean = false
        binding = FragmentAlbumInfoBinding.inflate(inflater, container, false)

        //데이터 렌더링
        setFragmentResultListener("requestKey"){ requestKey, bundle ->
            val result = bundle.getParcelable<Song>("bundleKey")

            //받아온 비트맵 데이터를 다시 drawable 데이터로 전환
            val resource : Resources = this.resources
            val drawable = BitmapDrawable(resource, result?.mainImgURL)

            binding.albumInfoMainTitleTv.text = result?.title
            binding.albumInfoMainArtistTv.text = result?.artist
            binding.albumInfoMainAlbumIv.setImageDrawable(drawable)
        }

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
            Log.d("isfavorite", isfavorite.toString())
        }

        val pagerAdapter = AlbumInfoVpAdapter(this)

        binding.albumInfoTabVp.apply {
            adapter = pagerAdapter
            overScrollMode = ViewPager2.OVER_SCROLL_NEVER
        }

        TabLayoutMediator(binding.albumInfoTabLayout, binding.albumInfoTabVp){
            tab, position -> tab.text = tab_items[position]
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