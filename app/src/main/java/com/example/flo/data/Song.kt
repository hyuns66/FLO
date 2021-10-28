package com.example.flo.data

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(
        var title : String = "",
        var artist : String = "",
        var isPlaying : Boolean = false,
        var playTime : Int = 0,
        var currentMillis : Int = 0,
        var musicRepeatMode : Int = 0,
        val mainImgURL : Bitmap? = null
) : Parcelable
