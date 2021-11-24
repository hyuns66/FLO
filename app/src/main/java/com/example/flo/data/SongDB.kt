package com.example.flo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Song::class, Album::class], version = 1)
abstract class SongDB : RoomDatabase() {
    abstract fun SongDao() : SongDao
    abstract fun AlbumDao() : AlbumDao

    companion object{
        private var instance : SongDB? = null

        @Synchronized
        fun getInstance(context: Context) : SongDB? {
            if (instance == null){
                synchronized(SongDB::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDB::class.java,
                        "user-database"
                    ).allowMainThreadQueries().build()
                }
            }
            return  instance
        }
    }
}