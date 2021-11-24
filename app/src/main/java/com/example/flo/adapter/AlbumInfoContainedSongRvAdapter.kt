package com.example.flo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.Song
import com.example.flo.databinding.ItemAlbumInfoContainedMusicRvBinding

class AlbumInfoContainedSongRvAdapter(private val songList : ArrayList<Song>) : RecyclerView.Adapter<AlbumInfoContainedSongRvAdapter.ViewHolder>() {

    inner class ViewHolder(val binding:ItemAlbumInfoContainedMusicRvBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(song : Song, position: Int){
            binding.albumInfoMusicListTitleTv.text = song.title
            binding.albumInfoMusicListArtistTv.text = song.artist
            binding.albumInfoMusicListNumberTv.text = position.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlbumInfoContainedMusicRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songList[position], position+1)
    }

    override fun getItemCount(): Int {
        return songList.size
    }
}