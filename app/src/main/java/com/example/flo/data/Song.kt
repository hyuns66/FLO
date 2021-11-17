package com.example.flo.data

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flo.R

@Entity(tableName = "SongTable")
data class Song(
        var title : String = "",
        var artist : String = "",
        var albumTitle : String = "",
        @PrimaryKey val music : String = "",
        var playTime : Int = 0,
        var currentMillis : Int = 0,
        var isLike : Boolean = false,
        val coverImg : Int = R.drawable.img_album_exp2
) : Parcelable {
//
//    @PrimaryKey(autoGenerate = true) var id : Int = 0

    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel : Parcel) : this (
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readBoolean(),
            parcel.readInt()
            )

    override fun describeContents(): Int {
        return 0
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(title)
        dest?.writeString(artist)
        dest?.writeString(albumTitle)
        dest?.writeString(music)
        dest?.writeInt(playTime)
        dest?.writeInt(currentMillis)
        dest?.writeBoolean(isLike)
        dest?.writeInt(coverImg)
    }

    companion object CREATOR : Parcelable.Creator<Song>{
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(source: Parcel): Song {
            return Song(source)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }

    }

}
