package com.example.flo.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.R
import com.example.flo.data.HomeAlbum
import com.example.flo.data.Song
import com.example.flo.databinding.ItemHomeTodayPublishedAlbumRvBinding
import com.example.flo.databinding.ItemLockerSavedMusicRvBinding

class LockerSavedMusicRvAdapter() : RecyclerView.Adapter<LockerSavedMusicRvAdapter.ViewHolder>(){

    private val savedSongs = arrayListOf<Song>()
    interface ItemClickListener{
        fun removeSong(music : String)
    }

    private lateinit var mItemClickListener : ItemClickListener

    fun setItemClickListener(itemClickListener : ItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLockerSavedMusicRvBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(savedSongs[position])
        holder.itemView.setOnClickListener {
            Log.e("position", position.toString())
        }
        holder.binding.lockerSavedMusicBtnMoreIv.setOnClickListener{
            mItemClickListener.removeSong(savedSongs[position].music)
            removeSong(position)
        }
    }

    override fun getItemCount(): Int {
        return savedSongs.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(savedSongs : ArrayList<Song>){
        this.savedSongs.clear()
        this.savedSongs.addAll(savedSongs)

        notifyDataSetChanged()
    }

    private fun removeSong(position: Int){
        savedSongs.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    inner class ViewHolder(val binding:ItemLockerSavedMusicRvBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(song : Song){
            binding.lockerSavedMusicTitleTv.text = song.title
            binding.lockerSavedMusicArtistTv.text = song.artist
            binding.lockerSavedMusicIv.setImageResource(song.coverImg)
        }
    }
}