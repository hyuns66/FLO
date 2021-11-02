package com.example.flo.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.flo.R

class MusicPlayerService : Service() {

    var music = 0
    var mediaPlayer : MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        mediaPlayer = MediaPlayer.create(this, music)
        mediaPlayer?.setLooping(false) // 반복재생
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer?.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        super.onDestroy()
    }
}