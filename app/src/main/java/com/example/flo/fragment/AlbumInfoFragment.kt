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
import com.example.flo.R
import com.example.flo.adapter.AlbumInfoVpAdapter
import com.example.flo.data.Song
import com.example.flo.databinding.FragmentAlbumInfoBinding

class AlbumInfoFragment : Fragment() {

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

        // 내 취향 MIX 버튼
//        binding.albumInfoFavoriteMix.setOnClickListener{
//            if(binding.albumInfoFavoriteMixBtnOffIv.visibility == View.VISIBLE){
//                binding.albumInfoFavoriteMixBtnOnIv.visibility = View.VISIBLE
//                binding.albumInfoFavoriteMixBtnOffIv.visibility = View.GONE
//                binding.albumInfoMainAlbumIv.setImageResource(R.drawable.eminem_favorite_mix)
//                binding.albumInfoMainTitleTv.text = "Love The Way You Lie (피처링: Rihanna(리아나))"
//                binding.albumInfoMainArtistTv.text = "Eminem(에미넴)"
//            } else {
//                binding.albumInfoFavoriteMixBtnOnIv.visibility = View.GONE
//                binding.albumInfoFavoriteMixBtnOffIv.visibility = View.VISIBLE
//                binding.albumInfoMainAlbumIv.setImageResource(R.drawable.img_album_exp2)
//                binding.albumInfoMainTitleTv.text = "LILAC"
//                binding.albumInfoMainArtistTv.text = "아이유(IU)"
//            }
//        }

        val pagerAdapter = AlbumInfoVpAdapter(this)
        binding.albumInfoTabVp.adapter = pagerAdapter

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
}