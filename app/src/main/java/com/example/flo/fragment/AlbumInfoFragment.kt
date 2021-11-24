package com.example.flo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.activity.MainActivity
import com.example.flo.activity.SongActivity
import com.example.flo.adapter.AlbumInfoVpAdapter
import com.example.flo.data.Album
import com.example.flo.data.SongDB
import com.example.flo.databinding.FragmentAlbumInfoBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumInfoFragment : Fragment() {

    val tabItems : ArrayList<String> = arrayListOf("수록곡", "상세정보", "영상")
    lateinit var binding : FragmentAlbumInfoBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentAlbumInfoBinding.inflate(inflater, container, false)
        val songDB = SongDB.getInstance(context as MainActivity)!!

        //데이터 렌더링
        val albumInfo = arguments?.getParcelable<Album>("albumInfo")

        // 뷰 초기화
        binding.albumInfoMainTitleTv.text = albumInfo?.title
        binding.albumInfoMainArtistTv.text = albumInfo?.artist
        binding.albumInfoMainAlbumIv.setImageResource(albumInfo?.coverImg!!)

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
            setFavoriteBtn(albumInfo.isLike, albumInfo.title, songDB)
        }

        val pagerAdapter = AlbumInfoVpAdapter(this, albumInfo.title)

        binding.albumInfoTabVp.apply {
            adapter = pagerAdapter
            overScrollMode = ViewPager2.OVER_SCROLL_NEVER
        }

        TabLayoutMediator(binding.albumInfoTabLayout, binding.albumInfoTabVp){
            tab, position -> tab.text = tabItems[position]
        }.attach()

        return binding.root
    }

    private fun setFavoriteBtn(isLike : Boolean, title : String, songDB: SongDB) {
        if (isLike) {
            binding.albumInfoIcFavoriteOffIv.visibility = View.VISIBLE
            binding.albumInfoIcFavoriteOnIv.visibility = View.GONE

            songDB.AlbumDao().setIsLike(false, title)
        } else {
            binding.albumInfoIcFavoriteOffIv.visibility = View.GONE
            binding.albumInfoIcFavoriteOnIv.visibility = View.VISIBLE

            songDB.AlbumDao().setIsLike(true, title)
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