package com.example.flo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class User(
        val email : String,
        val password : String
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}