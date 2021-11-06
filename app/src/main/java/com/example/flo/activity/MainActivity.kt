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
import com.example.flo.service.MusicPlayerService
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    var isLike = false
    var isUnlike = false
    var isMixed = false
    private var song : Song = Song()
    private var gson : Gson = Gson()
    private var mediaPlayer : MediaPlayer? = null
    lateinit var player : Player
    lateinit var binding: ActivityMainBinding
    var backPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d("state", "Main onCreate()")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()

        val serviceIntent = Intent(this,MusicPlayerService::class.java)
        // sharedPreferences
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("song", null)
        song = if(songJson == null) {
            Song("LILAC", "아이유(IU)", false, "music_lilac", 215, 0, 0, R.drawable.img_album_exp2)
        } else {
            gson.fromJson(songJson, Song::class.java)
        }

        // 뷰 초기화
        binding.mainPlayTimeBar.isEnabled = false
        binding.mainMiniplayerBtn.visibility = View.VISIBLE
        binding.mainPauseBtn.visibility = View.GONE
        // 스레드 생성, 시작
        player = Player(song.playTime, song.currentMillis, song.isPlaying, serviceIntent)
        player.start()

        // 플레이, 정지 버튼
        binding.mainMiniplayerBtn.setOnClickListener{
            setPlayerStatus(song)
            song.isPlaying = true
            player.isPlaying = true
            if(song.musicRepeatMode == 0 && player.state == Thread.State.TERMINATED){
                if (binding.mainPlayTimeBar.progress == 1000){
                    binding.mainPlayTimeBar.progress = 0
                    song.currentMillis = 0
                    player = Player(song.playTime, song.currentMillis, song.isPlaying, serviceIntent)
                    player.start()
                } else {
                    song.currentMillis = (binding.mainPlayTimeBar.progress)*(song.playTime)
                }
            }
            serviceIntent.putExtra("musicService", song.music)
            serviceIntent.putExtra("millisService", player.millis)
            startService(serviceIntent)
        }

        binding.mainPauseBtn.setOnClickListener{
            setPlayerStatus(song)
            song.isPlaying = false
            player.isPlaying = false
            val intent = Intent(this, MusicPlayerService::class.java)
            stopService(intent)
        }

        // SongActivity intent
        binding.mainPlayerLayout.setOnClickListener{
            mediaPlayer?.pause()
            mediaPlayer?.release()
            mediaPlayer = null

            val intent = Intent(this, SongActivity::class.java)
            song.currentMillis = player.millis

            intent.putExtra("songJson", song)
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
            return false
        } else {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
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
                mediaPlayer?.pause()
                mediaPlayer?.release()
                mediaPlayer = null

                finish()
            }
        }
        super.onBackPressed()
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()
    }

    override fun onNewIntent(intent: Intent?) {
        Log.d("state", "Main onNewIntent()")
        if(intent != null){
            song = intent.getParcelableExtra("songJson")!!
            isLike = intent.getBooleanExtra("isLike", false)
            isUnlike = intent.getBooleanExtra("isUnlike", false)
            isMixed = intent.getBooleanExtra("isMixed", false)
        }
        super.onNewIntent(intent)
    }

    override fun onResume() {
        Log.d("state", "Main onResume()")

        // 플레이어 뷰 초기화
        if(song.isPlaying){
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }
        player.millis = song.currentMillis
        player.isPlaying = song.isPlaying

        binding.mainPlayTimeBar.progress = song.currentMillis/song.playTime
        super.onResume()
    }
    override fun onStart() {
        Log.d("state", "Main onStart()")

        super.onStart()
    }

    override fun onRestart() {
        Log.d("state","Main onRestart()")
        super.onRestart()
    }

    override fun onStop() {
        Log.d("state","Main onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("state","Main onDestroy()")
        mediaPlayer?.pause()
        mediaPlayer?.release()
        mediaPlayer = null
        player.isPlaying = false
        song.isPlaying = false
        song.currentMillis = player.millis
        // sharedPreferences
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val songJson = gson.toJson(song)
        editor.putString("song", songJson)
        editor.apply()
        player.interrupt()
        super.onDestroy()
    }

    inner class Player(private val playTime : Int, private val currentMillis : Int, var isPlaying : Boolean , val intent: Intent) : Thread(){
        var millis = currentMillis

        override fun run() {
            try {
                while (true){
                    if (millis/1000 >= playTime){
                        stopService(intent)
                        if(song.musicRepeatMode == 0){
                            runOnUiThread{
                                setPlayerStatus(song)
                                song.isPlaying = setPlayerStatus(song)
                                player.isPlaying = song.isPlaying
                            }
                            break
                        } else {
                            millis = 0
                            intent.putExtra("musicService", song.music)
                            intent.putExtra("millisService", millis)
                            startService(intent)
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

