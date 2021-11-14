package com.example.flo.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.flo.data.Song

class MediaPlayerService : Service() {
    private val mBinder = LocalBinder()
    var music = 0
    var playTime = 0
    var mediaPlayer : MediaPlayer? = null

    inner class LocalBinder : Binder() {
        fun getService() : MediaPlayerService = this@MediaPlayerService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        if(!mediaPlayer?.isPlaying!!){
            mediaPlayer?.release()
            mediaPlayer = null
        }
        super.onDestroy()
    }

    fun playMusic(song : Song){
        mediaPlayer?.seekTo(song.currentMillis)
        mediaPlayer?.start()
    }

    fun stopMusic(){
        mediaPlayer?.pause()
    }

    fun initService(song : Song){
        if(mediaPlayer == null){
            music = resources.getIdentifier(song.music, "raw", this.packageName)
            mediaPlayer = MediaPlayer.create(this, music)
            mediaPlayer?.isLooping = false // 반복재생
        }
        playTime = mediaPlayer?.duration!!
    }
}