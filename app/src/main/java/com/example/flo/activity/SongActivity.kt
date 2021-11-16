package com.example.flo.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.data.Song
import com.example.flo.databinding.ActivitySongBinding
import com.example.flo.service.MediaPlayerService
import com.google.gson.Gson

class SongActivity : AppCompatActivity(){

    var isMixed = false
    var isLike = false
    var isUnlike = false
    var isPlaying = false
    var musicRepeatMode = 0
    private var song : Song = Song()
    private lateinit var mediaPlayerService : MediaPlayerService
    private var isServiceBound = false
    private lateinit var player : Player
    lateinit var binding : ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 데이터 렌더링
        if(intent.hasExtra("songJson") && intent.hasExtra("isLike")
                && intent.hasExtra("isMixed") && intent.hasExtra("isUnlike")){
            song = intent.getParcelableExtra<Song>("songJson")!!

            binding.songTitleTv.text = song.title
            binding.songArtistTv.text = song.artist
            isPlaying = intent.getBooleanExtra("isPlaying", false)
            isLike = intent.getBooleanExtra("isLike", false)
            isUnlike = intent.getBooleanExtra("isUnlike", false)
            isMixed = intent.getBooleanExtra("isMixed", false)
        }

        Log.d("playtime", song.playTime.toString())
        // Player() 스레드 생성
        val serviceIntent = Intent(this,MediaPlayerService::class.java)
        player = Player(song.playTime, song.currentMillis,  isPlaying)
        player.start()
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)

        // 뷰 초기화
        setIconStatus(isPlaying, binding.songPlayerPauseBtnIv, binding.songPlayerPlayBtnIv)
        setIconStatus(isMixed, binding.songPlayerRandomBtnOnIv, binding.songPlayerRandomBtnOffIv)
        setIconStatus(isLike, binding.songLikeBtnOnIv, binding.songLikeBtnOffIv)
        setIconStatus(isUnlike, binding.songUnlikeBtnOnIv, binding.songUnlikeBtnOffIv)

        binding.songPlayTimeBar.progress = song.currentMillis/song.playTime
        binding.songPlayTimeCurrentTv.text = String.format("%02d:%02d", song.currentMillis/1000/60, song.currentMillis/1000%60)

        when(musicRepeatMode){
            0 -> {
                binding.songPlayerRepeatBtn0Iv.visibility = View.VISIBLE
                binding.songPlayerRepeatBtn1Iv.visibility = View.GONE
                binding.songPlayerRepeatBtn2Iv.visibility = View.GONE
            }
            1 -> {
                binding.songPlayerRepeatBtn0Iv.visibility = View.GONE
                binding.songPlayerRepeatBtn1Iv.visibility = View.VISIBLE
                binding.songPlayerRepeatBtn2Iv.visibility = View.GONE
            }
            2 -> {
                binding.songPlayerRepeatBtn0Iv.visibility = View.GONE
                binding.songPlayerRepeatBtn1Iv.visibility = View.GONE
                binding.songPlayerRepeatBtn2Iv.visibility = View.VISIBLE
            }
        }

        // 닫기 버튼
        binding.songCloseBtnIv.setOnClickListener{

            val intent = Intent(this, MainActivity::class.java)
            song.currentMillis = player.millis
            intent.putExtra("isPlaying", isPlaying)
            intent.putExtra("songJson", song)
            intent.putExtra("isLike", isLike)
            intent.putExtra("isUnlike", isUnlike)
            intent.putExtra("isMixed", isMixed)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        // 재생, 일시정지 버튼
        binding.songPlayerControlBtn.setOnClickListener{
            if(isPlaying){
                mediaPlayerService.stopMusic()
                song.currentMillis = player.millis
            } else {
                if(musicRepeatMode == 0 && player.state == Thread.State.TERMINATED){
                    if (binding.songPlayTimeBar.progress == 1000){
                        binding.songPlayTimeBar.progress = 0
                        song.currentMillis = 0
                        player = Player(song.playTime, song.currentMillis, isPlaying)
                        player.start()
                    } else {
                        song.currentMillis = (binding.songPlayTimeBar.progress)*(song.playTime)
                        player = Player(song.playTime, song.currentMillis, isPlaying)
                        player.start()
                    }
                }
                mediaPlayerService.playMusic(song.currentMillis)
            }
            setIconStatus(isPlaying, binding.songPlayerPlayBtnIv, binding.songPlayerPauseBtnIv)
            isPlaying = setIconStatus(isPlaying, binding.songPlayerPlayBtnIv, binding.songPlayerPauseBtnIv)
            player.isPlaying = isPlaying
        }

        // 플레이타임 조절 리스너
        binding.songPlayTimeBar.setOnSeekBarChangeListener(SeekbarListener())

        // 믹스재생 버튼
        binding.songPlayerRandomBtn.setOnClickListener{
            setIconStatus(isMixed, binding.songPlayerRandomBtnOffIv, binding.songPlayerRandomBtnOnIv)
            isMixed = setIconStatus(isMixed, binding.songPlayerRandomBtnOffIv, binding.songPlayerRandomBtnOnIv)
        }

        // 좋아요 버튼
        binding.songLikeBtn.setOnClickListener{
            Log.d("isLike", isLike.toString())
            setIconStatus(isLike, binding.songLikeBtnOffIv, binding.songLikeBtnOnIv)
            isLike = setIconStatus(isLike, binding.songLikeBtnOffIv, binding.songLikeBtnOnIv)
            if(isLike == true){
                Toast.makeText(this, "좋아요 표시한 플레이 리스트에 담았습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 싫어요 버튼
        binding.songUnlikeBtn.setOnClickListener{
            setIconStatus(isUnlike, binding.songUnlikeBtnOffIv, binding.songUnlikeBtnOnIv)
            isUnlike = setIconStatus(isUnlike, binding.songUnlikeBtnOffIv, binding.songUnlikeBtnOnIv)
            if(isUnlike == true){
                Toast.makeText(this, "더이상 이 곡을 표시하지 않습니다.",Toast.LENGTH_SHORT).show()
            }
        }

        // 반복재생 버튼
        binding.songPlayerRepeatBtn.setOnClickListener{
            when(musicRepeatMode){
                0 -> {
                    binding.songPlayerRepeatBtn0Iv.visibility = View.GONE
                    binding.songPlayerRepeatBtn1Iv.visibility = View.VISIBLE
                    binding.songPlayerRepeatBtn2Iv.visibility = View.GONE
                    musicRepeatMode = 1
                }
                1 -> {
                    binding.songPlayerRepeatBtn0Iv.visibility = View.GONE
                    binding.songPlayerRepeatBtn1Iv.visibility = View.GONE
                    binding.songPlayerRepeatBtn2Iv.visibility = View.VISIBLE
                    musicRepeatMode = 2
                }
                2 -> {
                    binding.songPlayerRepeatBtn0Iv.visibility = View.VISIBLE
                    binding.songPlayerRepeatBtn1Iv.visibility = View.GONE
                    binding.songPlayerRepeatBtn2Iv.visibility = View.GONE
                    musicRepeatMode = 0
                }
            }
        }
    }

    fun setIconStatus(status : Boolean, icon_1st : View, icon_2nd : View) : Boolean{
        if(status == true){
            icon_1st.visibility = View.VISIBLE
            icon_2nd.visibility = View.GONE
            return false
        } else {
            icon_1st.visibility = View.GONE
            icon_2nd.visibility = View.VISIBLE
            return true
        }
    }

    override fun onBackPressed() {

        val intent = Intent(this, MainActivity::class.java)
        song.currentMillis = player.millis
        intent.putExtra("isPlaying", isPlaying)
        intent.putExtra("songJson", song)
        intent.putExtra("isLike", isLike)
        intent.putExtra("isUnlike", isUnlike)
        intent.putExtra("isMixed", isMixed)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    inner class SeekbarListener() : SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            player.millis = progress*(song.playTime)
            binding.songPlayTimeCurrentTv.text = String.format("%02d:%02d", player.millis/1000/60, player.millis/1000%60)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            if(isPlaying){
                mediaPlayerService.stopMusic()
            }
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            song.currentMillis = player.millis
            if(isPlaying){
                mediaPlayerService.playMusic(song.currentMillis)
            }
        }
    }
    inner class Player(private val playTime : Int, private val currentMillis : Int, var isPlaying : Boolean) : Thread(){
        var millis = currentMillis

        override fun run() {
            try {
                while (true){
                    if (millis/1000 >= playTime){
                        mediaPlayerService.stopMusic()
                        if(musicRepeatMode == 0){
                            runOnUiThread{
                                setIconStatus(true, binding.songPlayerPlayBtnIv, binding.songPlayerPauseBtnIv)
                                isPlaying = setIconStatus(true, binding.songPlayerPlayBtnIv, binding.songPlayerPauseBtnIv)
                                player.isPlaying = isPlaying
                            }
                            break
                        } else {
                            millis = 0
                            song.currentMillis = 0
                            mediaPlayerService.playMusic(song.currentMillis)
                        }
                        continue
                    } else {
                        if(isPlaying){
                            sleep(1)
                            millis++

                            runOnUiThread{
                                binding.songPlayTimeBar.progress = millis/playTime
                                binding.songPlayTimeCurrentTv.text = String.format("%02d:%02d", millis/1000/60, millis/1000%60)
                            }
                        }
                    }
                }
            } catch (e : InterruptedException){
                Log.d("interrupt", "쓰레드 종료")
            }
        }
    }

    override fun onStart() {
        Log.d("state", "Song onStart()")
        super.onStart()
    }
    override fun onPause() {
        Log.d("state", "Song onPause()")
        super.onPause()
    }

    override fun onStop() {
        Log.d("state", "Song onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("state", "Song onDestroy()")
        player.interrupt()
        unbindService(connection)
        super.onDestroy()
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MediaPlayerService.LocalBinder
            mediaPlayerService = binder.getService()
            mediaPlayerService.initService(song)
            binding.songPlayTimeTv.text = String.format("%02d:%02d", mediaPlayerService.playTime/1000/60, mediaPlayerService.playTime/1000%60)
            isServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceBound = false
            Log.d("service", "service disconnected")
        }
    }
}