package com.example.flo

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(
        val title : String,
        val artist : String,
        val mainImgURL : Bitmap?
) : Parcelable
