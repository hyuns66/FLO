package com.example.flo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LikeTable")
data class SavedAlbum(
        val userId : Int,
        var album : String,
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}