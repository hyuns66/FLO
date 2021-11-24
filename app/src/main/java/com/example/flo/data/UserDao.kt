package com.example.flo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insert(user:User)

    @Query("SELECT * FROM UserTable")
    fun getUsers() : List<User>

    @Query("SELECT * FROM UserTable WHERE email= :email")
    fun getUser(email : String) : User?
}