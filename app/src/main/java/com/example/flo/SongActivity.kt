package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import java.util.zip.Inflater

class SongActivity : AppCompatActivity(){

    var isMixed = false
    var musicRepeatMode : Int = 0
    private var isPlaying = false
    lateinit var binding : ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 데이터 렌더링
        if(intent.hasExtra("title") && intent.hasExtra("artist") && intent.hasExtra("isPlaying")){
            binding.songTitleTv.text = intent.getStringExtra("title")
            binding.songArtistTv.text = intent.getStringExtra("artist")
            isPlaying = intent.getBooleanExtra("isPlaying", false)
        }

        // 뷰 초기화
        if(isPlaying == true){
            binding.songPlayerPlayBtnIv.visibility = View.GONE
            binding.songPlayerPauseBtnIv.visibility = View.VISIBLE
        } else {
            binding.songPlayerPlayBtnIv.visibility = View.VISIBLE
            binding.songPlayerPauseBtnIv.visibility = View.GONE
        }

        // 닫기 버튼
        binding.songCloseBtnIv.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("isPlaying", isPlaying)
            startActivity(intent)
            finish()
        }

        // 재생, 일시정지 버튼
        binding.songPlayerControlBtn.setOnClickListener{
            setPlayerStatus(isPlaying)
            isPlaying = setPlayerStatus(isPlaying)
            Log.d("isPlaying", isPlaying.toString())
        }

        // 믹스재생 버튼
        binding.songPlayerRandomBtn.setOnClickListener{
            setPlayListMix(isMixed)
            isMixed = setPlayListMix(isMixed)
            Log.d("isMixed", isMixed.toString())
        }

        // 반복재생 버튼
        binding.songPlayerRepeatBtn.setOnClickListener{
            when(musicRepeatMode){
                0 -> {
                    binding.songPlayerRepeatBtn0Iv.visibility = View.GONE
                    binding.songPlayerRepeatBtn1Iv.visibility = View.VISIBLE
                    binding.songPlayerRepeatBtn2Iv.visibility = View.GONE
                    musicRepeatMode = 1
                    Log.d("musicRepeatMode", musicRepeatMode.toString())
                }
                1 -> {
                    binding.songPlayerRepeatBtn0Iv.visibility = View.GONE
                    binding.songPlayerRepeatBtn1Iv.visibility = View.GONE
                    binding.songPlayerRepeatBtn2Iv.visibility = View.VISIBLE
                    musicRepeatMode = 2
                    Log.d("musicRepeatMode", musicRepeatMode.toString())
                }
                2 -> {
                    binding.songPlayerRepeatBtn0Iv.visibility = View.VISIBLE
                    binding.songPlayerRepeatBtn1Iv.visibility = View.GONE
                    binding.songPlayerRepeatBtn2Iv.visibility = View.GONE
                    musicRepeatMode = 0
                    Log.d("musicRepeatMode", musicRepeatMode.toString())
                }
            }
        }
    }

    fun setPlayerStatus(isPlaying : Boolean) : Boolean{
        if(isPlaying == true){
            binding.songPlayerPlayBtnIv.visibility = View.VISIBLE
            binding.songPlayerPauseBtnIv.visibility = View.GONE
            return false
        } else {
            binding.songPlayerPlayBtnIv.visibility = View.GONE
            binding.songPlayerPauseBtnIv.visibility = View.VISIBLE
            return true
        }
    }

    fun setPlayListMix(isMixed : Boolean) : Boolean{
        if(isMixed == true){
            binding.songPlayerRandomBtnOffIv.visibility = View.VISIBLE
            binding.songPlayerRandomBtnOnIv.visibility = View.GONE
            return false
        } else {
            binding.songPlayerRandomBtnOffIv.visibility = View.GONE
            binding.songPlayerRandomBtnOnIv.visibility = View.VISIBLE
            return true
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("isPlaying", isPlaying)
        startActivity(intent)
        finish()
    }

}