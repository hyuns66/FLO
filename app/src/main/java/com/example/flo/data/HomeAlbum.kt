package com.example.flo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class HomeAlbum(
        @PrimaryKey val title : String = "",
        val artist : String = "",
        val coverImg : Int? = null,
        val songs : ArrayList<Song>? = null
)
