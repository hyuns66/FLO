package com.example.flo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.R
import com.example.flo.data.HomeAlbum
import com.example.flo.databinding.ItemHomeTodayPublishedAlbumRvBinding
import com.example.flo.databinding.ItemLockerSavedMusicRvBinding

class LockerSavedMusicRvAdapter(private val albumList : ArrayList<HomeAlbum>) : RecyclerView.Adapter<LockerSavedMusicRvAdapter.ViewHolder>(){

    interface ItemClickListener{

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
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener {
            Log.e("position", position.toString())
        }
        holder.binding.lockerSavedMusicBtnMoreIv.setOnClickListener{
            notifyItemMoved(position, position+1)
            notifyItemChanged(position)
            notifyItemChanged(position+1)
        }
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    inner class ViewHolder(val binding:ItemLockerSavedMusicRvBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(album : HomeAlbum){
            binding.lockerSavedMusicTitleTv.text = album.title
            binding.lockerSavedMusicArtistTv.text = album.artist
            binding.lockerSavedMusicIv.setImageResource(album.coverImg!!)
        }
    }
}