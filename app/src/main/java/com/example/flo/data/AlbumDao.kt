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

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun likeAlbum(album : SavedAlbum)

    @Query("SELECT * FROM AlbumTable")
    fun getAlbums() : List<Album>

    @Query("SELECT * FROM AlbumTable WHERE title = :title")
    fun getAlbum(title : String) : Album

    @Query("SELECT * FROM LikeTable WHERE userId = :userId AND album= :album")
    fun getIsLikedAlbum(userId: Int, album : String) :  SavedAlbum?

    @Query("SELECT id FROM LikeTable WHERE userId= :userId AND album= :album")
    fun setIsLike(userId: Int, album: String) : Int?

    @Query("DELETE FROM LikeTable WHERE userId= :userId AND album= :album")
    fun disLikeAlbum(userId : Int, album : String)

    @Query("SELECT AT.* FROM LikeTable AS LT LEFT JOIN AlbumTable AS AT ON LT.album = AT.title WHERE LT.userId = :userId")
    fun getLikedAlbums(userId: Int) : List<Album>
}