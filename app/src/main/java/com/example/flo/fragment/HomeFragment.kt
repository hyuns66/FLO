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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.activity.MainActivity
import com.example.flo.R
import com.example.flo.adapter.HomeAdBannerAdapter
import com.example.flo.adapter.HomeAlbumRvAdapter
import com.example.flo.adapter.HomeMainPannelAdapter
import com.example.flo.data.HomeAlbum
import com.example.flo.data.Song
import com.example.flo.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var gson : Gson = Gson()
    private var pagerHandler = Handler(Looper.getMainLooper())
    private var albumDatas = arrayListOf<HomeAlbum>()
    private val tabItems : ArrayList<String> = arrayListOf("","","","","")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // 메인 패널 자동 스와이프 스레드 생성
        val swiper = AutoPannelSwipe()
        swiper.start()

        // 데이터리스트 더미데이터 생성
        albumDatas.clear()
        albumDatas.apply {
            add(HomeAlbum("LILAC", "아이유(IU)", R.drawable.img_album_exp2))
            add(HomeAlbum("strawberry moon", "아이유(IU)", R.drawable.img_album_exp3))
            add(HomeAlbum("Savage", "에스파(asepa)", R.drawable.img_album_exp4))
            add(HomeAlbum("Weekend", "태연(TAEYEON)", R.drawable.img_album_exp5))
        }

        // 오늘 발매음악 리사이클러뷰 어댑터 연결
        val todayPublishedAdapter = HomeAlbumRvAdapter(albumDatas)
        binding.homeTodayPublishedAlbumRv.apply {
            adapter = todayPublishedAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        todayPublishedAdapter.setItemClickListener(object : HomeAlbumRvAdapter.ItemClickListener{
            override fun onItemClick(homeAlbum: HomeAlbum) {
                changeAlbumFragment(homeAlbum)
            }
        })


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

    private fun changeAlbumFragment(homeAlbum: HomeAlbum) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumInfoFragment().apply {
                    arguments = Bundle().apply {
                        val albumJson = gson.toJson(homeAlbum)
                        putString("albumData", albumJson)
                    }
                })
                .addToBackStack(null)
                .commitAllowingStateLoss()
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