package com.example.flo

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import java.util.zip.Inflater

class SongActivity : AppCompatActivity(){
    lateinit var binding : ActivitySongBinding
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}