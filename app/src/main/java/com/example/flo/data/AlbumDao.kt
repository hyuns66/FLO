package com.example.flo.data

import androidx.room.*

@Dao
interface AlbumDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(album: Album)

    @Update
    fun update(album : Album)

    @Delete
    fun delete(album : Album)

    @Query("SELECT * FROM AlbumTable")
    fun getAlbums() : List<Album>

    @Query("SELECT * FROM AlbumTable WHERE title = :title")
    fun getAlbum(title : String) : Album

    @Query("SELECT * FROM AlbumTable WHERE isLike = :isLike")
    fun getIsLikedAlbums(isLike : Boolean) :  List<Album>

    @Query("UPDATE AlbumTable SET  isLike = :isLike WHERE title = :title")
    fun setIsLike(isLike: Boolean, title: String)
}