package com.example.flo.activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.data.Song
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson
import java.lang.Thread.yield

class SongActivity : AppCompatActivity(){

    var isMixed = false
    var isLike = false
    var isUnlike = false
    private val song : Song = Song()
    private var mediaPlayer: MediaPlayer? = null
    private val gson : Gson = Gson()
    private lateinit var player : Player
    lateinit var binding : ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 데이터 렌더링
        if(intent.hasExtra("title") && intent.hasExtra("artist") && intent.hasExtra("isPlaying")
                && intent.hasExtra("isLike") && intent.hasExtra("isUnlike") && intent.hasExtra("playTime")
                && intent.hasExtra("currentMillis") && intent.hasExtra("music")){
            song.title = intent.getStringExtra("title")!!
            song.artist = intent.getStringExtra("artist")!!
            song.music = intent.getStringExtra("music")!!
            song.playTime = intent.getIntExtra("playTime", 0)
            song.currentMillis = intent.getIntExtra("currentMillis", 0)
            song.isPlaying = intent.getBooleanExtra("isPlaying", false)
            song.musicRepeatMode = intent.getIntExtra("musicRepeatMode", 0)

            var music = resources.getIdentifier(song.music, "raw", this.packageName)
            if(mediaPlayer == null){
                mediaPlayer = MediaPlayer.create(this, music)
                song.playTime = mediaPlayer?.duration!!/1000
            }
            binding.songTitleTv.text = song.title
            binding.songArtistTv.text = song.artist
            binding.songPlayTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime%60)
            isLike = intent.getBooleanExtra("isLike", false)
            isUnlike = intent.getBooleanExtra("isUnlike", false)
            isMixed = intent.getBooleanExtra("isMixed", false)
        }

        // 뷰 초기화
        setIconStatus(song.isPlaying, binding.songPlayerPauseBtnIv, binding.songPlayerPlayBtnIv)
        setIconStatus(isMixed, binding.songPlayerRandomBtnOnIv, binding.songPlayerRandomBtnOffIv)
        setIconStatus(isLike, binding.songLikeBtnOnIv, binding.songLikeBtnOffIv)
        setIconStatus(isUnlike, binding.songUnlikeBtnOnIv, binding.songUnlikeBtnOffIv)

        binding.songPlayTimeBar.progress = song.currentMillis/song.playTime
        binding.songPlayTimeCurrentTv.text = String.format("%02d:%02d", song.currentMillis/1000/60, song.currentMillis/1000%60)
        when(song.musicRepeatMode){
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

        // Player() 스레드 생성
        player = Player(song.playTime, song.currentMillis,  song.isPlaying)
        player.start()

        // 닫기 버튼
        binding.songCloseBtnIv.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("isPlaying", song.isPlaying)
            intent.putExtra("currentMillis", player.millis)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("music", song.music)
            intent.putExtra("title", song.title)
            intent.putExtra("artist", song.artist)
            intent.putExtra("musicRepeatMode", song.musicRepeatMode)
            intent.putExtra("isLike", isLike)
            intent.putExtra("isUnlike", isUnlike)
            intent.putExtra("isMixed", isMixed)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        // 재생, 일시정지 버튼
        binding.songPlayerControlBtn.setOnClickListener{
            if(song.isPlaying == true){
                mediaPlayer?.pause()
            } else {
                mediaPlayer?.start()
            }
            setIconStatus(song.isPlaying, binding.songPlayerPlayBtnIv, binding.songPlayerPauseBtnIv)
            song.isPlaying = setIconStatus(song.isPlaying, binding.songPlayerPlayBtnIv, binding.songPlayerPauseBtnIv)
            player.isPlaying = song.isPlaying
            if(song.musicRepeatMode == 0 && player.state == Thread.State.TERMINATED){
                if (binding.songPlayTimeBar.progress == 1000){
                    binding.songPlayTimeBar.progress = 0
                    song.currentMillis = 0
                    player = Player(song.playTime, song.currentMillis, song.isPlaying)
                    player.start()
                } else {
                    song.currentMillis = (binding.songPlayTimeBar.progress)*(song.playTime)
                    player = Player(song.playTime, song.currentMillis, song.isPlaying)
                    player.start()
                }
            }
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
            when(song.musicRepeatMode){
                0 -> {
                    binding.songPlayerRepeatBtn0Iv.visibility = View.GONE
                    binding.songPlayerRepeatBtn1Iv.visibility = View.VISIBLE
                    binding.songPlayerRepeatBtn2Iv.visibility = View.GONE
                    song.musicRepeatMode = 1
                }
                1 -> {
                    binding.songPlayerRepeatBtn0Iv.visibility = View.GONE
                    binding.songPlayerRepeatBtn1Iv.visibility = View.GONE
                    binding.songPlayerRepeatBtn2Iv.visibility = View.VISIBLE
                    song.musicRepeatMode = 2
                }
                2 -> {
                    binding.songPlayerRepeatBtn0Iv.visibility = View.VISIBLE
                    binding.songPlayerRepeatBtn1Iv.visibility = View.GONE
                    binding.songPlayerRepeatBtn2Iv.visibility = View.GONE
                    song.musicRepeatMode = 0
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
        intent.putExtra("isPlaying", song.isPlaying)
        intent.putExtra("playTime", song.playTime)
        intent.putExtra("currentMillis", player.millis)
        intent.putExtra("title", song.title)
        intent.putExtra("artist", song.artist)
        intent.putExtra("music", song.music)
        intent.putExtra("musicRepeatMode", song.musicRepeatMode)
        intent.putExtra("isLike", isLike)
        intent.putExtra("isUnlike", isUnlike)
        intent.putExtra("isMixed", isMixed)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    inner class SeekbarListener : SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            player.millis = progress*(song.playTime)
            binding.songPlayTimeCurrentTv.text = String.format("%02d:%02d", player.millis/1000/60, player.millis/1000%60)
            // 사용자가 progress 조작 시 미디어플레이어 위치변경
            if(fromUser){
                mediaPlayer?.seekTo(progress*(song.playTime))
            }

        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }

    }
    inner class Player(private val playTime : Int, private val currentMillis : Int, var isPlaying : Boolean) : Thread(){
        var millis = currentMillis

        override fun run() {
            try {
                while (true){
                    if (millis/1000 >= playTime){
                        if(song.musicRepeatMode == 0){
                            runOnUiThread{
                                setIconStatus(true, binding.songPlayerPlayBtnIv, binding.songPlayerPauseBtnIv)
                                song.isPlaying = setIconStatus(true, binding.songPlayerPlayBtnIv, binding.songPlayerPauseBtnIv)
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

    override fun onPause() {
        mediaPlayer?.pause()
        player.isPlaying = false
        song.isPlaying = false
        song.currentMillis = player.millis
        setIconStatus(true, binding.songPlayerPlayBtnIv, binding.songPlayerPauseBtnIv)
        // sharedPreferences
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val songJson = gson.toJson(song)
        editor.putString("song", songJson)
        editor.apply()
        super.onPause()
    }

    override fun onDestroy() {
        player.interrupt()
        super.onDestroy()
    }
}