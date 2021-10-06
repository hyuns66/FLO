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
    var isLike = false
    var isUnlike = false
    private var isPlaying = false
    lateinit var binding : ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 데이터 렌더링
        if(intent.hasExtra("title") && intent.hasExtra("artist") && intent.hasExtra("isPlaying")
                && intent.hasExtra("isLike") &&intent.hasExtra("isUnlike")){
            binding.songTitleTv.text = intent.getStringExtra("title")
            binding.songArtistTv.text = intent.getStringExtra("artist")
            isPlaying = intent.getBooleanExtra("isPlaying", false)
            isLike = intent.getBooleanExtra("isLike", false)
            isUnlike = intent.getBooleanExtra("isUnlike", false)
            isMixed = intent.getBooleanExtra("isMixed", false)
        }

        Log.d("isLike", isLike.toString())
        Log.d("isUnLike", isUnlike.toString())

        // 뷰 초기화
        setIconStatus(isPlaying, binding.songPlayerPauseBtnIv, binding.songPlayerPlayBtnIv)
        setIconStatus(isMixed, binding.songPlayerRandomBtnOnIv, binding.songPlayerRandomBtnOffIv)
        setIconStatus(isLike, binding.songLikeBtnOnIv, binding.songLikeBtnOffIv)
        setIconStatus(isUnlike, binding.songUnlikeBtnOnIv, binding.songUnlikeBtnOffIv)

        // 닫기 버튼
        binding.songCloseBtnIv.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("isPlaying", isPlaying)
            intent.putExtra("isLike", isLike)
            intent.putExtra("isUnlike", isUnlike)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        // 재생, 일시정지 버튼
        binding.songPlayerControlBtn.setOnClickListener{
            setIconStatus(isPlaying, binding.songPlayerPlayBtnIv, binding.songPlayerPauseBtnIv)
            isPlaying = setIconStatus(isPlaying, binding.songPlayerPlayBtnIv, binding.songPlayerPauseBtnIv)
            Log.d("isPlaying", isPlaying.toString())
        }

        // 믹스재생 버튼
        binding.songPlayerRandomBtn.setOnClickListener{
            setIconStatus(isMixed, binding.songPlayerRandomBtnOffIv, binding.songPlayerRandomBtnOnIv)
            isMixed = setIconStatus(isMixed, binding.songPlayerRandomBtnOffIv, binding.songPlayerRandomBtnOnIv)
            Log.d("isMixed", isMixed.toString())
        }

        // 좋아요 버튼
        binding.songLikeBtn.setOnClickListener{
            Log.d("isLike", isLike.toString())
            setIconStatus(isLike, binding.songLikeBtnOffIv, binding.songLikeBtnOnIv)
            isLike = setIconStatus(isLike, binding.songLikeBtnOffIv, binding.songLikeBtnOnIv)
            Log.d("isLike", isLike.toString())
        }

        // 싫어요 버튼
        binding.songUnlikeBtn.setOnClickListener{
            setIconStatus(isUnlike, binding.songUnlikeBtnOffIv, binding.songUnlikeBtnOnIv)
            isUnlike = setIconStatus(isUnlike, binding.songUnlikeBtnOffIv, binding.songUnlikeBtnOnIv)
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
        intent.putExtra("isPlaying", isPlaying)
        intent.putExtra("isLike", isLike)
        intent.putExtra("isUnlike", isUnlike)
        intent.putExtra("isMixed", isMixed)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
        Log.d("finish", "finish()")
    }
}