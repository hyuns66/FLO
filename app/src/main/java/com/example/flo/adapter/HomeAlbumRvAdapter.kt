package com.example.flo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.HomeAlbum
import com.example.flo.databinding.ItemHomeTodayPublishedAlbumRvBinding

class HomeAlbumRvAdapter(private val albumList : ArrayList<HomeAlbum>) : RecyclerView.Adapter<HomeAlbumRvAdapter.ViewHolder>(){

    // 클릭이벤트 인터페이스
    interface ItemClickListener{
        fun onItemClick(homeAlbum: HomeAlbum)
    }

    // 리스너 객체를 전달받는 함수
    private lateinit var mItemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HomeAlbumRvAdapter.ViewHolder {
        val binding = ItemHomeTodayPublishedAlbumRvBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeAlbumRvAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener{
            mItemClickListener.onItemClick(albumList[position])
        }
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    // 뷰 홀더
    inner class ViewHolder(val binding : ItemHomeTodayPublishedAlbumRvBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(album: HomeAlbum){
            binding.homeTodayAlbumTitleTv.text = album.title
            binding.homeTodayAlbumArtistTv.text = album.artist
            binding.homeTodayAlbumIv.setImageResource(album.coverImg!!)
        }
    }
}