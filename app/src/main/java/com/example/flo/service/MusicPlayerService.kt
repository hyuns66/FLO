package com.example.flo.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.flo.R

class MusicPlayerService : Service() {

    var music = 0
    var millis = 0
    var mediaPlayer : MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val stringmusic = intent.getStringExtra("musicService")!!
            music = resources.getIdentifier(stringmusic, "raw", this.packageName)
            millis = intent.getIntExtra("millisService", 0)
        }
        mediaPlayer = MediaPlayer.create(this, music)
        mediaPlayer?.setLooping(false) // 반복재생
        mediaPlayer?.seekTo(millis)
        mediaPlayer?.start()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }
}