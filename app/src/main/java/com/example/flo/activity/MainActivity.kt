package com.example.flo.activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.flo.fragment.HomeFragment
import com.example.flo.fragment.LockerFragment
import com.example.flo.fragment.LookFragment
import com.example.flo.fragment.SearchFragment
import com.example.flo.R
import com.example.flo.data.Song
import com.example.flo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    var isLike = false
    var isUnlike = false
    var isMixed = false
    private var song : Song = Song()
    lateinit var player : Player
    var mediaPlayer: MediaPlayer? = null
    lateinit var binding: ActivityMainBinding
    var backPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()

        song = Song("LILAC", "아이유(IU)", false, "music_lilac", 215, 0, 0, null)
        setPlayerStatus(song)

        if(intent.hasExtra("isPlaying") && intent.hasExtra("isLike") &&intent.hasExtra("isUnlike")
                &&intent.hasExtra("isMixed") && intent.hasExtra("playTime") && intent.hasExtra("title")
                &&intent.hasExtra("artist") && intent.hasExtra("music")){
            song.isPlaying = intent.getBooleanExtra("isPlaying", false)
            song.currentMillis = intent.getIntExtra("currentMillis", 0)
            Log.d("currentMilils", song.currentMillis.toString())
            song.title = intent.getStringExtra("title")!!
            song.artist = intent.getStringExtra("artist")!!
            song.music = intent.getStringExtra("music")!!
            song.playTime = intent.getIntExtra("playTime", 215)
            song.musicRepeatMode = intent.getIntExtra("musicRepeatMode", 0)
            isLike = intent.getBooleanExtra("isLike", false)
            isUnlike = intent.getBooleanExtra("isUnlike", false)
            isMixed = intent.getBooleanExtra("isMixed", false)
        }

        // 플레이어 뷰 초기화
        if(song.isPlaying){
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }
        binding.mainPlayTimeBar.progress = song.currentMillis/song.playTime
        binding.mainPlayTimeBar.isEnabled = false

        // 스레드 생성, 시작
        player = Player(song.playTime, song.currentMillis, song.isPlaying)
        player.start()

        var music = resources.getIdentifier(song.music, "raw", this.packageName)



        // 플레이, 정지 버튼
        binding.mainMiniplayerBtn.setOnClickListener{
            setPlayerStatus(song)
            song.isPlaying = true
            player.isPlaying = true
            if(song.musicRepeatMode == 0 && player.state == Thread.State.TERMINATED){
                    binding.mainPlayTimeBar.progress = 0
                    player = Player(song.playTime, 0, song.isPlaying)
                    player.start()
            }
        }

        binding.mainPauseBtn.setOnClickListener{
            setPlayerStatus(song)
            song.isPlaying = false
            player.isPlaying = false
            Log.d("isPlaying", song.isPlaying.toString())
        }

        // SongActivity intent
        binding.mainPlayerLayout.setOnClickListener{
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("artist", song.artist)
            intent.putExtra("isPlaying", song.isPlaying)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("music", song.music)
            intent.putExtra("musicRepeatMode", song.musicRepeatMode)
            intent.putExtra("currentMillis", player.millis)
            intent.putExtra("isLike", isLike)
            intent.putExtra("isUnlike", isUnlike)
            intent.putExtra("isMixed", isMixed)
            startActivity(intent)
        }

        // 바텀 네비게이션 뷰
        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.popBackStack("homeFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .addToBackStack("homeFragment")
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.popBackStack("lookFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .addToBackStack("lookFragment")
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.popBackStack("searchFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .addToBackStack("searchFragment")
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.popBackStack("lockerFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .addToBackStack("lockerFragment")
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }

    }

    fun setPlayerStatus(song: Song) : Boolean{
        binding.mainPlayerArtistTv.text = song.artist
        binding.mainPlayerTitleTv.text = song.title
        if(song.isPlaying){
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
            mediaPlayer?.pause()
            Log.d("pause", "Paused")
            return false
        } else {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
            return true
        }
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 0){
            if(System.currentTimeMillis() > backPressedTime + 2000){
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show()
                return
            } else {
                finish()
            }
        }
        Log.d("fragmentStack", supportFragmentManager.backStackEntryCount.toString())
        super.onBackPressed()
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()
    }

    override fun onDestroy() {
        player.interrupt()
        super.onDestroy()
    }

    inner class Player(private val playTime : Int, private val currentMillis : Int, var isPlaying : Boolean) : Thread(){
        var millis = currentMillis

        override fun run() {
            try {
                while (true){
                    if (millis/1000 >= playTime){
                        if(song.musicRepeatMode == 0){
                            runOnUiThread{
                                setPlayerStatus(song)
                                song.isPlaying = setPlayerStatus(song)
                                player.isPlaying = song.isPlaying
                            }
                            break
                        } else {
                            millis = 0
                        }
                        continue
                    } else {
                        if(isPlaying){
                            sleep(1)
                            millis++

                            runOnUiThread{
                                binding.mainPlayTimeBar.progress = millis/playTime
                            }
                        }
                    }
                }
            } catch (e : InterruptedException){
                Log.d("interrupt", "쓰레드 종료")
            }
        }
    }

}

