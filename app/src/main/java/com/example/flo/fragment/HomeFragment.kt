package com.example.flo.fragment

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.activity.MainActivity
import com.example.flo.R
import com.example.flo.adapter.HomeAdBannerAdapter
import com.example.flo.adapter.HomeMainPannelAdapter
import com.example.flo.data.Song
import com.example.flo.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var pagerHandler = Handler(Looper.getMainLooper())
    private val tabItems : ArrayList<String> = arrayListOf("","","","","")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeTodayAlbum1Layout.setOnClickListener{
            //프래그먼트로 넘겨줄 데이터 렌더링
            val title = binding.homeTodayAlbum1TitleTv.text
            val artist = binding.homeTodayAlbum1ArtistTv.text

            //이미지의 경우 drawable 데이터를 비트맵 데이터로 전환
//            val drawable = binding.homeTodayAlbum1Iv.drawable as BitmapDrawable
//            val bitmap = drawable.bitmap

            val songData = Song(title.toString(), artist.toString(), false, "music_lilac",0, 0, 0, R.drawable.img_album_exp2)

            setFragmentResult("requestKey", bundleOf("bundleKey" to songData))

            (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, AlbumInfoFragment())
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
        }

        val swiper = AutoPannelSwipe()
        swiper.start()

        // 배너광고 뷰페이저
        val homeAdBannerAdapter = HomeAdBannerAdapter(this)
        homeAdBannerAdapter.addFragment(HomeAdBannerFragmnet(R.drawable.img_home_viewpager_exp))
        homeAdBannerAdapter.addFragment(HomeAdBannerFragmnet(R.drawable.img_home_viewpager_exp2))
        homeAdBannerAdapter.addFragment(HomeAdBannerFragmnet(R.drawable.img_home_viewpager_exp))
        homeAdBannerAdapter.addFragment(HomeAdBannerFragmnet(R.drawable.img_home_viewpager_exp2))
        homeAdBannerAdapter.addFragment(HomeAdBannerFragmnet(R.drawable.img_home_viewpager_exp))
        binding.homeAdBannerVp.apply {
            adapter = homeAdBannerAdapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        // 매인 패널 뷰페이저
        val homeMainPannelAdapter = HomeMainPannelAdapter(this)
        homeMainPannelAdapter.addFragment(HomeMainPannelFragment(R.drawable.img_default_4_x_1, "포근하게 덮어주는 꿈의 목소리"))
        homeMainPannelAdapter.addFragment(HomeMainPannelFragment(R.drawable.img_home_pannel_1, "스카이웨이 드라이브 감성뮤직"))
        homeMainPannelAdapter.addFragment(HomeMainPannelFragment(R.drawable.img_home_pannel_2, "지친 하루에 차 안에서 듣는 위로"))
        homeMainPannelAdapter.addFragment(HomeMainPannelFragment(R.drawable.img_home_pannel_3, "집중이 필요할 때, 내 방 도서관"))
        homeMainPannelAdapter.addFragment(HomeMainPannelFragment(R.drawable.img_home_pannel_4, "연인과 밤 산책하며 듣는 감성 팝"))

        binding.homeMainPannelVp.apply {
            adapter = homeMainPannelAdapter
            overScrollMode = ViewPager2.OVER_SCROLL_NEVER
        }

        TabLayoutMediator(binding.homeMainPannelIndicator, binding.homeMainPannelVp){
            tab, position -> tab.text = tabItems[position]
        }.attach()
        return binding.root
    }

    inner class AutoPannelSwipe : Thread(){
        override fun run() {
            try {
                while (true){
                    sleep(4000)
                    pagerHandler.post{
                        var position = binding.homeMainPannelVp.currentItem
                        Log.d("position", position.toString())
                        if(position == 4){
                            position = 0
                            binding.homeMainPannelVp.setCurrentItem(position)
                        } else {
                            position++
                            binding.homeMainPannelVp.setCurrentItem(position)
                        }
                    }
                }
            } catch (e: InterruptedException){
                Log.d("interrupt", "쓰레드 종료")
            }
        }
    }

}