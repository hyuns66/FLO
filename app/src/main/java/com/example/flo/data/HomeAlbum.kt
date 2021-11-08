package com.example.flo.data

data class HomeAlbum(
        var title : String? = "",
        var artist : String? = "",
        var coverImg : Int? = null,
        var songs : ArrayList<Song>? = null
)
