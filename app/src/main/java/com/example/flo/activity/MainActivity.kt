package com.example.flo.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.flo.fragment.HomeFragment
import com.example.flo.fragment.LockerFragment
import com.example.flo.fragment.LookFragment
import com.example.flo.fragment.SearchFragment
import com.example.flo.R
import com.example.flo.data.Album
import com.example.flo.data.Song
import com.example.flo.data.SongDB
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.service.MediaPlayerService
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    var isUnlike = false
    var isMixed = false
    var isPlaying = false
    var musicRepeatMode = 0
    var playList : ArrayList<Song> = arrayListOf()
    var playListPosition = 0
    private var song : Song = Song()
    private lateinit var mediaPlayerService : MediaPlayerService
    private var isServiceBound = false
    lateinit var player : Player
    lateinit var binding: ActivityMainBinding
    var backPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d("state", "Main onCreate()")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val songDB = SongDB.getInstance(this)!!
        initNavigation()
        initDummySongs(songDB)
        initDummyAlbums(songDB)
        initPlayList(songDB.SongDao().getSongs())

        val serviceIntent = Intent(this,MediaPlayerService::class.java)
        bindService(serviceIntent, connection, BIND_AUTO_CREATE)

        // sharedPreferences
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val playedMusic = sharedPreferences.getString("music", null)
        if(playedMusic == null){
            initMusic(getPlayListPosition("savage"))
        } else {
            initMusic(getPlayListPosition(playedMusic))
        }
        song.currentMillis = sharedPreferences.getInt("currentMillis", 1234)
        song.playTime = sharedPreferences.getInt("playTime", 200)

        // ??? ?????????
        binding.mainPlayTimeBar.isEnabled = false
        binding.mainMiniplayerBtn.visibility = View.VISIBLE
        binding.mainPauseBtn.visibility = View.GONE

        // ????????? ??????, ??????
        player = Player(song.playTime, song.currentMillis, isPlaying)
        player.start()

        // ?????????, ?????? ??????
        binding.mainMiniplayerBtn.setOnClickListener{
            setPlayerStatus(song)
            isPlaying = true
            player.isPlaying = true
            if(musicRepeatMode == 0 && player.state == Thread.State.TERMINATED){
                if (binding.mainPlayTimeBar.progress == 1000){
                    binding.mainPlayTimeBar.progress = 0
                    song.currentMillis = 0
                    player = Player(song.playTime, song.currentMillis, isPlaying)
                    player.start()
                } else {
                    song.currentMillis = (binding.mainPlayTimeBar.progress)*(song.playTime)
                }
            }
            mediaPlayerService.playMusic(song.currentMillis)
        }

        binding.mainPauseBtn.setOnClickListener{
            setPlayerStatus(song)
            isPlaying = false
            player.isPlaying = false
            song.currentMillis = player.millis
            mediaPlayerService.stopMusic()
        }

        // ?????? ??????
        binding.mainPreviousBtn.setOnClickListener {
            if(playListPosition > 0){
                playListPosition--
                setMusic(playListPosition)
            } else {
                Toast.makeText(this, "????????? ???????????? ??? ???????????????.", Toast.LENGTH_SHORT).show()
            }
        }

        // ?????? ??????
        binding.mainNextBtn.setOnClickListener {
            if(playListPosition < playList.size - 1){
                playListPosition++
                setMusic(playListPosition)
            } else {
                Toast.makeText(this, "????????? ???????????? ??? ??????????????????.", Toast.LENGTH_SHORT).show()
            }
        }

        // SongActivity intent
        binding.mainPlayerLayout.setOnClickListener{

            val intent = Intent(this, SongActivity::class.java)
            song.playTime = mediaPlayerService.playTime/1000
            Log.d("playtime", song.playTime.toString())
            song.currentMillis = player.millis

            intent.putExtra("playListPosition", playListPosition)
            intent.putExtra("playList", playList)
            intent.putExtra("songJson", song)
            intent.putExtra("isPlaying", isPlaying)
            intent.putExtra("isUnlike", isUnlike)
            intent.putExtra("isMixed", isMixed)
            startActivity(intent)
        }

        // ?????? ??????????????? ???
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
        if(isPlaying){
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
                Toast.makeText(this, "'??????' ????????? ?????? ??? ????????? ???????????????.",Toast.LENGTH_SHORT).show()
                return
            } else {
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
            playListPosition = intent.getIntExtra("playListPosition", 0)
            isPlaying = intent.getBooleanExtra("isPlaying", false)
            song = intent.getParcelableExtra("songJson")!!
            isUnlike = intent.getBooleanExtra("isUnlike", false)
            isMixed = intent.getBooleanExtra("isMixed", false)
            playList = intent.getParcelableArrayListExtra<Song>("playList")!!
            player.mPlayTime = song.playTime
        }
        super.onNewIntent(intent)
    }

    override fun onResume() {
        Log.d("state", "Main onResume()")

        // ???????????? ??? ?????????
        if(isPlaying){
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }
        player.millis = song.currentMillis
        player.isPlaying = isPlaying

        binding.mainPlayerTitleTv.text = song.title
        binding.mainPlayerArtistTv.text = song.artist
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
        player.isPlaying = false
        isPlaying = false
        song.playTime = mediaPlayerService.playTime/1000
        song.currentMillis = player.millis
        // sharedPreferences
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("currentMillis", song.currentMillis)
        editor.putString("music", song.music)
        editor.putInt("playTime", song.playTime)
        editor.apply()
        player.interrupt()
        unbindService(connection)
        super.onDestroy()
    }

    private val connection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MediaPlayerService.LocalBinder
            mediaPlayerService = binder.getService()
            mediaPlayerService.initService(song)
            isServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceBound = false
        }

    }

    private fun initDummyAlbums(songDB: SongDB){
        val albums = songDB.AlbumDao().getAlbums()

        if (albums.isNotEmpty()) return

        songDB.AlbumDao().insert(
                Album("IU 5th Album 'LILAC'",
                    "?????????(IU)",
                    "2021.03.25 | ?????? | ?????? ??????/??????",
                        R.drawable.img_album_exp2,
                    false)
        )

        songDB.AlbumDao().insert(
                Album("strawberry moon",
                        "?????????(IU)",
                    "2021.10.19 | ?????? | ?????? ???/??????",
                        R.drawable.img_album_exp3,
                        false)
        )

        songDB.AlbumDao().insert(
                Album("Savage - The 1st Mini Album",
                        "?????????(aespa)",
                    "2021.10.05 | ?????? | ??????",
                        R.drawable.img_album_exp4,
                        false)
        )

        songDB.AlbumDao().insert(
                Album("Weekend",
                        "??????(TAEYEON)",
                    "2021.07.06 | ?????? | ?????? ???",
                        R.drawable.img_album_exp5,
                        false)
        )
    }

    private fun initDummySongs(songDB : SongDB){
        val songs = songDB.SongDao().getSongs()

        if (songs.isNotEmpty()) return
        songDB.SongDao().insert(
                Song(
                        "LILAC",
                        "?????????(IU)",
                        "IU 5th Album 'LILAC'",
                        "iu_lilac",
                        200,
                        0,
                        false,
                        R.drawable.img_album_exp2
                )
        )
        songDB.SongDao().insert(
                Song(
                        "Flu",
                        "?????????(IU)",
                        "IU 5th Album 'LILAC'",
                        "iu_flu",
                        200,
                        0,
                        false,
                        R.drawable.img_album_exp2
                )
        )
        songDB.SongDao().insert(
                Song(
                        "Coin",
                        "?????????(IU)",
                        "IU 5th Album 'LILAC'",
                        "iu_coin",
                        200,
                        0,
                        false,
                        R.drawable.img_album_exp2
                )
        )
        songDB.SongDao().insert(
                Song(
                        "??? ?????? ???",
                        "?????????(IU)",
                        "IU 5th Album 'LILAC'",
                        "iu_spring_hello_spring",
                        200,
                        0,
                        false,
                        R.drawable.img_album_exp2
                )
        )
        songDB.SongDao().insert(
                Song(
                        "Celebrity",
                        "?????????(IU)",
                        "IU 5th Album 'LILAC'",
                        "iu_celebrity",
                        200,
                        0,
                        false,
                        R.drawable.img_album_exp2
                )
        )
        songDB.SongDao().insert(
                Song(
                        "???????????? (Feat.DEAN)",
                        "?????????(IU)",
                        "IU 5th Album 'LILAC'",
                        "iu_spinning_song",
                        200,
                        0,
                        false,
                        R.drawable.img_album_exp2
                )
        )
        songDB.SongDao().insert(
                Song(
                        "??? ??? (Empty Cup)",
                        "?????????(IU)",
                        "IU 5th Album 'LILAC'",
                        "iu_empty_cup",
                        200,
                        0,
                        false,
                        R.drawable.img_album_exp2
                )
        )
        songDB.SongDao().insert(
                Song(
                        "????????? ?????? ??????",
                        "?????????(IU)",
                        "IU 5th Album 'LILAC'",
                        "iu_sea_of_child_and_me",
                        200,
                        0,
                        false,
                        R.drawable.img_album_exp2
                )
        )
        songDB.SongDao().insert(
                Song(
                        "?????? (Ah Phu)",
                        "?????????(IU)",
                        "IU 5th Album 'LILAC'",
                        "iu_ah_phu",
                        200,
                        0,
                        false,
                        R.drawable.img_album_exp2
                )
        )
        songDB.SongDao().insert(
                Song(
                        "????????????",
                        "?????????(IU)",
                        "IU 5th Album 'LILAC'",
                        "iu_epilogue",
                        200,
                        0,
                        false,
                        R.drawable.img_album_exp2
                )
        )

        songDB.SongDao().insert(
                Song(
                        "strawberry moon",
                        "?????????(IU)",
                        "strawberry moon",
                        "iu_strawberry_moon",
                        200,
                        0,
                        false,
                        R.drawable.img_album_exp3
                )
        )
        songDB.SongDao().insert(
                Song(
                        "savage",
                        "?????????(aespa)",
                        "Savage - The 1st Mini Album",
                        "aespa_savage",
                        200,
                        0,
                        false,
                        R.drawable.img_album_exp4
                )
        )
        songDB.SongDao().insert(
                Song(
                        "Weekend",
                        "??????(TAEYEON)",
                        "Weekend",
                        "taeyeon_weekend",
                        200,
                        0,
                        false,
                        R.drawable.img_album_exp5
                )
        )
    }

    private fun initPlayList(list : List<Song>){
        playList.addAll(list)
    }

    private fun initMusic(position : Int){
        song = playList[position]
        binding.mainPlayerTitleTv.text = song.title
        binding.mainPlayerArtistTv.text = song.artist
    }

    private fun setMusic(position : Int){
        player.interrupt()
        song = playList[position]
        binding.mainPlayerTitleTv.text = song.title
        binding.mainPlayerArtistTv.text = song.artist
        if(isPlaying){
            mediaPlayerService.stopMusic()
            mediaPlayerService.mediaPlayer?.release()
            mediaPlayerService.mediaPlayer = null
            mediaPlayerService.initService(song)
            mediaPlayerService.playMusic(0)
        } else {
            mediaPlayerService.mediaPlayer?.release()
            mediaPlayerService.mediaPlayer = null
            mediaPlayerService.initService(song)
            binding.mainPlayTimeBar.progress = 0
        }
        song.playTime = mediaPlayerService.playTime/1000
        player = Player(song.playTime, 0, isPlaying)
        player.start()
    }

    private fun getPlayListPosition(music : String) : Int{
        for (i in 0 until playList.size){
            if (playList[i].music == music){
                playListPosition = i
                return i
            }
        }
        playListPosition = 0
        return 0
    }

    inner class Player(private val playTime : Int, private val currentMillis : Int, var isPlaying : Boolean) : Thread(){
        var millis = currentMillis
        var mPlayTime = playTime

        override fun run() {
            try {
                while (true){
                    if (millis/1000 >= mPlayTime){
                        mediaPlayerService.stopMusic()
                        if(musicRepeatMode == 0){
                            runOnUiThread{
                                setPlayerStatus(song)
                                isPlaying = setPlayerStatus(song)
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
                                binding.mainPlayTimeBar.progress = millis/mPlayTime
                            }
                        }
                    }
                }
            } catch (e : InterruptedException){
                Log.d("interrupt", "????????? ??????")
            }
        }
    }

}

