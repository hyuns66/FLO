package com.example.flo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.Album
import com.example.flo.databinding.ItemLockerSavedAlbumRvBinding

class LockerSavedAlbumRvAdapter(private val albumDatas : ArrayList<Album>) : RecyclerView.Adapter<LockerSavedAlbumRvAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemLockerSavedAlbumRvBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(albumData : Album){
            binding.lockerSavedAlbumIv.setImageResource(albumData.coverImg!!)
            binding.lockerSavedAlbumTitleTv.text = albumData.title
            binding.lockersavedAlbumArtistTv.text = albumData.artist
            binding.lockerSavedAlbumPublishInfoTv.text = albumData.albumInfo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLockerSavedAlbumRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumDatas[position])
    }

    override fun getItemCount(): Int {
        return albumDatas.size
    }
}