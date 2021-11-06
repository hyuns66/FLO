package com.example.flo.data

import android.graphics.Bitmap
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.example.flo.R

data class Song(
        var title : String = "",
        var artist : String = "",
        var isPlaying : Boolean = false,
        var music : String = "",
        var playTime : Int = 0,
        var currentMillis : Int = 0,
        var musicRepeatMode : Int = 0,
        val mainImgURL : Int = R.drawable.img_album_exp2
) : Parcelable {

    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel : Parcel) : this (
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readBoolean(),
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt()
            )

    override fun describeContents(): Int {
        return 0
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(title)
        dest?.writeString(artist)
        dest?.writeBoolean(isPlaying)
        dest?.writeString(music)
        dest?.writeInt(playTime)
        dest?.writeInt(currentMillis)
        dest?.writeInt(musicRepeatMode)
        dest?.writeInt(mainImgURL)
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
