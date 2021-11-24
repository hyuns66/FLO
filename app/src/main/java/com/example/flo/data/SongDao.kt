package com.example.flo.data

import androidx.room.*

@Dao
interface SongDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(song:Song)

    @Update
    fun update(song:Song)

    @Delete
    fun delete(song:Song)

    @Query("SELECT * FROM SongTable")
    fun getSongs() : List<Song>

    @Query("SELECT * FROM SongTable WHERE music = :music")
    fun getSong(music : String) : Song

    @Query("UPDATE SongTable SET isLike = :isLike WHERE music = :music")
    fun updateIsLikeByMusic(isLike : Boolean, music : String)

    @Query("SELECT * FROM SongTable WHERE isLike = :isLike")
    fun getIsLikedSongs(isLike : Boolean) : List<Song>

    @Query("SELECT * FROM SongTable WHERE albumTitle = :albumTitle")
    fun getAlbumSongs(albumTitle : String) : List<Song>
}